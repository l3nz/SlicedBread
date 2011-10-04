/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.slicedbread.messages.error;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.MsgError;

/**
 * MsgErrProcessDied
 *
 *
 * @version  $Id: MsgErrProcessDied.java,v 1.1 2010/04/10 15:43:25 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
 */
public class MsgErrProcessDied extends MsgError {

    @Override
    public String toString() {
        return super.toString() + " - DiedProcess"  ;
    }

    // METODO factory

    public static MsgErrProcessDied build( PID from, PID to, Throwable cause ) {
        MsgErrProcessDied m = new MsgErrProcessDied();
        m.setFromPid(from);
        m.setToPid(to);
        m.setCause(cause);
        return m;
    }


}

// $Log: MsgErrProcessDied.java,v $
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
