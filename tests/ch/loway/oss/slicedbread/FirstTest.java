package ch.loway.oss.slicedbread;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.MsgError;
import ch.loway.oss.slicedbread.messages.Msg_Print;
import ch.loway.oss.slicedbread.messages.common.MsgActionIds;
import ch.loway.oss.slicedbread.messages.common.MsgPleaseStop;

import org.junit.Test;


/**
 * FirstTest
 *
 *
 * @version  $Id: FirstTest.java,v 1.3 2010/05/15 08:30:20 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
 */
public class FirstTest extends TaskProcess {

    public static final int ACTION_RAISE_EXCEPTION = 1000;
    public static final int ACTION_PRINT_PRO2      = 1001;



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
                if ( m instanceof Msg_Print ) {
                    Msg_Print mp = (Msg_Print) m;
                    log( "PRT:" + mp.getToBePrinted() );
                } else
                if ( m instanceof MsgActionIds ) {
                    MsgActionIds ma = (MsgActionIds) m;

                    switch ( ma.getCode1() ) {
                        case ACTION_RAISE_EXCEPTION:
                            log( "RAiseExcc.");
                            int b = 1 - 1;
                            int c = 1 / b;
                            break;

                        case ACTION_PRINT_PRO2:
                            log("Contactiong pro2");
                            PID p2 = console.findByDescription("Pro2");
                            log( "Pro2 is: " + p2 );
                            console.send( Msg_Print.build(getOwnPid(), p2, "To p2 by name") );
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




    public static void main( String[] argv ) {
        System.out.println( "Hello there!");

        MessagingConsole console = MessagingConsole.getConsole();

        PID myself = console.registerExistingThread("Main");

        PID p1 = console.register(myself, "Pro1", new FirstTest() );
        PID p2 = console.register(myself, "Pro2", new FirstTest() );
        PID p3 = console.register(myself, "Pro3", new FirstTest() );
        PID p4 = console.register(myself, "Pro4", new FirstTest() );
        printReceivedQueue(console, myself);

        console.send( Msg_Print.build(myself, p1, "To p1") );
        console.send( Msg_Print.build(myself, p2, "To p2") );
        printReceivedQueue(console, myself);

        for ( int i = 0; i < 10; i++ ) {
            console.send( Msg_Print.build(myself, p1, "To p1 - #" + i) );            
        }

        printReceivedQueue(console, myself);

        console.send( MsgActionIds.build(myself, p3, ACTION_PRINT_PRO2, 0, "pp2"));
        console.send( MsgActionIds.build(myself, p4, ACTION_RAISE_EXCEPTION, 0, "xc"));
        printReceivedQueue(console, myself);

        console.send( MsgPleaseStop.build( myself, p1) );
        console.send( MsgPleaseStop.build( myself, p2) );
        console.send( MsgPleaseStop.build( myself, p3) );
        console.send( MsgPleaseStop.build( myself, p4) );
        printReceivedQueue(console, myself);

    }

    private static void printReceivedQueue(MessagingConsole console, PID myself) {
        try {
            Msg m = null;
            do {
                m = console.receive(myself, 500);
                if ( m != null) {
                    System.out.println("< " + m.toString());

                    if ( m instanceof MsgError ) {
                        MsgError err = (MsgError) m;
                        System.out.println( Tools.stringifyException( err.getCause()));
                    }

                }
            } while (m != null);
        } catch (InterruptedException e) {
            System.out.println(Tools.stringifyException(e));
        }
    }


    @Test
    public void IAmNotReallyATest() {
        assert(true);
    }

}

// $Log: FirstTest.java,v $
// Revision 1.3  2010/05/15 08:30:20  lenz-mobile
// FunzionaSenzaErrori
//
// Revision 1.2  2010/05/10 15:57:34  lenz-mobile
// Prima versione sotto Wombat
//
// Revision 1.1  2010/04/10 15:42:22  lenz-mobile
// no message
//
//
//
