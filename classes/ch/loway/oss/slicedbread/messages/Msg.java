
package ch.loway.oss.slicedbread.messages;

import ch.loway.oss.slicedbread.containers.PID;

/**
 * Msg
 *
 * This is our base messaging object, thatis ancestor for every message.
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
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
     * @return the sender Pid
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
     * Use only for debug - the builder is pretty unefficient.
     * 
     * @return a serialized view.
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

// 
//
