/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.slicedbread.messages;

import ch.loway.oss.slicedbread.containers.PID;

/**
 * Msg
 *
 * Questo è il messaggio di base, da cui tutti ereditano.
 *
 * @version  $Id: Msg.java,v 1.2 2010/04/14 16:37:15 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
 */
public class Msg {

    private PID  fromPid   = null;
    private PID  toPid     = null;
    private long sentOn    = 0L;
    private int  replyTo   = 0;

    // ======================================================
    //        m e t o d i     d i     a c c e s s o
    // ======================================================


    /**
     * @return the fromPid
     */
    public PID getFromPid() {
        return fromPid;
    }

    /**
     * @param fromPid the fromPid to set
     */
    public void setFromPid(PID fromPid) {
        this.fromPid = fromPid;
    }

    /**
     * @return the toPid
     */
    public PID getToPid() {
        return toPid;
    }

    /**
     * @param toPid the toPid to set
     */
    public void setToPid(PID toPid) {
        this.toPid = toPid;
    }

    /**
     * @return the sentOn
     */
    public long getSentOn() {
        return sentOn;
    }

    /**
     * @param sentOn the sentOn to set
     */
    public void setSentOn(long sentOn) {
        this.sentOn = sentOn;
    }

    /**
     * @return the replyTo
     */
    public int getReplyTo() {
        return replyTo;
    }

    /**
     * @param replyTo the replyTo to set
     */
    public void setReplyTo(int replyTo) {
        this.replyTo = replyTo;
    }



    // metodi comuni

    /**
     *
     * @return
     */

    @Override
    public String toString() {
        return "F:" + fromPid
                + " T:" + toPid
                + ( replyTo > 0 ? " R: " + replyTo : "");
    }


    /**
     * Imposta i recipients.
     * 
     * @param from
     * @param to
     */
    public void setRecipients( PID from, PID to) {
        setFromPid(from);
        setToPid(to);
    }

}

// $Log: Msg.java,v $
// Revision 1.2  2010/04/14 16:37:15  lenz-mobile
// Imposta recipients
//
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
