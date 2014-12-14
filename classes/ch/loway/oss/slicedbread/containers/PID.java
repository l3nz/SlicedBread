
package ch.loway.oss.slicedbread.containers;

/**
 * PID
 *
 * This is a wrapper for the process-ID, the unique integer that identifies
 * message tasks.
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
 */
public class PID {

    private static int nextPid = 0;

    private PID() {
        // no public constructor
        // use the static build method
    }

    private  int pid = 0;
    private  String descr = "";


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append( descr )
           .append( '#' )
           .append( pid );

        return  sb.toString();
    }

    // gettr e setter
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getDescr() {
        return descr;
    }




    /**
     * Metodo builder.
     * 
     * @param myPid
     * @return Oggetto PID
     */

    public static PID build( int myPid, String description ) {
        PID p = new PID();
        p.setPid(myPid);
        p.setDescr(description);
        return p;
    }

    /**
     * Ottiene un nuovo PID unico.
     * 
     * @return the new PID.
     */

    public static synchronized PID getNextPid( String description) {

        nextPid += 1;
        return build(nextPid, description);

    }

    public static PID systemPid() {
        return PID.build(0, "SYSTEM");
    }

    /**
     * \todo vedi Effective Java
     * @param obj
     * @return equality
     */

    @Override
    public boolean equals(Object obj) {

        if ( obj instanceof PID ) {
            PID pObj = (PID) obj;
            return ( pObj.getPid() == this.getPid() );
        } else {
            return false;
        }

    }

    /**
     * Per l'hashCode, uso il PID corrente
     * \todo vedi Effective Java
     *
     * @return hashcode
     */

    @Override
    public int hashCode() {
        return pid;
    }

    // \todo Metodo Compare e Hash - vedi Effective Java
    // \todo rendere final la classe?
}

