

package ch.loway.oss.slicedbread.messages.error;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.MsgError;

/**
 * MsgErrProcessDied
 *
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
 */
public class MsgErrProcessDied extends MsgError {

    @Override
    public String toString() {
        return super.toString() + " - DiedProcess"  ;
    }

    // METODO factory

    public static MsgErrProcessDied build( PID from, PID to, Throwable cause ) {
        final MsgErrProcessDied m = new MsgErrProcessDied();
        m.setFromPid(from);
        m.setToPid(to);
        m.setCause(cause);
        return m;
    }


}

// 
//
//
