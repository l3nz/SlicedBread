
package ch.loway.oss.slicedbread.messages.wd;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;

/**
 * This is a pool-runnable getRTask.
 * 
 * \todo use a better interface so the RTask can know it 
 *       all about its environment.
 * 
 * @author lenz
 * @since 0.3
 */
public class MsgPoolRunnable extends Msg {

    RTask task = null;
    
    /**
     * This interface is to be implemented by the pool tasks.
     */
    public static interface RTask {
        /**
         * This method shall clean up its own mess.
         * Do NOT leave open database connections, resources or anything.
         * 
         * @return a message to be enqueued, or null if not needed.
         */
        public Msg process();
    }
    
    /**
     * Builds an instance of the object.
     * 
     * @param from
     * @param to
     * @param task
     * @return a runnable message wrapping the RTask object.
     */
    
    public static MsgPoolRunnable build( PID from, PID to, RTask task ) {
        MsgPoolRunnable mpr = new MsgPoolRunnable();
        mpr.setRecipients(from, to);
        mpr.task = task;
        return mpr;
    } 
    
    /**
     * Builds a runnable getRTask out of an expired deferred getRTask.
     * 
     * @param m
     * @return 
     */
    
    public static MsgPoolRunnable build( MsgPoolDeferred m ) {
        return build(m.getFromPid(), m.getPool(), m.getRTask() );
    }
    
    /**
     * Obtains the RTask.
     * \todo 
     * 
     * @return the current getRTask. 
     */
    
    public RTask getRTask() {
        return task;
    }
    
    public void setRTask( RTask t) {
        task = t;
    }
    
}


