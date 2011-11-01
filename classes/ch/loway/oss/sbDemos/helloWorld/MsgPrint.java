
package ch.loway.oss.sbDemos.helloWorld;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.MsgCustom;

/**
 * This is a very basic message with only one field.
 * This exemplifies message passing.
 *
 * $Id$
 * @author lenz
 */
public class MsgPrint extends MsgCustom {

    private String text = "";

    /**
     * We set this private so that this object can only be built through
     * the build() method.
     * 
     */
    private MsgPrint() {
    }

    /**
     * We suggest using builders to create messages; so that they are immutable.
     * This is not mandatory, but plays well with the general design.
     * 
     * @param from The sender of this message
     * @param to The receiver of this message
     * @param msgToPrint The message to be printed
     * @return An object that's ready to go
     */
    public static MsgPrint build( PID from, PID to, String msgToPrint ) {
        final MsgPrint m = new MsgPrint();
        m.setText(msgToPrint);
        m.setRecipients(from, to);
        return m;
    }

    /**
     * @return the text to be printed
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to be printed
     */
    private void setText(String text) {
        this.text = text;
    }

}

// $Log$
//
