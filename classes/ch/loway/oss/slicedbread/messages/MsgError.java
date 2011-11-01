
package ch.loway.oss.slicedbread.messages;

/**
 * This abstract class is used for system error messages.
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
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

// 
//
//
