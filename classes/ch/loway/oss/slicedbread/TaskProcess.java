
package ch.loway.oss.slicedbread;

import ch.loway.oss.slicedbread.containers.PID;

/**
 * TaskProcess
 *
 * This class implements the basic unit of work
 *
 * @author   l3nz
 * @since    1.0.0 - Nov 1, 2011
 */
public abstract class TaskProcess {

    PID parentPid      = null;
    PID ownPid         = null;
    MessagingConsole console = null;

    public MessagingConsole getConsole() {
        return console;
    }

    public PID getOwnPid() {
        return ownPid;
    }

    public PID getParentPid() {
        return parentPid;
    }

    public void setConsole(MessagingConsole console) {
        this.console = console;
    }

    public void setOwnPid(PID ownPid) {
        this.ownPid = ownPid;
    }

    public void setParentPid(PID parentPid) {
        this.parentPid = parentPid;
    }

    public String getProcessDescription() {
        return ownPid.getDescr();
    }

    public String getParentDescription() {
        return parentPid.getDescr();
    }

    public abstract void run();    

}
