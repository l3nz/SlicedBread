
package ch.loway.oss.sbDemos.helloWorld;

import ch.loway.oss.slicedbread.MessagingConsole;
import ch.loway.oss.slicedbread.SbTools;
import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.containers.QueueInfo;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.common.MsgActionIds;
import ch.loway.oss.slicedbread.messages.common.MsgPleaseStop;
import ch.loway.oss.slicedbread.messages.wd.MsgPoolRunnable;
import ch.loway.oss.slicedbread.messages.wd.MsgThreadPool;
import ch.loway.oss.slicedbread.tasks.ThreadPool;
import ch.loway.oss.slicedbread.tasks.WatchdogTask;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class shows how to run a thread pool in SlicedBread.
 *
 * 
 * 
 * @author l3nz
 */
public class TestPool {
    
    private final static Logger logger = LoggerFactory.getLogger(TestPool.class);

    public static void main( String args[] ) {
        logger.warn("Starting up....");
        
        MessagingConsole console = MessagingConsole.getConsole();
        final PID myself = console.registerExistingThread("Main");

        PID watchdog = console.register(myself, WatchdogTask.WATCHDOG, new WatchdogTask() );
        
        console.send( MsgThreadPool.build(myself, watchdog, "P1", 10, 10) );
        console.send( MsgThreadPool.build(myself, watchdog, "P2", 1, 10) );
        
        PID pool1 = ThreadPool.getPoolPid_blocking( "P1", 2000 );
        PID pool2 = ThreadPool.getPoolPid_blocking( "P2", 2000 );
        
        logConsole("Before", console);
        
        
        for ( int i = 0; i < 10000; i++ ) {
            
            PID pool = ( i%2 == 0) ? pool1 : pool2;
            final String poolName = pool.getDescr();
            
            console.send( MsgPoolRunnable.build(myself, pool, new MsgPoolRunnable.RTask() {

                public Msg process() {            
                    String msg = poolName + ":" + Thread.currentThread().getName();
                    return MsgActionIds.build(null, myself, 1, 0, msg);                  
                }
            }));
        }
        
        
        // then we sleep for a while and check our e-mail
        SbTools.sleep(5000);
        CounterHash ch = new CounterHash();
        for ( Msg m: console.receiveAll(myself) ) {
        
            if ( m instanceof MsgActionIds ) {
                MsgActionIds i = (MsgActionIds) m;
                ch.add(i.getText());
            }
        }
        logger.warn("Received:\n" + ch.toString() );
        
        
        logConsole("After", console);
        
        // Now kill the thread pools
//        console.send( MsgThreadPool.build(myself, watchdog, "P1", 0, 0) );
//        console.send( MsgThreadPool.build(myself, watchdog, "P2", 0, 0) );
//        
//        
//        console.blockUntilPidDisappears( ThreadPool.getPoolName( "P1" ), 2000);
//        console.blockUntilPidDisappears( ThreadPool.getPoolName( "P2" ), 2000);

        //        
        // Kill the Watchdog
        console.send( MsgPleaseStop.build(myself, watchdog) );
        console.blockUntilPidDisappears(WatchdogTask.WATCHDOG, 10000);
        
        logConsole("Shutdown", console);
        
        
        
    }


    private static void logConsole( String name, MessagingConsole console ) {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append( "Console at point " ).append( name ).append( "\n");
        
        List<QueueInfo> lQI = console.list();
        List<String> lA = new ArrayList<String>();
        
        for ( QueueInfo qi: lQI ) {
            lA.add(qi.toString() );
        }
        
        Collections.sort(lA);
        
        for ( String s: lA ) {
            sb.append( s );
        }
        
        logger.warn(sb.toString());
    }
    
    
    public static class CounterHash {
        int n =0;
        Map<String,Integer> m = new HashMap<String,Integer>();
        
        public void add( String s ) {
            int v =0;
            if ( m.containsKey(s) ) {
                v = m.get(s);
            }
            m.put(s, v+1);
            n +=1;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            List<String> lS = new ArrayList<String>( m.keySet() );
            Collections.sort(lS);
            for ( String s: lS ) {
                sb.append( s ).append( ": ").append( m.get(s) ).append("\n");
            }
            sb.append( "Total: ").append( n ).append( "\n"); 
            return sb.toString();
        }
        
        
        
    }
    

}

// $Log$
//
