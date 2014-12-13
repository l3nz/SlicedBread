package ch.loway.oss.slicedbread.containers;

import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.timer.LogBin;
import ch.loway.oss.slicedbread.timer.LogTimer;
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

    private final List<MsgWrapper> queue = new LinkedList<MsgWrapper>();
    private final LogTimer statsCollector = LogTimer.build(16000);
    
    /**
     * Queues a message.
     * @param m
     */
    public int push(Msg m) {
        
        MsgWrapper wrapped = MsgWrapper.build(m);
        synchronized (queue) {
            queue.add(wrapped);
            return queue.size();
        }
    }

    /**
     * Pulls the oldest message from a queue.
     *
     * @return
     */
    public Msg pull() {
        MsgWrapper m = null;

        synchronized (queue) {
            if (queue.size() > 0) {
                m = queue.remove(0);
            }
        }
        
        return unwrap(m);
    }

    
    private Msg unwrap( MsgWrapper wrapper ) {
        if ( wrapper == null ) {
            return null;
        }
        
        long time = System.currentTimeMillis() - wrapper.queuedAt;
        
        synchronized ( statsCollector ) {
            statsCollector.add( (int) time );
        }

        return wrapper.msg;
    }
    
    
    /**
     * Gets all messages from a queue and empties it.
     * 
     * @return
     */
    public List<Msg> fetchAllMessages() {

        List<MsgWrapper> lOutWrap = Collections.EMPTY_LIST;
        List<Msg> lOut = Collections.EMPTY_LIST;

        synchronized (queue) {
            if (!queue.isEmpty()) {
                lOutWrap = new ArrayList<MsgWrapper>(queue);
                queue.clear();
            }
        }

        if (lOutWrap.size() > 0) {
            lOut = new ArrayList<>(lOutWrap.size());
            for (MsgWrapper wr : lOutWrap) {
                lOut.add(unwrap(wr));
            }
        }

        return lOut;
    }

    /**
     * Gets the size of the mailbox (for debugging purpouses mostly).
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
    
    /**
     * Returns statistics for this mailbox.
     * 
     * @return 
     */
    
    public List<LogBin> getStats() {
        synchronized ( statsCollector ) {
            return statsCollector.results();
        }
    }

    
    /**
     * Wraps a message so we can measure time in queue.
     * 
     */
    
    private static class MsgWrapper {
        long queuedAt =0;
        Msg msg = null;
        
        public static MsgWrapper build( Msg msg ) {
            MsgWrapper m = new MsgWrapper();
            m.queuedAt = System.currentTimeMillis();
            m.msg = msg;
            return m;
        }
    } 
    
    
}

