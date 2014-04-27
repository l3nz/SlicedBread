package ch.loway.oss.slicedbread;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.MsgError;
import ch.loway.oss.slicedbread.messages.common.MsgActionIds;
import ch.loway.oss.slicedbread.messages.common.MsgPleaseStop;
import ch.loway.oss.slicedbread.messages.Msg;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Sends a lot of requests and expects just as many back.
 *
 * ======================================================================
 *
 * Counting all messages sent and received:
 *
 * (LinkedList)
 * R/Snt: 8000000 R/Rcv:8000000 Threads: 100 Millis:5554 Req/sec:2880806
 * R/Snt: 8000000 R/Rcv:8000000 Threads:   2 Millis:4662 Req/sec:3432003
 *
 * (ArrayList)
 * R/Snt: 8000000 R/Rcv:8000000 Threads: 100 Millis:46652 Req/sec:342964
 *
 * JVM usage is about 20/25 M on Win32 and it closed with no problems.
 * ======================================================================
 *
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
 */
public class SoManyRequests {

    public static final int ACTION_MSG_A_IN = 1000;
    public static final int ACTION_MSG_A_OUT = 1001;
    public static final int ACTION_MSG_B_IN = 1002;
    public static final int ACTION_MSG_B_OUT = 1003;
    public static final int ACTION_PRINT_MSG = 1004;

    final static int THREADS = 100;
    final static int MSG_SENT_INNER = 4000;
    final static int MSG_SENT_OUTER = 2000;

    int msgA_sent = 0;
    int msgA_recv = 0;
    int msgB_sent = 0;
    int msgB_recv = 0;

    /**
     * How does this work?
     *
     * First we register as many threads as THREADS and store them in an array.
     *
     *
     *
     * @param argv
     */

    @Test
    public  void loadTest() {
        System.out.println("Sending " + (MSG_SENT_INNER * MSG_SENT_OUTER) + " reqs on " + THREADS + " threads");

        MessagingConsole console = MessagingConsole.getConsole();

        PID myself = console.registerExistingThread("Main");
        PID[] thr = new PID[THREADS + 1];

        for (int i = 0; i < THREADS; i++) {
            thr[i] = console.register(myself, "T" + i, new SoManyReqsProcess());
        }

        emptyReceivedQueue(console, myself, 1000);

        long t0 = System.currentTimeMillis();

        for (int outer = 0; outer < MSG_SENT_OUTER; outer++) {
            for (int i = 0; i < MSG_SENT_INNER; i++) {
                int nT = i % THREADS;
                console.send(MsgActionIds.build(myself, thr[nT], ACTION_MSG_A_IN, 0, "pp2"));
                msgA_sent += 1;
            }
            emptyReceivedQueue(console, myself, 0);
        }
        
        long t1 = System.currentTimeMillis() - t0;
        emptyReceivedQueue(console, myself, 500);

        for (int i = 0; i < THREADS; i++) {
            console.send(MsgActionIds.build(myself, thr[i], ACTION_PRINT_MSG, 0, ""));
            console.send(MsgPleaseStop.build(myself, thr[i]));
        }

        emptyReceivedQueue(console, myself, 1000);

        long totalReqsSent = (msgA_sent + msgB_sent);
        long totalReqsRcvd = (msgA_recv + msgB_recv);

        System.out.println("R/Snt: " + totalReqsSent + " R/Rcv:" + totalReqsRcvd
                + " Threads: " + THREADS
                + " Millis:" + t1
                + " Req/sec:" + (((totalReqsSent + totalReqsRcvd) * 1000) / t1));

        assertTrue(true);

    }

    /**
     * Aggiorna MsgA_recvd e MsgB_recvd
     *
     * @param console
     * @param myself
     * @param timeout
     */

    private void emptyReceivedQueue(MessagingConsole console, PID myself, int timeout) {
        try {
            Msg m = null;
            do {
                m = console.receive(myself, timeout);
                if (m != null) {
                    //System.out.println("< " + m.toString());

                    if (m instanceof MsgError) {
                        MsgError err = (MsgError) m;
                        System.out.println(SbTools.stringifyException(err.getCause()));
                    }

                    if (m instanceof MsgActionIds) {
                        MsgActionIds ma = (MsgActionIds) m;

                        switch (ma.getCode1()) {
                            case ACTION_MSG_A_OUT:
                                msgA_recv += 1;
                                break;

                            case ACTION_MSG_B_OUT:
                                msgB_recv += 1;
                                break;

                        }
                    }
                }
            } while (m != null);
        } catch (InterruptedException e) {
            System.out.println(SbTools.stringifyException(e));
        }
    }

    public static class SoManyReqsProcess extends TaskProcess {

        int nMessages = 0;

        @Override
        public void run() {
            
            log(
                    " Im here as " + getProcessDescription()
                    + " (son of " + getParentDescription() + ")");
            
            try {
                for (;;) {
                    Msg m = console.receive(ownPid, 100);

                    if (m instanceof MsgPleaseStop) {
                        log("Stopping");
                        return;
                    } else if (m instanceof MsgActionIds) {
                        MsgActionIds ma = (MsgActionIds) m;

                        nMessages += 1;

                        switch (ma.getCode1()) {
                            case ACTION_MSG_A_IN:
                                console.send(MsgActionIds.build(getOwnPid(), ma.getFromPid(), ACTION_MSG_A_OUT, 0, "pp2"));
                                break;

                            case ACTION_MSG_B_IN:
                                console.send(MsgActionIds.build(getOwnPid(), ma.getFromPid(), ACTION_MSG_B_OUT, 0, "pp2"));
                                break;

                            case ACTION_PRINT_MSG:
                                log("Messages processed: " + nMessages);
                                break;

                        }

                    }
                    
                }
            } catch (InterruptedException e) {
                return;
            }

        }

        public void log(String s) {
            System.out.println(getOwnPid() + "| " + s);
        }
    }
}
// $Log: SoManyRequests.java,v $
// Revision 1.1  2010/04/10 16:37:08  lenz-mobile
// First revision
//
// Revision 1.1  2010/04/10 15:42:22  lenz-mobile
// no message
//
//
//

