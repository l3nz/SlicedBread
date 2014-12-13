
package ch.loway.oss.slicedbread;

import ch.loway.oss.slicedbread.containers.MsgQueue;
import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.containers.QueueInfo;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.common.MsgProcessEnded;
import ch.loway.oss.slicedbread.messages.common.MsgProcessStarted;
import ch.loway.oss.slicedbread.messages.error.MsgErrProcessDied;
import ch.loway.oss.slicedbread.messages.error.MsgErrUndeliverable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * MessagingConsole
 *
 * This object is the singleton console for all MsgTasking needs.
 * This is the only interactions users have with the MsgTasking world.
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
 */
public class MessagingConsole {

    private static final Logger logger = LoggerFactory.getLogger(MessagingConsole.class);
    private static final MessagingConsole me = new MessagingConsole();
    private final Map<PID,MsgQueue> mQueues = new ConcurrentHashMap<PID,MsgQueue>();

    private final static int POLL_EVERY_MS = 10;

    public static MessagingConsole getConsole() {
        return me;
    }

    /**
     * Remove mappings from previous execution in the same JVM / ClassLoader.
     * Useful e.g. for Unit Testing
     * 
     */

    public void reset() {
        logger.warn( "=========== SlicedBread console reset ================");
        mQueues.clear();
    }



    /**
     * Queues the message for delivery.
     *
     * @param message
     * @return The nummber of messages on the queue it's been delivered to.
     */
    public int send( Msg message ) {
        if ( message == null ) {
            return 0;
        }

        logger.trace("S: {}", message);

        MsgQueue q = getQueue( message.getToPid() );

        // if the destination address was not found...
        if ( q == null ) { 
            returnMsgUndeliverable(message);
            return 0;           
        }
        
        return q.push(message);
    }

    /**
     * Gets you a message from a queue.
     * If the queue is empty, then this call will block for the given timeout (in ms).
     * If you don't want it blocking, pass 0 as the timeout.
     *
     * @param myPid which mailbox to check
     * @param timeout in milliseconds
     * @return a messagem, or null if none is found.
     * @throws InterruptedException
     */
    
    public Msg receive( PID myPid, int timeout ) throws InterruptedException {        
        int timeRemaining = timeout;

        MsgQueue q = getQueue( myPid );
        if ( q == null ) {
            return null;
        }

        do {
            Msg message = q.pull();
            if ( message == null ) {

                if ( timeRemaining > 0 ) {
                    Thread.sleep( POLL_EVERY_MS );
                    timeRemaining = timeRemaining - POLL_EVERY_MS;
                }

            } else {
                // I found a message at last                
                logger.trace("R: {}", message);
                return message;
            }

        } while ( timeRemaining > 0 );

        // no message was found        
        return null;
    }



    /**
     * Returns all messages.
     *
     * @param myPID
     * @return
     */

    public List<Msg> receiveAll( PID myPID ) {
        MsgQueue q = getQueue( myPID );
        return q.fetchAllMessages();
    }



    /**
     * Crea un nuovo PID per un thread esistente.
     * Il PID e' sempre diverso.
     * 
     * @param description
     * @return
     */

    public PID registerExistingThread( String description ) {
        final PID processPid = PID.getNextPid( description );
        addQueue(processPid);
        return processPid;
    }


    /**
     * Given an unique name, creates a new mailbox.
     * 
     * @param uniqueName
     * @return
     */
    public PID registerExistingThreadByName( String uniqueName ) {
        final PID px = whois( uniqueName );
        if ( px != null ) {
            return px;
        } else {
            return registerExistingThread(uniqueName);
        }
    }


    /**
     * Start a thread
     *
     * @param callerPid
     * @param description
     * @param process
     * @return
     */

    public PID register( final PID callerPid, String description, final TaskProcess process ) {

        final PID processPid = PID.getNextPid( description );
        addQueue(processPid);

        process.setOwnPid(processPid);
        process.setParentPid(callerPid);
        process.setConsole(me);

        Runnable task = new Runnable() {

            public void run() {
                try {

                    send( MsgProcessStarted.build( processPid, callerPid));
                    process.run();
                    send( MsgProcessEnded.build( processPid, callerPid ) );


                } catch ( Throwable t)  {

                    logger.error("Process " + processPid + " died ", t);
                    send( MsgErrProcessDied.build( processPid, callerPid, t ) );
                    
                } finally {

                    // Marking all existing messages as "undeliverable"
                    MsgQueue myMailbox = getQueue(processPid);
                    List<Msg> undeliveredMsgs = myMailbox.fetchAllMessages();
                    for ( Msg m: undeliveredMsgs ) {
                        returnMsgUndeliverable(m);
                    }
                    removeQueue(processPid);
                }
            }
        };

        Thread tx = new Thread(task, description);
        tx.setDaemon(true);
        tx.start();

        logger.info( "Thread {} started", description );

        return processPid;

    }

    /**
     * Looks up for a PID given its description.
     * If not found, returns null.
     * 
     * @param description
     * @return
     */

    public PID whois( String description ) {
        return findByDescription(description);
    }

    /**
     * Get a list of objects that describe the current open mailboxes.
     *
     * @return The list of MailBoxes
     */

    public List<QueueInfo> list() {
        List<QueueInfo> lQueues = new ArrayList<QueueInfo>();

        for ( PID pid: mQueues.keySet() ) {

            MsgQueue mq = getQueue(pid);

            if ( mq != null) {
                QueueInfo qi = new QueueInfo();
                qi.queuePid = pid;
                qi.queueSize = mq.size();
                qi.queueStats = mq.getStats();
                lQueues.add(qi);
            }
        }

        return lQueues;
    }




    /**
     * Adds a queue.
     * If a queue for the same PID alredy exists, the old queue
     * is discarded.
     *
     * This methid requires no synchronization as the map is synchonized.
     *
     * @param pid
     */

    public void addQueue(PID pid) {
        if (mQueues.containsKey(pid)) {
            mQueues.remove(pid);
        }

        mQueues.put(pid, new MsgQueue());
    }

    /**
     * Gets you a  the list of messages from a queue.
     * 
     * @param pid
     * @return
     */

    private MsgQueue getQueue( PID pid ) {
        
        if ( pid != null) {
            if ( mQueues.containsKey(pid)) {
                return mQueues.get(pid);
            }
        }
        return null;
    }

    /**
     * Drops a queue if it exists.
     * 
     * @param pid
     */

    public void removeQueue(PID pid) {
        if (mQueues.containsKey(pid)) {
            mQueues.remove(pid);
        }
    }

    public PID findByDescription( String description ) {
        
        PID res = null;

        // TOLGO synchronized ( mQueues ) {
        // non serve la sincronizzazione per gli oggetti "concurrent"
        //
        //    The view's returned iterator is a "weakly consistent" iterator
        //    that will never throw ConcurrentModificationException, and
        //    guarantees to traverse elements as they existed upon construction
        //    of the iterator, and may (but is not guaranteed to) reflect any
        //    modifications subsequent to construction.

        Iterator<PID> i = mQueues.keySet().iterator();
        while ( i.hasNext() ) {
            PID px = i.next();

            if ( description.equals( px.getDescr() ) ) {
                res = px;
                break;
            }
        }

        return res;
    }

    /**
     * Returns a message to the sender.
     * We make sure that there is a valid received.
     *
     * @param message
     */

    private void returnMsgUndeliverable(Msg message) {

        // Is there a valid receiver?
        PID sourcePid = message.getFromPid();

        if (sourcePid != null) {
            // we check that the source address is a valid queue
            MsgQueue q2 = getQueue(sourcePid);

            if (q2 != null) {
                Msg error = MsgErrUndeliverable.build(message.getFromPid(), message);
                q2.push(error);

            } else {
                logger.error("Cannot return message to non-existent mailbox {}", sourcePid);
            }
        } else {
            logger.error("Cannot return message with empty PID {}", message);
        }

    }

}

// 
//
