package ch.loway.oss.slicedbread.messages;

import ch.loway.oss.slicedbread.containers.PID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lenz
 */
public class MsgHashTest {
    
    PID fromPid = PID.getNextPid("from");
    PID toPid   = PID.getNextPid("to");
    
    
    
    public MsgHashTest() {
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
     * 
     */
    @Test
    public void testPlain() {
    
        MsgHash mh = MsgHash.Builder.build( Farlokk.LOCC )
                .setRecipients(fromPid, toPid)
                .put( Babbon.MIZZY, "Miz" )
                .put( "Hello", "there" )
                .seal();
        
        System.out.println( mh.toString() );
        
        switch ( (Farlokk) mh.t() ) {
            case LOCC:
                break;
        }
        
        
        assertTrue( mh.t() == Farlokk.LOCC );
        assertEquals( "Miz", mh.getS( Babbon.MIZZY ));
        assertEquals( "there", mh.getS( "Hello" ));
        
    }
    
    @Test
    public void testEnumSwitching() {
        MsgHash mh = MsgHash.Builder.build( Farlokk.LOCC )
                .setRecipients(fromPid, toPid)
                .put( Babbon.MIZZY, "Miz" )
                .put( "Hello", "there" )
                .seal();
        
        System.out.println( mh.toString() );
        
        boolean found = false;
        
        switch ( (Farlokk) mh.t() ) {
            case LOCC:
                found = true;
                break;
            default:
                break;
        }
        
        assertTrue( "Correct switch", found );
        
    }
    
    
    
    /**
     * Enums used for testing.
     */
    
    
    public static enum Farlokk {
        FAR,
        LOCC,
        SBAB,
        BON;
    }
    
    public static enum Babbon {
        MIZZY,
        PIPPO
    }
    
}
