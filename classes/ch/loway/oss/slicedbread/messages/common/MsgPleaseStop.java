
package ch.loway.oss.slicedbread.messages.common;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.MsgCommon;

/**
 * MsgPleaseStop
 *
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
 */
public class MsgPleaseStop extends MsgCommon {

    @Override
    public String toString() {
        return super.toString() + " - PleaseStop ";
    }

    public static MsgPleaseStop build( PID from, PID to) {
        MsgPleaseStop m =  new MsgPleaseStop();
        m.setFromPid(from);
        m.setToPid(to);
        return m;
    }


}
