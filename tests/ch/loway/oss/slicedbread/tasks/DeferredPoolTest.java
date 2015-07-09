
package ch.loway.oss.slicedbread.tasks;

import ch.loway.oss.slicedbread.MessagingConsole;
import ch.loway.oss.slicedbread.SbTools;
import ch.loway.oss.slicedbread.containers.*;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.wd.MsgPoolDeferred;
import ch.loway.oss.slicedbread.messages.wd.MsgPoolRunnable;
import ch.loway.oss.slicedbread.messages.wd.MsgPoolRunnable.RTask;
import ch.loway.oss.slicedbread.messages.wd.MsgThreadPool;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test deferred message execution on a thread pool.
 * 
 * @author lenz
 */
public class DeferredPoolTest {

    public static final Logger logger = LoggerFactory.getLogger(DeferredPoolTest.class);
    public static final MessagingConsole mc = MessagingConsole.getConsole();
    PID me = null;
    
    public DeferredPoolTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
//        q = new MsgQueue();
        
        me = mc.registerExistingThreadByName("HELLO");
        
        
    }

    @After
    public void tearDown() {
//        q = null;
        me = null;
        mc.reset();
    }

    /**
     * Tries running a message a given number of times.
     */
    @Test
    public void testStartStopThreadPool() {

        // Starts a thread pool
        final PID watchdog = WatchdogTask.up();
        mc.send( MsgThreadPool.build(me, watchdog, "MYPOOL", 3, 3));
        
        SbTools.awaitMailboxUp("MYPOOL", 2000);
        final PID poolPid = mc.findByDescription("MYPOOL");
        assertNotNull( "Pool PID", poolPid);
        
        
        MsgPoolDeferred m1 = MsgPoolDeferred.build(me, watchdog, poolPid, 100, 
            new RTaskGeneric("A", 5, 150, 200, me, watchdog, poolPid)
        );
        
        MsgPoolDeferred m2 = MsgPoolDeferred.build(me, watchdog, poolPid, 100, 
            new RTaskGeneric("B", 3, 800, 100, me, watchdog, poolPid)
        );
        
        MsgPoolDeferred m3 = MsgPoolDeferred.build(me, watchdog, poolPid, 100, 
            new RTaskGeneric("C", 7, 10, 20, me, watchdog, poolPid)
        );
        
        mc.send(m1);
        mc.send(m2);
        mc.send(m3);
        
        for ( int i =0; i < 4; i++) {
            logger.error("1 sec elapsed");
            SbTools.sleep(1000);
        }
        
        
        SbTools.sleep(5000);
        
        
        
        
        
    }

    
    
    public static class RTaskGeneric implements RTask {

        final String taskName;
        final int maxCycles;
        final int runsFor;
        final int reschedulesIn;
        final PID fromPid;
        final PID watchDogPid;
        final PID poolPid;
        
        
        public RTaskGeneric( String name, int loops, int dur, int reschAfter, PID f, PID wd, PID p ) {
            taskName = name;
            maxCycles = loops;
            runsFor = dur;
            reschedulesIn = reschAfter;
            fromPid = f;
            watchDogPid = wd;
            poolPid = p;
            
        }
        
        int i = 0;
        
        
        public Msg process() {
                    String preamble = "Task " + taskName + " (iteration " + i + ") ";
                    logger.error(preamble + "starting at " + System.currentTimeMillis() );
                    SbTools.sleep( runsFor );
                    i = i+ 1;
                    logger.error(preamble + "stopping at " + System.currentTimeMillis() );

                    if ( i < maxCycles ) {
                        return MsgPoolDeferred.build( fromPid, watchDogPid, poolPid, 100, this);
                    } else {
                        return null;
                    }

                }
        
        
    }
    
    
    


}