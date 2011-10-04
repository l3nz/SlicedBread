/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.slicedbread.messages.common;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.MsgCommon;

/**
 * MsgProcessEnded
 *
 *
 * @version  $Id: MsgProcessEnded.java,v 1.1 2010/04/10 15:43:25 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
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

// $Log: MsgProcessEnded.java,v $
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
