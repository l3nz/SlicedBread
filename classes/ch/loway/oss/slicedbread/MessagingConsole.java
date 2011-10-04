
package ch.loway.oss.slicedbread;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.common.MsgProcessEnded;
import ch.loway.oss.slicedbread.messages.common.MsgProcessStarted;
import ch.loway.oss.slicedbread.messages.error.MsgErrProcessDied;
import ch.loway.oss.slicedbread.messages.error.MsgErrUndeliverable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;


/**
 * MessagingConsole
 *
 * This object is the singleton console for all MsgTasking needs.
 * This is the only interactions users have with the MsgTasking world.
 *
 * @version  $Id: MessagingConsole.java,v 1.11 2011/05/27 08:56:41 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
 */
public class MessagingConsole {

    private static final MessagingConsole me = new MessagingConsole();

    static Logger logger = Logger.getLogger("Messaging");

    public static MessagingConsole getConsole() {
        return me;
    }

    /**
     * Elimina mapping precedenti.
     * 
     */

    public void reset() {
        System.out.println( "=========== RESET CONSOLE ================");
        mQueues.clear();
    }



    /**
     * Queues the message for delivery.
     *
     * @param message
     * @return The nummber of messages on the queue it's been delivered to.
     */
    public int send( Msg message ) {

        //System.out.println( "CX:Sending:" + message );

        if ( message == null ) {
            return 0;
        }

        logger.trace("S:" + message);

        List<Msg> q = getQueue( message.getToPid() );

        if ( q == null ) { // if the destination address was not found...

            // we check that the source address is a valid queue
            List<Msg> q2 = getQueue( message.getFromPid());
            if ( q2 != null ) {
                send( MsgErrUndeliverable.build( message.getFromPid(), message) );
            }
            return 0;
            
        }

        return queueMessage( q, message );

    }

    public final int POLL_EVERY_MS = 10;
    public Msg receive( PID myPid, int timeout ) throws InterruptedException {

        //System.out.println( "CX:recv:" + myPid);

        int timeRemaining = timeout;

        List<Msg> q = getQueue( myPid );
        if ( q == null ) {
            return null;
        }

        do {
            Msg message = fetchMessage(q);
            if ( message == null ) {

                if ( timeRemaining > 0 ) {
                    Thread.sleep( POLL_EVERY_MS );
                    timeRemaining = timeRemaining - POLL_EVERY_MS;
                }

            } else {
                // I found a message at last
                //System.out.println( "CX:recv:" + myPid + " " + message);
                logger.trace("R:" + message);
                return message;
            }

        } while ( timeRemaining > 0 );

        // no message was found
        //System.out.println( "CX:recv:" + myPid + " --- ");
        return null;
    }

//    public Msg receive( PID fromPid, long timeout ) {
//        return null;
//    }

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
     * Dato il nome univoco, crea una nuova mailbox o la registra.
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

                    String ex = Tools.stringifyException(t);
                    System.out.print(ex);

                    send( MsgErrProcessDied.build( processPid, callerPid, t ) );
                    
                } finally {

                    // \todo mando indietro i messaggi pendenti come "rejected"
                    removeQueue(processPid);
                }
            }
        };

        new Thread(task, description).start();

        return processPid;

    }

    /**
     * Cerca un PID dalla descrizione.
     * Se non c'è, ritorna null.
     * 
     * @param description
     * @return
     */

    public PID whois( String description ) {
        return findByDescription(description);
    }

    // ====================================================================
    // HashMap delle queues

    private final Map<PID,List<Msg>> mQueues = new ConcurrentHashMap<PID,List<Msg>>();

    public void addQueue( PID pid ) {
        //synchronized ( mQueues ) {
            if ( mQueues.containsKey(pid)) {
                mQueues.remove(pid);
            }

            mQueues.put(pid, new LinkedList<Msg>() );
        //}
    }

    public List<Msg> getQueue( PID pid ) {
        
        if ( pid != null) {
            if ( mQueues.containsKey(pid)) {
                return mQueues.get(pid);
            }
        }
        return null;
    }

    public void removeQueue( PID pid ) {
        //synchronized ( mQueues ) {
            if ( mQueues.containsKey(pid)) {
                mQueues.remove(pid);
            }
        //}
    }

    public PID findByDescription( String description ) {
        
        PID res = null;

        // TOLGO synchronized ( mQueues ) {
        // non serve la sincronizzazione per gli oggetti "conbcurrent"
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

    // ====================================================================
    // gestione delle queues

    public int queueMessage( List<Msg> queue, Msg m ) {

        int i = 0;
        synchronized ( queue ) {
            queue.add(m);
            i = queue.size();
        }
        return i;

    }

    /**
     * Se non ci sono messaggi, ritorna null.
     *
     * @param queue
     * @return
     */
    public Msg fetchMessage( List<Msg> queue ) {
        Msg m = null;
        synchronized ( queue ) {
            if ( queue.size() > 0 ) {
                m = queue.remove(0);
            }
        }
        return m;
    }

    /**
     * Se non ci sono messaggi, ritorna null.
     *
     * @param queue
     * @return
     */
    public Msg fetchMessageFrom( List<Msg> queue, PID from ) {
        Msg m = null;
        synchronized ( queue ) {

            for ( int i = 0; i < queue.size(); i++) {
                m = queue.get(i);
                if ( from.equals( m.getFromPid() ) ) {
                    queue.remove(i);
                    break;
                }
            }
        }
        return m;
    }



}

// $Log: MessagingConsole.java,v $
// Revision 1.11  2011/05/27 08:56:41  lenz-mobile
// Riconnessione di thread
//
// Revision 1.10  2011/05/12 08:23:37  lenz-mobile
// Eliminazione PID dalla lista code anche quando un processo si chiude a buon fine.
//
// Revision 1.9  2010/08/04 16:02:59  lenz-mobile
// Controlla chiamate attive.... non va
//
// Revision 1.8  2010/07/23 17:28:37  lenz-mobile
// Stampa strack trace
//
// Revision 1.7  2010/05/17 14:37:06  lenz-mobile
// FindBugs
//
// Revision 1.6  2010/05/14 17:41:00  lenz-mobile
// Reset dei messaggi
//
// Revision 1.5  2010/05/14 15:19:00  lenz-mobile
// Prevede NullPointer
//
// Revision 1.4  2010/05/09 16:19:21  lenz-mobile
// Aggiunto metodo statico per la Sleep()
//
// Revision 1.3  2010/04/11 11:29:05  lenz-mobile
// Removed Map Locking by using ConcurrentHashMap.
// Threads have names now.
//
// Revision 1.2  2010/04/10 15:48:14  lenz-mobile
// Removed -Xlint:unchecked warnings
//
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
