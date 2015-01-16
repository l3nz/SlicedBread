package ch.loway.oss.slicedbread.timer;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests our LogTimer.
 * 
 * @author lenz
 */
public class LogTimerTest {

   LogTimer lt = null; 
    
    
    public LogTimerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        lt = new LogTimer();
        lt.reset(16);
    }
    
    @After
    public void tearDown() {
        lt = null;
    }

    @Test
    public void testBinBoundaries() {

        for (int i = 0; i < 5; i++) {
            System.out.println("Bin " + i + " min " + lt.minForBin(i) + " max " + lt.maxForBin(i));
        }

        assertEquals("b0 min", 0, lt.minForBin(0));
        assertEquals("b0 max", 0, lt.maxForBin(0));

        assertEquals("b1 min", 1, lt.minForBin(1));
        assertEquals("b1 max", 1, lt.maxForBin(1));

        assertEquals("b2 min", 2, lt.minForBin(2));
        assertEquals("b2 max", 3, lt.maxForBin(2));

        assertEquals("b3 min", 4, lt.minForBin(3));
        assertEquals("b3 max", 7, lt.maxForBin(3));
    }
    
    @Test
    public void testBinNumber() {

        for (int i = 0; i < 10; i++) {
            System.out.println("Num " + i + " bin " + lt.binFor(i));
        }

        assertEquals("bin number", 0, lt.binFor(0));
        assertEquals("bin number", 1, lt.binFor(1));
        assertEquals("bin number", 2, lt.binFor(2));
        assertEquals("bin number", 2, lt.binFor(3));
        assertEquals("bin number", 3, lt.binFor(4));
        assertEquals("bin number", 3, lt.binFor(5));
        assertEquals("bin number", 5, lt.binFor(16));
    }

    

    /**
     * Test of add method, of class LogTimer.
     */
    @Test
    public void testEmptyBins() {
        lt.reset(16);
        List<LogBin> r = lt.results();
        System.out.println( r );
        
        assertEquals( "Bins found", 6+1, r.size() );
       
        assertEquals( "b0 min" , 0, r.get(0).getIntervalMin() );
        assertEquals( "b0 max" , 0, r.get(0).getIntervalMax() );
        
        assertEquals( "b1 min" , 1, r.get(1).getIntervalMin() );
        assertEquals( "b1 max" , 1, r.get(1).getIntervalMax() );

        assertEquals( "b2 min" , 2, r.get(2).getIntervalMin() );
        assertEquals( "b2 max" , 3, r.get(2).getIntervalMax() );

        assertEquals( "b3 min" , 4, r.get(3).getIntervalMin() );
        assertEquals( "b3 max" , 7, r.get(3).getIntervalMax() );
        
        assertEquals( "b4 min" , 8, r.get(4).getIntervalMin() );
        assertEquals( "b4 max" , 15, r.get(4).getIntervalMax() );

        assertEquals( "b5 min" , 16, r.get(5).getIntervalMin() );
        assertEquals( "b5 max" , 31, r.get(5).getIntervalMax() );
        
        assertEquals( "b last min" , 32, r.get(6).getIntervalMin() );
        assertEquals( "b last max" , Integer.MAX_VALUE, r.get(6).getIntervalMax() );
        
    }

    /**
     * Test of results method, of class LogTimer.
     */
    @Test
    public void testSendLinearResults() {
        lt.reset(7);
        
        for ( int i =0; i < 10; i++ ) {
            lt.add(i);
        }

        List<LogBin> r = lt.results();
        System.out.println( r );
        assertEquals( "Bins found", 5, r.size() );
       
        assertEquals( "b0  0-0" ,  1, r.get(0).getHits() );
        assertEquals( "b1  1-1" ,  1, r.get(1).getHits() );
        assertEquals( "b2  2-3" ,  2, r.get(2).getHits() );
        assertEquals( "b2  4-7" ,  4, r.get(3).getHits() );
        assertEquals( "b3  8+" ,   10-(2+2+4), r.get(4).getHits() );        
    }
    
    @Test
    public void testSendSparseResults() {
    
        lt.reset(7);
        
        lt.add(5);
        lt.add(5);
        lt.add(0);
        lt.add(2);
        lt.add(0);
        lt.add(3);
        
        List<LogBin> r = lt.results();
        System.out.println( r );
        assertEquals( "Bins found", 5, r.size() );
       
        assertEquals( "b0  0-0" ,  2, r.get(0).getHits() );
        assertEquals( "b1  1-1" ,  0, r.get(1).getHits() );
        assertEquals( "b2  2-3" ,  2, r.get(2).getHits() );
        assertEquals( "b2  4-7" ,  2, r.get(3).getHits() );
        assertEquals( "b3  8+" ,   0, r.get(4).getHits() );      
        
        
        
    }
    
    /**
     * Con il logaritmo:
     * 
     * Took 3656 for 100000000 iterations
     * 
     * Con lo shift
     * Took 2219 for 100000000 iterations
     * 
     * 
     */
    
    @Test
    public void testPerformance() {
        lt.reset(7);
        int N_CYCLES = 100000000;
        
        long now = System.currentTimeMillis();
        
        for ( int i =0; i < N_CYCLES; i++ ) {
            lt.add(i);
        }
    
        long took = System.currentTimeMillis() - now;
        System.out.println( "Took " + took + " for " + N_CYCLES + " iterations" );
        
    }
    
    
}
