
package ch.loway.oss.slicedbread.messages.common;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.MsgCommon;

/**
 * MsgProcessEnded
 *
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
 */
public class MsgProcessEnded extends MsgCommon {


    @Override
    public String toString() {
        return super.toString() + " - ProcessEnded ";
    }

    // METODO factory

    public static MsgProcessEnded build( PID from, PID to ) {
        MsgProcessEnded m = new MsgProcessEnded();
        m.setFromPid(from);
        m.setToPid(to);
        return m;
    }





}

