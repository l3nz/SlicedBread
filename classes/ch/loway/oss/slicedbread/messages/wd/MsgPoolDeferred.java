
package ch.loway.oss.slicedbread.messages.wd;

import ch.loway.oss.slicedbread.containers.PID;

/**
 * This message will be sent to the watchdog to be rescheduled
 * in a fixed time.
 * 
 * @author lenz
 */

public class MsgPoolDeferred extends MsgPoolRunnable {

    private long scheduleAfter = 0;
    private PID mailbox = null;
    
    public long getScheduleAfter() {
        return scheduleAfter;
    }
    
    public void rescheduleIn( long timeout ) {
        scheduleAfter = System.currentTimeMillis() + timeout;
    }
    
    public PID getPool() {
        return mailbox;
    }
    
    public void setPool( PID poolPid ) {
        mailbox = poolPid;
    }
    
    public static MsgPoolDeferred build( PID fromPid, PID toPid, PID pool, long afterMs, RTask task ) {
        final MsgPoolDeferred d = new MsgPoolDeferred();
        d.setRecipients(fromPid, toPid);
        d.setPool(pool);
        d.setRTask(task);
        d.rescheduleIn(afterMs);
        return d;
    }
    
    
}

