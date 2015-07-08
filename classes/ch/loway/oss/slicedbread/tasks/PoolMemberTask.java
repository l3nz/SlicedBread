package ch.loway.oss.slicedbread.tasks;

import ch.loway.oss.slicedbread.MessagingConsole;
import ch.loway.oss.slicedbread.TaskProcess;
import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.common.MsgPleaseStop;
import ch.loway.oss.slicedbread.messages.wd.MsgPoolRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This task is a pool thread.
 * 
 * @author lenz
 * @since 0.3
 */
public class PoolMemberTask extends TaskProcess {

    private final Logger logger = LoggerFactory.getLogger(PoolMemberTask.class);
    MessagingConsole console = MessagingConsole.getConsole();
    PID ownPid = null;
    PID poolPID = null;
    
    @Override
    public void run() {

        ownPid = getOwnPid();
        

        try {
            for ( ;; ) {
                
                // Is the ThreadPool telling us anything?
                Msg mP = console.receive(ownPid,0);
                if ( mP instanceof MsgPleaseStop ) {
                    logger.error("Thread gracefully stopping");
                    return;
                } else {
                    if ( mP != null ) {
                        logger.error("Unknown message from ThreadPool: " + mP.toString() );
                    }
                }
                
                // Are clients telling us anything?
                Msg m = console.receive(poolPID, 100);
                if ( m instanceof MsgPoolRunnable ) {
                    try {
                        processRunnable( (MsgPoolRunnable) m );
                    } catch ( Throwable t ) {
                        logger.error( "When procesing " + m, t);
                    }
                } else {
                    if ( m != null ) {
                        logger.error("Unknown message from clients: " + m.toString() );
                    }
                }
                
                
            }
        } catch ( InterruptedException e ) {
            return;
        }

    };

    /**
     * Processes a runnable task.
     * If there is a return message, it enqueues it.
     * 
     * @param r 
     */
    
    private void processRunnable( MsgPoolRunnable r ) {
        
        Msg m = r.task().process();
        if ( m != null ) {
            console.send(m);
        }
    }
    
    
   
    /**
     * Creates a task for a given pool.
     * 
     * @param poolPID the Pool's own PID
     * @return A brand-new task.
     */
    
    public static PoolMemberTask build( PID poolPID ) {
        PoolMemberTask pmt = new PoolMemberTask();
        pmt.poolPID = poolPID;
        return pmt;
    }
    
}
