/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.slicedbread;

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
public class MessagingConsoleTest {

    public MessagingConsoleTest() {
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
    public void testThreadSleep() throws InterruptedException {

        testDuration(500, 1);
        testDuration(500, 10);
        testDuration(500, 100);

        assertTrue(true);
    }

    /**
     * Stampa una durata effettiva steppando fino al periodo indicato.
     * 
     * @param duration
     * @param step
     * @throws InterruptedException
     */

    private void testDuration( int duration, int step ) throws InterruptedException {
        int iters = duration / step;

        long t0 = System.currentTimeMillis();
        for ( int i = 0; i < iters; i++) {
            Thread.sleep( step );
        }

        long dur = System.currentTimeMillis() -t0;
        long expected = iters * step;

        System.out.println( "Sleep Duration: " + step +"ms "
                + "-  Expected: " + expected + "ms "
                + "- Actual: " + dur + "ms");

    }



}