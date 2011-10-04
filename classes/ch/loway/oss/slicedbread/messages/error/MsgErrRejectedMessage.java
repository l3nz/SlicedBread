/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.slicedbread.messages.error;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.MsgError;

/**
 * MsgErrProcessDead
 *
 *
 * @version  $Id: MsgErrRejectedMessage.java,v 1.1 2010/04/10 15:43:25 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
 */
public class MsgErrRejectedMessage extends MsgError {

    private PID deadProcessId = null;
    private Msg message   = null;

    public PID getDeadProcessId() {
        return deadProcessId;
    }

    public void setDeadProcessId(PID deadProcessId) {
        this.deadProcessId = deadProcessId;
    }

    public void setMessage(Msg message) {
        this.message = message;
    }

    public Msg getMessage() {
        return message;
    }



    @Override
    public String toString() {
        return super.toString() 
                + " - RejectedMessage " + deadProcessId
                + " [" + message + "]" ;
    }


}

// $Log: MsgErrRejectedMessage.java,v $
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
