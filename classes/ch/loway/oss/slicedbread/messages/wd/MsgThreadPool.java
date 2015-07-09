
package ch.loway.oss.slicedbread.messages.wd;

import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;

/**
 * This message is sent to the watchdog to create thread pools.
 *
 * @author lenz
 * @version $Id$
 */
public class MsgThreadPool extends Msg {
    
    String name = "";
    int minChannels = 0;
    int maxChannels = 0;
    
    /**
     * Creates a thread pool.
     * 
     * @param from
     * @param to
     * @param name
     * @param min
     * @param max
     * @return 
     */
    
    public static MsgThreadPool build( PID from, PID to, String name, int min, int max ) {
        
        MsgThreadPool m = new MsgThreadPool();
        m.name = name;
        m.minChannels = min;
        m.maxChannels = max;
        m.setRecipients(from, to);
        return m;
        
    }
    
    /**
     * Shut down a thread pool.
     * 
     * @param from
     * @param to
     * @param name
     * @return 
     */
    
    public static MsgThreadPool buildShutdown( PID from, PID to, String name ) {
        return build( from, to , name, 0, 0 );
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


