
package ch.loway.oss.slicedbread.messages.wd;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;

/**
 *
 * @author lenz
 * @version $Id$
 */
public class MsgThreadPool extends Msg {
    
    String name = "";
    int minChannels = 0;
    int maxChannels = 0;
    
    
    public static MsgThreadPool build( PID from, PID to, String name, int min, int max ) {
        
        MsgThreadPool m = new MsgThreadPool();
        m.name = name;
        m.minChannels = min;
        m.maxChannels = max;
        m.setRecipients(from, to);
        return m;
        
    }
    
    public boolean isShutdown() {
        return (( minChannels == 0 ) && (maxChannels == 0));
    }
    
    public String getName() {
        return name;
    }

    public int getMinChannels() {
        return minChannels;
    }
    
    public int getMaxChannels() {
        return maxChannels;
    }

}


