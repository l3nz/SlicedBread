
package ch.loway.oss.slicedbread.messages.common;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.MsgCommon;

/**
 * MsgPleaseStop
 *
 *
 * @version  $Id: MsgPleaseStop.java,v 1.1 2010/04/10 15:43:25 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
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

// $Log: MsgPleaseStop.java,v $
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
