package ch.loway.oss.slicedbread.timer;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class is very plain, but having a test always helps.
 * 
 * @author lenz
 */
public class LogBinTest {
    
    public LogBinTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of build method, of class LogBin.
     */
    @Test
    public void testBuild() {
        
        LogBin b = LogBin.build(16, 199, 233);
        assertEquals( "min",  16, b.getIntervalMin() );
        assertEquals( "min", 199, b.getIntervalMax() );
        assertEquals( "min", 233, b.getHits() );
    }
    
    
    @Test
    public void printAsText() {
        
        LogTimer lt = new LogTimer();
        lt.reset(7);
        
        lt.add(5);
        lt.add(5);
        lt.add(0);
        lt.add(2);
        lt.add(0);
        lt.add(3);
        
        List<LogBin> lR = lt.results();
        
        System.out.println( lR );
        
        System.out.println( LogBin.printAsText(lR ));
        
    }
}
