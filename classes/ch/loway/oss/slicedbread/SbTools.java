
package ch.loway.oss.slicedbread;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.common.MsgPleaseStop;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some static tools.
 *
 * 
 * @author l3nz
 * @since  1.0.0 - Nov 1, 2011
 */
public class SbTools {

    private static final Logger logger = LoggerFactory.getLogger(SbTools.class);
    
    /**
     * Turns an exception - actually a Throwable - into a String.
     * It stores the complete stack trace and inner exceptions as well.
     * Very handy for logging and storing it.
     *
     * @param ex The exception
     * @return A string representation
     */


    public static String stringifyException(Throwable ex) {
        StringBuilder stThrowable = new StringBuilder();
        stThrowable.append( "-- Inner Exception --\n" );

        if (ex != null) {
                stThrowable.append("Exception: ").append(ex.getClass().getName()).append("\n");
                String stMessage = ex.getMessage();

                if ( stMessage != null) {
                        stThrowable.append( "Error:\n" );
                        stThrowable.append( stMessage );
                }

                StringWriter out = new StringWriter();
                PrintWriter printOut = new PrintWriter(out);
                ex.printStackTrace(printOut);

                stThrowable.append( "\nStack trace:\n" );
                stThrowable.append(out.toString());
        }

        stThrowable.append( "-- End Inner Exception --\n");

        return stThrowable.toString();
    }


    /**
     * Funzione statica per fare la sleep di un thread.
     * Visto che molte classi che usano il framework la utilizzeranno,
     * tanto vale averla in un posto solo.
     * Mi evita di gestire l'eccezione - se la voglio gestire basta che guardo
     * lo stato risultante.
     *
     * @param ms Durata del sonno.
     * @return Se interrotta, ritorna false. la durata della pausa in questo caso
     *         non e' definita.
     *
     * \todo TRANSLATE
     */

    public static boolean sleep( int ms ) {
        try {
            Thread.sleep(ms);
            return true;
        } catch ( InterruptedException e ) {
            return false;
        }
    }

    /**
     * A simple way to block until a condition is met.
     */
    
    public static abstract class BlockUntil {

        /**
         * Waits until the specified condition is met.
         * 
         * @param maxWait in ms
         * @return true: ok - false: timeout
         */
        public boolean sync(long maxWait) {
            long beginning = System.currentTimeMillis();
            long took = 0;
            do {

                if (stopIfTrue()) {
                    return true;
                }

                SbTools.sleep(100);
                took = System.currentTimeMillis() - beginning;

            } while (took < maxWait);
            return false;
        }

        
        /**
         * Stop procesing if this function returns true.
         * 
         * @return true to stop (condition met); else false. 
         */
        public abstract boolean stopIfTrue();
    }

    /**
     * Waits for a mailbox to be existent or non-existent.
     * 
     * @param mailbox the name
     * @param maxTimeout max timeout
     * @param isUp true: up, false: downs
     * @return 
     */
    
    
    private static boolean awaitMailbox( final String mailbox, int maxTimeout, final boolean isUp ) {
        
        boolean result = new BlockUntil() {

            MessagingConsole mc = MessagingConsole.getConsole();
            
            @Override
            public boolean stopIfTrue() {
                return (( mc.findByDescription( mailbox ) != null) == isUp);
            }
            
        }.sync(maxTimeout);
        
        if ( !result  ) {
            logger.error( "Timed out waiting for mailbox '" + mailbox + "' "
                        + "to go" + (isUp ? "up": "down"));
        }
        
        return result;
    }
        
        
    public static boolean awaitMailboxUp( String mailbox, int maxTimeout ) {
        return awaitMailbox( mailbox, maxTimeout, true);
    }
    
    
    public static boolean awaitMailboxDown( String mailbox, int maxTimeout ) {
        return awaitMailbox( mailbox, maxTimeout, false);
    }
    
    /**
     * Shuts down a PID and waits for confirmation.
     * Uses a temporary mailbox that is deleted after some answer is received.
     * 
     * @param to the PID to shut down.
     * @return 
     */
    
    public static Msg shutdown( PID to ) {
        
        PID temp = null; 
        Msg msg = null;
        MessagingConsole mc = MessagingConsole.getConsole();
            
        try {
            
            temp = mc.registerExistingThread( "Shutdown/" + Math.random() );
            mc.send( MsgPleaseStop.build(temp, to));
            // waits for the answer.
            msg = mc.receive(temp, 5000);
        
            if ( msg == null ) {
                logger.error( "Thread " + to + " did not terminate within the timeout.");
            }
            
        } catch ( Throwable t ) {
            logger.error("While shutting down " + to, t);
            
        } finally {
            if ( temp != null ) {
                mc.removeQueue(temp);
            }
        }
        
        return msg;
    }
    

}

