
package ch.loway.oss.slicedbread;

import ch.loway.oss.slicedbread.containers.PID;

/**
 * TaskProcess
 *
 * This class implements the basic unit of work
 *
 * @version  $Id: TaskProcess.java,v 1.1 2010/04/10 15:43:25 lenz-mobile Exp $
 * @author   gnu
 * @since    1.x.x - 10-apr-2010
 * @see
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

// $Log: TaskProcess.java,v $
// Revision 1.1  2010/04/10 15:43:25  lenz-mobile
// no message
//
//
//
