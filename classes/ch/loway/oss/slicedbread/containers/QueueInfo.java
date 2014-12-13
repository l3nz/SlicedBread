
package ch.loway.oss.slicedbread.containers;

import ch.loway.oss.slicedbread.timer.LogBin;
import java.util.List;

/**
 * This class is used to "list" the state of queues.
 * 
 * @author lenz
 * @since  0.1.0
 */
public class QueueInfo {

    public PID queuePid = null;
    public int queueSize = 0;
    public List<LogBin> queueStats = null;

    @Override
    public String toString() {
        return queuePid + " Size: " + queueSize + "\n" 
                + LogBin.printAsText(queueStats) + "\n";
    }

}

// $Log$
//
