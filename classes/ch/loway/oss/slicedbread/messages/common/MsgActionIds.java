
package ch.loway.oss.slicedbread.messages.common;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.MsgCommon;

/**
 * MsgActionIds
 *
 * This message contains two numeric status codes and one string.
 * general usage for Quick prototying.
 *
 * @version  $Id: MsgActionIds.java,v 1.1 2010/04/10 15:43:25 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
 */
public class MsgActionIds extends MsgCommon {

    int code1 = 0;
    int code2 = 0;
    String text = "";

    public int getCode1() {
        return code1;
    }

    public int getCode2() {
        return code2;
    }

    public String getText() {
        return text;
    }

    public void setCode1(int code1) {
        this.code1 = code1;
    }

    public void setCode2(int code2) {
        this.code2 = code2;
    }

    public void setText(String text) {
        this.text = text;
    }




    @Override
    public String toString() {
        return super.toString() + " - ActionIds "
                + code1 + " "
                + code2 + " "
                + text;
    }

    public static MsgActionIds build( PID from, PID to, int c1, int c2, String t) {
        MsgActionIds m =  new MsgActionIds();
        m.setFromPid(from);
        m.setToPid(to);
        m.setCode1(c1);
        m.setCode2(c2);
        m.setText(t);
        return m;
    }


}

// $Log: MsgActionIds.java,v $
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
