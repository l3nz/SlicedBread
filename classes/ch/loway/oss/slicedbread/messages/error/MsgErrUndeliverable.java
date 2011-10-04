/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.slicedbread.messages.error;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.MsgError;

/**
 * MsgErrProcessDied
 *
 *
 * @version  $Id: MsgErrUndeliverable.java,v 1.2 2010/05/14 17:21:59 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
 */
public class MsgErrUndeliverable extends MsgError {

    private Msg originalMessage = null;

    public void setOriginalMessage(Msg originalMessage) {
        this.originalMessage = originalMessage;
    }

    public Msg getOriginalMessage() {
        return originalMessage;
    }

    
    @Override
    public String toString() {

        String orgMsg = "**null**";
        if ( originalMessage != null ) {
            orgMsg = originalMessage.toString();
        }

        return super.toString() + " - Undeliverable to " + originalMessage.getToPid()
                + "(was: " + orgMsg + ")"  ;
    }

    // METODO factory

    public static MsgErrUndeliverable build(  PID to, Msg source ) {
        MsgErrUndeliverable m = new MsgErrUndeliverable();
        m.setFromPid( PID.systemPid() );
        m.setToPid(to);
        m.setOriginalMessage(source);
        return m;
    }


}

// $Log: MsgErrUndeliverable.java,v $
// Revision 1.2  2010/05/14 17:21:59  lenz-mobile
// Mostra messaggio originale
//
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
