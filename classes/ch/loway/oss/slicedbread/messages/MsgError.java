/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.slicedbread.messages;

/**
 * This abstract class is used for system error messages.
 *
 * @version  $Id: MsgError.java,v 1.1 2010/04/10 15:43:25 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
 */
public class MsgError  extends Msg {

    private Throwable cause = null;

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }
    

    @Override
    public String toString() {
        return "E-" + super.toString();
    }

}

// $Log: MsgError.java,v $
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
