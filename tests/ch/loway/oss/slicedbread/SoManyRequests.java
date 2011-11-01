
package ch.loway.oss.slicedbread;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.MsgError;
import ch.loway.oss.slicedbread.messages.common.MsgActionIds;
import ch.loway.oss.slicedbread.messages.common.MsgPleaseStop;

/**
 * Sends a lot of requests and expects just as many back.
 *
 * ======================================================================
 *
 * First revision, MessagingConsole version 1.2:
 *
 * Reqs:     8000 Threads:   1 Millis:2047    Req/sec:3908
 * Reqs:   800000 Threads:   1 Millis:11953   Req/sec:66928
 * Reqs:   800000 Threads:  40 Millis:12375   Req/sec:64646
 * Reqs:   800000 Threads: 100 Millis:12516   Req/sec:63918
 * Reqs: 80000000 Threads: 100 Millis:449095  Req/sec:178136
 *
 * The actual number of reqs processed is twice that.
 * JVM usage is about 20/25 M on Win32 and it closed with no problems.
 *
 * ======================================================================
 *
 *
 * @version  $Id: SoManyRequests.java,v 1.1 2010/04/10 16:37:08 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
 */
public class SoManyRequests extends TaskProcess {

    public static final int ACTION_MSG_A_IN  = 1000;
    public static final int ACTION_MSG_A_OUT = 1001;

    public static final int ACTION_MSG_B_IN  = 1002;
    public static final int ACTION_MSG_B_OUT = 1003;

    public static final int  ACTION_PRINT_MSG = 1004;

    int nMessages = 0;

    @Override
    public void run() {
        PID ownPid = getOwnPid();
        log (
            " Im here as "  + getProcessDescription()
            + " (son of " + getParentDescription() + ")"
        );
        MessagingConsole console = getConsole();

        try {
            for ( ;; ) {
                Msg m = console.receive(ownPid, 100);

                if ( m instanceof MsgPleaseStop ) {
                    log( "Stopping" );
                    return;
                } else
                if ( m instanceof MsgActionIds ) {
                    MsgActionIds ma = (MsgActionIds) m;

                    nMessages +=1;

                    switch ( ma.getCode1() ) {
                        case ACTION_MSG_A_IN:
                            console.send( MsgActionIds.build(getOwnPid(), ma.getFromPid(), ACTION_MSG_A_OUT, 0, "pp2"));
                            break;

                        case ACTION_MSG_B_IN:
                            console.send( MsgActionIds.build(getOwnPid(), ma.getFromPid(), ACTION_MSG_B_OUT, 0, "pp2"));
                            break;

                        case ACTION_PRINT_MSG:
                            log( "Messages processed: " + nMessages );
                            break;

                    }

                };
            }
        } catch ( InterruptedException e ) {
            return;
        }

    }


    public void log( String s ) {
        System.out.println( getOwnPid() + "| " + s);
    }


    final static int THREADS  = 100;
    final static int MSG_SENT_INNER = 400;
    final static int MSG_SENT_OUTER = 2000;

    static int msgA_sent = 0;
    static int msgA_recv = 0;

    static int msgB_sent = 0;
    static int msgB_recv = 0;


    public static void main( String[] argv ) {
        System.out.println( "Sending " + ( MSG_SENT_INNER * MSG_SENT_OUTER) + " reqs on " + THREADS + " threads");

        MessagingConsole console = MessagingConsole.getConsole();

        PID myself = console.registerExistingThread("Main");
        PID[] thr = new PID[THREADS+1];

        for ( int i=0; i < THREADS; i++ ) {
            thr[i] = console.register(myself, "T" + i, new SoManyRequests() );
        }

        printReceivedQueue(console, myself, 1000);

        long t0 = System.currentTimeMillis();

        for ( int outer = 0; outer < MSG_SENT_OUTER; outer++ ) {
            for ( int i =0; i < MSG_SENT_INNER; i++) {
                int nT = i % THREADS;
                console.send( MsgActionIds.build(myself, thr[nT], ACTION_MSG_A_IN, 0, "pp2"));
                msgA_sent += 1;
            }
            printReceivedQueue(console, myself, 50);
        }
        printReceivedQueue(console, myself, 1000);

        long t1 = System.currentTimeMillis() - t0;

        for ( int i=0; i < THREADS; i++ ) {
            console.send( MsgActionIds.build(myself, thr[i], ACTION_PRINT_MSG, 0, ""));
            console.send( MsgPleaseStop.build( myself, thr[i]) );
        }

        printReceivedQueue(console, myself, 1000);

        long totalReqs = (msgA_sent + msgB_sent);

        System.out.println( "Reqs: " + totalReqs
                         + " Threads: " + THREADS
                         + " Millis:" + t1
                         + " Req/sec:" + ((totalReqs * 1000) / t1)
        );

    }

    private static void printReceivedQueue(MessagingConsole console, PID myself, int timeout) {
        try {
            Msg m = null;
            do {
                m = console.receive(myself, timeout);
                if ( m != null) {
                    //System.out.println("< " + m.toString());

                    if ( m instanceof MsgError ) {
                        MsgError err = (MsgError) m;
                        System.out.println( SbTools.stringifyException( err.getCause()));
                    }

                    if ( m instanceof MsgActionIds) {
                        MsgActionIds ma = (MsgActionIds) m;

                        switch ( ma.getCode1() ) {
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

        System.out.println(
                "A sent:" + msgA_sent + " rec: " + msgA_recv
              + " - B sent:" + msgB_sent + " rec: " + msgB_recv
        );

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
