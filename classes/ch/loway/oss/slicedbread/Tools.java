
package ch.loway.oss.slicedbread;

/**
 * Some static tools.
 *
 * 
 * @author l3nz
 * @since  1.0.0 - Nov 1, 2011
 */
public class Tools {

    public static String stringifyException( Throwable t ) {
        return "TBD";
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

