package ch.loway.oss.slicedbread.containers;

import ch.loway.oss.slicedbread.messages.Msg;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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

    private final List<Msg> queue = new LinkedList<Msg>();

    /**
     * Queues a message.
     * @param m
     */
    public int push(Msg m) {
        synchronized (queue) {
            queue.add(m);
            return queue.size();
        }
    }

    /**
     * Pulls the oldest message from a queue.
     *
     * @return
     */
    public Msg pull() {
        Msg m = null;

        synchronized (queue) {
            if (queue.size() > 0) {
                m = queue.remove(0);
            }
        }
        return m;
    }

    /**
     * Gets all messages froma  queue and empties it.
     * 
     * @return
     */
    public List<Msg> fetchAllMessages() {

        List<Msg> lOut = Collections.EMPTY_LIST;

        synchronized (queue) {
            if (!queue.isEmpty()) {
                lOut = new ArrayList<Msg>(queue);
                queue.clear();
            }
        }

        return lOut;
    }

    /**
     * Gets the size of the mailbox (for debugging purpuses mostly).
     * 
     * @return
     */

    public int size() {
        int n = 0;
        synchronized ( queue ) {
            n = queue.size();
        }
        return n;
    }

}

