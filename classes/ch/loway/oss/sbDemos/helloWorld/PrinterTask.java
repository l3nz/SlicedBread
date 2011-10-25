
package ch.loway.oss.sbDemos.helloWorld;

import ch.loway.oss.slicedbread.MessagingConsole;
import ch.loway.oss.slicedbread.TaskProcess;
import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.common.MsgPleaseStop;

/**
 * This is an example task.
 * 
 * @author l3nz
 */
public class PrinterTask extends TaskProcess {

    @Override
    public void run() {

        PID ownPid = getOwnPid();
        log (
            "I am thread "  + getProcessDescription()
            + " (created by " + getParentDescription() + ")"
        );
        MessagingConsole console = getConsole();

        try {
            for ( ;; ) {
                Msg m = console.receive(ownPid, 100);

                if ( m instanceof MsgPleaseStop ) {
                    log( "Now Stopping" );
                    return;
                } else
                if ( m instanceof MsgPrint ) {
                    processMessage( (MsgPrint) m );
                };
            }
        } catch ( InterruptedException e ) {
            return;
        }

    };


    private void processMessage( MsgPrint m ) {
        log( "PRINTING: " + m.getText() );
    }


    private void log( String s ) {
        System.out.println( "(" + getOwnPid().toString() + ") " + s );
    }


}

// $Log$
//
