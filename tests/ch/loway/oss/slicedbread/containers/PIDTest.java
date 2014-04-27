/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.slicedbread.containers;

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
public class PIDTest {

    public PIDTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testUniques() {

        PID p1 = PID.getNextPid("x");
        PID p2 = PID.getNextPid("y");

        assertTrue("ID diversi", p1.getPid() != p2.getPid() );
        assertEquals("Nome 1", "x", p1.getDescr() );
        assertEquals("Nome 1", "y", p2.getDescr() );
    }


    @Test
    public void testEquals() {
        
        PID p1 = PID.getNextPid("xx" );

        PID handBuilt = PID.build( p1.getPid(), "xx" );

        assertTrue("Equals", p1.equals(handBuilt) );
        assertTrue("Hash", p1.hashCode() == handBuilt.hashCode() );

    }

}