/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.slicedbread.messages.common;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.MsgCommon;

/**
 * MsgProcessStarted
 *
 *
 * @version  $Id: MsgProcessStarted.java,v 1.1 2010/04/10 15:43:25 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
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

// $Log: MsgProcessStarted.java,v $
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
