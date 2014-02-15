package ch.loway.oss.slicedbread.containers;
import ch.loway.oss.slicedbread.messages.Msg;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements a message queue (mailbox).
 * All access is synchronized.
 * Queues are FIFO objects.
 *
 * @author lenz
 * @since Feb 15, 2014
 */
public class MsgQueue {

    private final List<Msg> queue = new ArrayList<Msg>();

    /**
     * Queues a message.
     * @param m
     */

    public synchronized int push(Msg m) {
        queue.add(m);
        return queue.size();
    }

   /**
    * Pulls the oldest message from a queue.
    * 
    * @return
    */
    public synchronized Msg pull() {
        Msg m = null;

        if (queue.size() > 0) {
            m = queue.remove(0);
        }
        return m;
    }

    /**
     * Gets all messages froma  queue and empties it.
     * 
     * @return
     */
    public synchronized List<Msg> fetchAllMessages() {

        List<Msg> lOut = Collections.EMPTY_LIST;

        if (!queue.isEmpty()) {
            lOut = new ArrayList<Msg>(queue);
            queue.clear();
        }

        return lOut;
    }
}



