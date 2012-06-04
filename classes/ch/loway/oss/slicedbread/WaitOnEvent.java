
package ch.loway.oss.slicedbread;

/**
 *
 *
 * @author lenz
 * @since  2012-06-04
 */
public abstract class WaitOnEvent {


    long startWait = 0L;
    long waitLen   = 0L;

    public abstract boolean stopWhenTrue();

    public boolean wait( int step, int max ) {

        startWait = System.currentTimeMillis();
        long maxTime = startWait + max;

        while ( System.currentTimeMillis() < maxTime ) {
            if ( stopWhenTrue() ) {
                waitLen = System.currentTimeMillis() - startWait;
                return true;
            }
            SbTools.sleep(step);
        }

        waitLen = System.currentTimeMillis() - startWait;
        return false;
    }

    public boolean wait( int max ) {
        return wait( 50, max );
    }

    public long getWaitTime() {
        return waitLen;
    }


}

// 
//
