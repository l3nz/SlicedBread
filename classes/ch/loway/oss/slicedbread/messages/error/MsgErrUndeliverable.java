

package ch.loway.oss.slicedbread.messages.error;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.MsgError;

/**
 * MsgErrProcessDied
 *
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
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

