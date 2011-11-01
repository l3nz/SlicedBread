
package ch.loway.oss.slicedbread.messages.common;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.MsgCommon;

/**
 * MsgProcessStarted
 *
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
 */
public class MsgProcessStarted extends MsgCommon {


    @Override
    public String toString() {
        return super.toString() + " - ProcessStarted";
    }

    // METODO factory

    public static MsgProcessStarted build( PID from, PID to ) {
        MsgProcessStarted m = new MsgProcessStarted();
        m.setFromPid(from);
        m.setToPid(to);
        return m;
    }





}

