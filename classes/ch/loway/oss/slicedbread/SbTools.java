
package ch.loway.oss.slicedbread;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Some static tools.
 *
 * 
 * @author l3nz
 * @since  1.0.0 - Nov 1, 2011
 */
public class SbTools {

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


}

