package ch.loway.oss.slicedbread.tasks;

import ch.loway.oss.sbDemos.helloWorld.MsgPrint;
import ch.loway.oss.slicedbread.MessagingConsole;
import ch.loway.oss.slicedbread.SbTools;
import ch.loway.oss.slicedbread.TaskProcess;
import ch.loway.oss.slicedbread.containers.PID;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.common.MsgPleaseStop;
import ch.loway.oss.slicedbread.messages.wd.MsgThreadPool;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This task implements the watchdog.
 * 
 * A watchdog:
 * - launches and keeps active tasks
 * - manages executor pools
 * - runs deferred messages
 * 
 * @author lenz
 * @since 0.3
 */
public class WatchdogTask extends TaskProcess {

    public static final String WATCHDOG = "Sb_Watchdog_Task";
    
    private final Logger logger = LoggerFactory.getLogger(WatchdogTask.class);
    PID watchdogPid = null;
    Map<String,ThreadPool> pools = new HashMap<String, ThreadPool>();
    
    /**
     * This is our main loop.
     */
    
    @Override
    public void run() {

        watchdogPid = getOwnPid();
        log (
            "I am Watchdog "  + getProcessDescription()
            + " (created by " + getParentDescription() + ")"
        );
        MessagingConsole console = getConsole();

        try {
            for ( ;; ) {
                Msg m = console.receive(watchdogPid, 100);

                if ( m instanceof MsgPleaseStop ) {
                    logger.error("Watchdog gracefully stopping");
                    cleanup();
                    return;
                } else
                if ( m instanceof MsgThreadPool ) {
                    updateThreadPool( (MsgThreadPool) m );
                }
                
                // Processes our thread pool
                processThreadPoolMailboxes();
                
                
            }
        } catch ( InterruptedException e ) {
            logger.error( "Watchdog interrupted: ", e);
            return;
        }

    };



    private void log( String s ) {        
        logger.warn(s);
    }

    /***
     * Creates / updates / deleted a thread pool.
     * 
     * @param m 
     */
    
    private void updateThreadPool( MsgThreadPool m ) {
        
        String name = m.getName();
        
        if ( pools.containsKey( name ) ) {
            
            ThreadPool tp = pools.get(name);
            if ( m.isShutdown() ) {
                // close all threads
                tp.shutdown();
                
            } else {
                // update number of threads
            }
            
        } else {
            if ( !m.isShutdown() ) {
                ThreadPool newPool = new ThreadPool();
                newPool.create( name, m.getMinChannels(), m.getMaxChannels() );
                pools.put( name, newPool );
            }
        } 
    }
    
    /**
     * Process messages for thread pools.
     * 
     */
    
    private void processThreadPoolMailboxes() {
        for ( ThreadPool pool: pools.values() ) {
            pool.processMessages();
        }
    }
    
    /**
     * Check whether all thread pools are down.
     * The idea is taht the mailbox for the Pool will be removed only when 
     * all threads confirm termination; so when it's not found, this means all 
     * threads are dead.
     * 
     * @return true if no thread pool is found; false if some are found.
     */
    
    private boolean areAllThreadPoolsDown() {
        for ( ThreadPool pool: pools.values() ) {
            PID thisPid = ThreadPool.getPoolPid( pool.name );
            if ( thisPid != null ) {
                return false;
            }
        }
        return true;
    }
    
    /***
     * Cleans up any resources that are still there.
     * 
     */
    public void cleanup() {
        cleanupThreadPools();
    }
    
    /**
     * Cleans up the thread pool.
     * Blocks until all ogf them are gone.
     * 
     */
    
    public void cleanupThreadPools() {
        
        long beginning = System.currentTimeMillis();
        
        for ( ThreadPool pool: pools.values() ) {
            pool.shutdown();
        }
        
        // It is very important that I process events
        // because thread pools release resorces based on closure 
        // events.
        boolean ok = new SbTools.BlockUntil() {

            @Override
            public boolean stopIfTrue() {
                WatchdogTask.this.processThreadPoolMailboxes();
                return WatchdogTask.this.areAllThreadPoolsDown();
            }
            
        }.sync(10000);
            
        long took = System.currentTimeMillis() - beginning;
        logger.error( "All thread pools cleaned in " + took + " ms - OK: " + ok );
        
    }
    
}

