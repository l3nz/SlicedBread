
package ch.loway.oss.slicedbread.containers;

/**
 * This class is used to "list" the state of queues.
 * 
 * @author lenz
 * @since  0.1.0
 */
public class QueueInfo {

    public PID queuePid = null;
    public int queueSize = 0;

    @Override
    public String toString() {
        return queuePid + " Size: " + queueSize;
    }

}

// $Log$
//
