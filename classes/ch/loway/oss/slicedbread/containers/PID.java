/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.slicedbread.containers;

/**
 * PID
 *
 * This is a wrapper for the process-ID, the unique integer that identifies
 * message tasks.
 *
 * @version  $Id: PID.java,v 1.1 2010/04/10 15:43:25 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
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
        return  descr + "#" + pid;
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
     * @return
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
     * @return
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
     * @return
     */

    @Override
    public int hashCode() {
        return pid;
    }






    // \todo Metodo Compare e Hash - vedi Effective Java
    // \todo rendere final la classe?
}

// $Log: PID.java,v $
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
