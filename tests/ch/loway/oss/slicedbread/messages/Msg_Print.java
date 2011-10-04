
package ch.loway.oss.slicedbread.messages;

import ch.loway.oss.slicedbread.containers.PID;

/**
 * Msg_Print
 *
 *
 * @version  $Id: Msg_Print.java,v 1.1 2010/04/10 15:43:25 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
 */
public class Msg_Print extends MsgCustom {

    String toBePrinted = "";

    public String getToBePrinted() {
        return toBePrinted;
    }

    public void setToBePrinted(String toBePrinted) {
        this.toBePrinted = toBePrinted;
    }

    @Override
    public String toString() {
        return super.toString() + " - Print: " + toBePrinted;
    }

    public static Msg_Print build( PID from, PID to, String s ) {
        Msg_Print p = new Msg_Print();
        p.setFromPid(from);
        p.setToPid(to);
        p.setToBePrinted(s);
        return p;
    }

}

// $Log: Msg_Print.java,v $
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
