package ch.loway.oss.slicedbread.timer;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a logarithmic timer.
 * All times are set into a series of bins, each of which is 
 * the base-2 log of the time being measured.
 * Times are usually expressed in millisecond, but could be anything.
 * 
 * NOTE: this class is not meant to be thread-safe.
 * 
 * @author lenz
 * @since December 13, 2014
 */
public class LogTimer {
   
    int nHits = 0;
    int[] bins = new int[0];
    int overflow = 0;
    
    /**
     * Creates the measunring object.
     * 
     * @param maxValue The maximum allowed value.
     */
    
    public void reset( int maxValue ) {
        int mxBins = binFor( maxValue );
        bins = new int[mxBins+1];
        nHits = 0;
        overflow =0;
    }
    
    /**
     * Adds (counts) an item. 
     * 
     * @param v 
     */
    
    public void add( int v ) {
        int b = binFor(v);
        if ( b >= bins.length ) {
            overflow += 1;
        } else {
            bins[b] += 1;
        }
        nHits += 1;
    }
    
    /**
     * Computes a slow log2.
     * 
     * @param v
     * @return 
     */
    
    private int slow_log2( int v ) {
        final double log2 = Math.log(2);
        double l = Math.log( v ) / log2;
        return (int) l;
    }
    
    /**
     * The shift-based logarithm.
     * 
     * For some reason, i+1 is way quicker than i++.
     * Using an object variable is quicker.
     */
    
    int nonstack_v = 0;
    private int quick_log2( int vv ) {
        
        if (vv == 0) {
            return 0;
        }
        
        nonstack_v = vv;
        
        int i = -1;
        while ( nonstack_v> 0 ) {
            i = i+1;
            nonstack_v = nonstack_v >>> 1;    
        }
        return i;
    }
    
    
    /**
     * To which bin does this number belong to?
     * 
     * @param i A number
     * @return the bin it belongs to.
     */
    
    public int binFor( int i ) {
        if ( i == 0) {
            return 0;
        } else {
            return quick_log2(i)+1;
        }
    }
    
    
    /**
     * Creates a list with all results.
     * 
     * @return 
     */
   
    public List<LogBin> results() {
        
        List<LogBin> res = new ArrayList<LogBin>();
        
        for ( int i = 0; i < bins.length; i++ ) {
            res.add( LogBin.build( minForBin(i), maxForBin(i), bins[i]) );
        }
        
        // Overflow goes last.
        // Starts from the next value after the maximum of
        // our last bin.
        int minOverflow = minForBin( bins.length );
        LogBin bOverflow = LogBin.build( minOverflow, Integer.MAX_VALUE, overflow);
        res.add( bOverflow );
        
        return res;
    }
    
    /**
     * The minimum value for bin 'n'.
     * 
     * @param n
     * @return 
     */
    
    public int minForBin( int n ) {
        if ( n == 0 ) {
            return 0;
        } else {
            return (int) Math.pow( 2.0, n-1 );
        }
    }
    
    
    
    /**
     * What is the maximum value for bin 'n'?
     * 
     * @param n
     * @return 
     */
    
    public int maxForBin( int n ) {
        
        if ( n == 0 ){
            return 0;
        } else {
            return minForBin(n+1)-1;    
        }
        
    }
    
    
    
    
    
}
