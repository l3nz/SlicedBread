
package ch.loway.oss.sbDemos.helloWorld;

import ch.loway.oss.slicedbread.MessagingConsole;
import ch.loway.oss.slicedbread.SbTools;
import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.common.MsgPleaseStop;

/**
 * This class is an "Hello world" example in SlicedBread.
 * Its aim is to start a couple of threads, ask them to print a string
 * and then terminate them.
 *
 *
 * @author l3nz
 */
public class HelloWorld {

    public static void main( String args[] ) {
        System.out.println( "Hello world starting" );

        MessagingConsole console = MessagingConsole.getConsole();
        PID myself = console.registerExistingThread("Main");

        // create the "slave" threads
        PID thread_A = console.register(myself, "Thread A", new PrinterTask() );
        PID thread_B = console.register(myself, "Thread B", new PrinterTask() );

        // send a message to threads A and B
        console.send( MsgPrint.build(myself, thread_A, "Hello"));
        console.send( MsgPrint.build(myself, thread_B, "World"));

        // then we ask both to terminate
        console.send( MsgPleaseStop.build(myself, thread_A));
        console.send( MsgPleaseStop.build(myself, thread_B));

        // then we sleep for a while and check our e-mail
        SbTools.sleep(100);

        try {
            for ( ;; ) {
                Msg msgReceived = console.receive(myself, 10);

                if ( msgReceived == null ) {
                    break;
                }

                System.out.println( "Message found: " +  msgReceived );
            }
        } catch ( InterruptedException e ) {
            System.out.println( "Thread was interrupted.");
        }

    }



}

// $Log$
//
