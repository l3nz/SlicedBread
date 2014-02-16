/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.slicedbread.containers;

import java.util.List;
import ch.loway.oss.slicedbread.messages.Msg;
import ch.loway.oss.slicedbread.messages.MsgCommon;
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
public class MsgQueueTest {

    MsgQueue q = null;
    final static int N_MSGS = 100;
    final static int N_LOAD = 200000;

    public MsgQueueTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        q = new MsgQueue();
    }

    @After
    public void tearDown() {
        q = null;
    }

    /**
     * Checks that messages are returned in the same order as they were built.
     */
    @Test
    public void testQueueDequeue() {

        for ( int i = 0; i < N_MSGS; i++ ) {
            Msg m = new MsgNum(i);
            int nQueued = q.push(m);
            assertEquals( "N messaggi:", i+1, nQueued );
        }

        assertEquals("Dimensioni coda piena", N_MSGS, q.size() );

        MsgNum m = null;
        int i = 0;
        do {
            m = (MsgNum) q.pull();
            if ( m != null) {
            assertEquals("Numero", i, m.num);
            i += 1;
            }
        } while ( m != null);

        assertEquals("Dimensioni coda vuota", 0, q.size() );
    }

    @Test
    public void testQueueDequeueAll() {

        for ( int i = 0; i < N_MSGS; i++ ) {
            Msg m = new MsgNum(i);
            int nQueued = q.push(m);
            assertEquals( "N messaggi:", i+1, nQueued );
        }

        assertEquals("Dimensioni coda piena", N_MSGS, q.size() );

        List<Msg> lM = q.fetchAllMessages();
        int i = 0;

        for ( Msg m: lM  ) {
            MsgNum mn = (MsgNum) m;
            assertEquals("Numero", i, mn.num);
            i += 1;
        }

        assertEquals("Dimensioni coda vuota", 0, q.size() );
    }



    @Test
    public void enqueue_and_dequeue_many_msgs_one_thread() {

        long t0 = System.currentTimeMillis();
        final int MAX_QUEUED = 500;

        int totalCounter = 0;

        for ( int i = 0;i < N_LOAD; i++ ) {

            int howManyForIter = i % MAX_QUEUED;

            for ( int n = 0; n < howManyForIter; n++ ) {
                q.push( new MsgNum(i) );
            }

            MsgNum recvd = null;
            do {
                recvd = (MsgNum) q.pull();
                totalCounter += 1;
            } while ( recvd != null );                        
        }

        long dur = System.currentTimeMillis() - t0;
        System.out.println( "Enqueued " + totalCounter + " msgs in " + dur + "ms");
    }



    /**
     * Un messaggio che contiene un numero (per test)
     */

    public static class MsgNum extends MsgCommon {
        public final int num;

        public MsgNum(int num) {
            this.num = num;
        }
    }


}