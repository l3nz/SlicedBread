
package ch.loway.oss.slicedbread.tasks;

import ch.loway.oss.slicedbread.MessagingConsole;
import ch.loway.oss.slicedbread.SbTools;
import ch.loway.oss.slicedbread.containers.*;
import ch.loway.oss.slicedbread.messages.Msg;
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
 * Test message execution on a thread pool.
 * 
 * @author lenz
 */
public class PlainPoolTest {

    public static final Logger logger = LoggerFactory.getLogger(PlainPoolTest.class);
    public static final MessagingConsole mc = MessagingConsole.getConsole();
    PID me = null;
    
    public PlainPoolTest() {
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
     * Checks that messages are returned in the same order as they were built.
     */
    @Test
    public void testStartStopThreadPool() {

        // Starts a thread pool
        PID watchdog = WatchdogTask.up();
        mc.send( MsgThreadPool.build(me, watchdog, "MYPOOL", 5, 5));
        
        assertTrue( "Wait for MYPOOL to go up", SbTools.awaitMailboxUp("MYPOOL", 10000));
        
        System.out.println( "Threads up: " + mc.list() );
        
        // Active threads: 5x pool + 2 ctrl, 1x WD, 1x hello
        assertEquals("Threads attivi", 9, mc.list().size() );
        
        mc.send(MsgThreadPool.buildShutdown(me, watchdog, "MYPOOL"));
        
        assertTrue( "Wait for MYPOOL to go down", SbTools.awaitMailboxDown("MYPOOL", 3000));
        
        // Active threads:  1x WD, 1x hello
        assertEquals("Threads dopo shutdown", 2, mc.list().size() );    
        
        SbTools.shutdown(watchdog);
        assertEquals("Threads dopo shutdown anche del WD", 1, mc.list().size() );    
        
    }

    
    @Test
    public void testRunTasksOnThreadPool() {
        
        logger.error( "Run tasks on thread pool ");
        
        // Starts a thread pool
        PID watchdog = WatchdogTask.up();
        mc.send( MsgThreadPool.build(me, watchdog, "MYPOOL", 5, 5));
        
        assertTrue( "Pool should be up", SbTools.awaitMailboxUp("MYPOOL", 10000));
        
        logger.error("Current mailboxes: " + mc.list() );
        
        PID pool = mc.findByDescription( "MYPOOL");
        for ( int i = 0; i < 20; i++ ) {
            final String TASK = "Task/" + i;
            mc.send(MsgPoolRunnable.build(me, pool, new RTask() {

                public Msg process() {
                    Logger ll = LoggerFactory.getLogger( TASK );
                    ll.error( TASK + " - Starting task");
                    SbTools.sleep(1000);
                    ll.error( TASK +  " - Ending task");
                    return null;
                }
                
            }));
        }
        
        SbTools.sleep(10000);
        
        mc.send(MsgThreadPool.buildShutdown(me, watchdog, "MYPOOL"));
        
        assertTrue( "Pool should be down", SbTools.awaitMailboxDown("MYPOOL", 10000));
        
        // Active threads:  1x WD, 1x hello
        assertEquals("Threads dopo shutdown", 2, mc.list().size() );    
        
    }
    
    
    
    
    


}