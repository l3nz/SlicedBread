
package ch.loway.oss.slicedbread.tasks;

import ch.loway.oss.slicedbread.MessagingConsole;
import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.common.MsgPleaseStop;
import ch.loway.oss.slicedbread.messages.common.MsgProcessEnded;
import ch.loway.oss.slicedbread.messages.common.MsgProcessStarted;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages a thread pool.
 * 
 * A thread pool has the following mailboxes:
 *  - Pool/name - the mailbox used to enqueue mesages
 *  - Pool/name/CTRL  - the control mailbox, used by the threads to notify the 
 *    pool manager and by SB to send mabk events (errors, closed thrtreads, etc)
 * - Pool/name/1...  - each thread's private mailbox
 * 
 * This class is not run as a thread itself, but will be multiplexed on the
 * watchdong thread by calling its non-blocking processMessages() that allows it 
 * to proces the /CTRL mailbox. 
 * This lets it react to events (eg closed threads) and manage pool size. 
 * 
 * 
 * @author lenz
 * @since 0.3
 */
public class ThreadPool {

    private final static Logger logger = LoggerFactory.getLogger(ThreadPool.class);
    private final static String POOL_PREFIX = "SbPool/";
    private final static String POOL_CTRL_SUFFIX = "/CTRL";
    
    
    MessagingConsole console = MessagingConsole.getConsole();
    String name = "";
    int    minChannels = 0;
    int    maxChannels = 0;
    PID    poolPID     = null;
    PID    fromPID     = null;
    Map<PID,ThreadState> threads   = new HashMap<PID,ThreadState>();
    int    currThreadNo = 0;
    
    public void create( String name, int min, int max ) {
        
        this.name = name;
        this.minChannels = min;
        this.maxChannels = max;
        
        this.poolPID = PID.getNextPid( getPoolName(name) );
        console.addQueue(poolPID);

        this.fromPID = PID.getNextPid( getPoolCtrlName(name) );
        console.addQueue(fromPID);

        for ( int i=0; i < minChannels; i++ ) {
            PID newThread = createNewThread();
            threads.put( newThread, ThreadState.STARTING );
        }
        
    }

    public void processMessages() {
        
        // Maybe the thread is already closed.
        if ( fromPID == null ) {
            return;
        }
        
        
        List<Msg> msgs = console.receiveAll(fromPID);
        
        for ( Msg m: msgs ) {
            if ( m instanceof MsgProcessStarted ) {
                updateThreadState( m.getFromPid(), ThreadState.RUNNING );
            } else
            if ( m instanceof MsgProcessEnded ) {
                
                if ( threadState( m.getFromPid()) == ThreadState.STOPPING ) {           
                    threads.remove( m.getFromPid() );
                    
                    if ( threads.isEmpty() ) {

                        console.removeQueue(fromPID);
                        console.removeQueue(poolPID);
                        fromPID = null;
                        poolPID = null;
        
                        logger.warn("Thread pool " + name + " removed");
        
                    }
                    
                    
                } else {
                    logger.error("Unknown stopped ThreadState");
                }
                
            } else {
                logger.error("Unknown message " + m.toString() );
            }
        }
    }
    
    /**
     *  Close down everything.
     */
    
    public void shutdown() {
        
        if ( fromPID == null ) {
            logger.error("Thread pool " + name + " is already being shut down.");
            return;
        }
        
        
        Set<PID> t = threads.keySet();
        for (PID p : t) {
            ThreadState st = threadState(p);
            if (st != ThreadState.STOPPING) {
                console.send(MsgPleaseStop.build(fromPID, p));
                updateThreadState(p, ThreadState.STOPPING);
            }
        }
        
    }

    /***
     * Creates a new worker thread in this pool.
     * 
     * @return the PID for the new thread.
     */
    private PID createNewThread() {
        String threadname = getNewThreadName();
        
        PID newThread = console.register(fromPID, threadname, PoolMemberTask.build(poolPID) );
        return newThread;
    }
    
    /**
     * Gets a new unique thread name.
     * 
     * @return the name, like Pool/name/123
     */
    
    private String getNewThreadName() {
        currThreadNo += 1;
        String n = getPoolName(name) + "/" + currThreadNo;
        return n;
    }
    
    /**
     * Updated the thread state.
     * If the thread is unknowns it is ignoored.
     * 
     * @param pid our thread
     * @param newState 
     */
    
    private void updateThreadState( PID pid, ThreadState newState ) {
        if (threads.containsKey(pid) ) {
            threads.put(pid, newState);
        }
    }
    
    /**
     * Gets the thread state.
     * 
     * @param pid
     * @return the current state, or UNKNOWN
     */
    
    private ThreadState threadState( PID pid ) {
        if (threads.containsKey(pid) ) {
            return threads.get(pid);
        } else {
            return ThreadState.UNKNOWN;
        }
    }
    
    /**
     * Finds (by name) the mailbox for a pool.
     * 
     * @param name
     * @return the name, like Pool/abcd
     */
    
    public static String getPoolName( String name ) {
        return POOL_PREFIX + name; 
    }
    
    /**
     * Gets the controller name.
     * 
     * @param name
     * @return the name, like Pool/abcd/CTRL
     */
    
    
    public static String getPoolCtrlName( String name ) {
        return getPoolName(name) + POOL_CTRL_SUFFIX; 
    }
    
    /**
     * Return the PID for a pool.
     * 
     * @param name
     * @return the PID if found, or null.
     */
    
    public static PID getPoolPid( String name ) {
        return MessagingConsole.getConsole().findByDescription( getPoolName(name) );
    }
    
    /**
     * Blocks until there is a PID for a pool and returns it.
     * 
     * @param name
     * @param maxWait
     * @return the PID if found, or null if not found until it times out.
     */
    
    public static PID getPoolPid_blocking( String name, long maxWait ) {
        return MessagingConsole.getConsole().blockingFindByDescription(getPoolName(name), maxWait  );
    }
    
    
    /**
     * Possible states for a thread
     */
    public static enum ThreadState {
        UNKNOWN,   /** No known thread */
        STARTING,  /** Thread started, not confirmed yet */
        RUNNING,  /** Thread is running */
        STOPPING; /** Thread is shutting down */
    }
}


