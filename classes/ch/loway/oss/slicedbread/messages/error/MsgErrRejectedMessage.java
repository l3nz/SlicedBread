

package ch.loway.oss.slicedbread.messages.error;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.MsgError;

/**
 * MsgErrProcessDead
 *
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
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

// 
//
//
