package ch.loway.oss.slicedbread.misc;

/**
 * Create a string out of fixed-width blocks.
 * 
 * @author lenz
 */
public class StrFmt {
   
    StringBuilder sb = new StringBuilder();
    
    /**
     * 
     *  Left-aligned string.
     * 
     */
    
    public StrFmt addL( String s, int size ) {
        s = trim( s, size );
        sb.append(s);
        addSpaces( size - s.length() );
        return this;
    }
    
    public StrFmt addR( String s, int size ) {
        s = trim( s, size );
        addSpaces( size - s.length() );
        sb.append(s);
        return this;
    }
    
    public StrFmt add( String s ) {
        sb.append(s);
        return this;
    }
    
    public StrFmt addL( int n, int size ) {
        return addL( Integer.toString(n), size );
    }

    public StrFmt addR( int n, int size ) {
        return addR( Integer.toString(n), size );
    }

    public StrFmt addHistogram( int n, int total, int nCols ) {
        
        int cols = 0;
        if ( total > 0 ) {
            cols =(n*(nCols-1))/total;
        }
        
        add( "|");
        
        addChars( cols, '#' );
        addSpaces((nCols-1)-cols);
        return this;
    }
    
    public StrFmt addPerc( int n, int total ) {
        int perc = 0;
        if ( total > 0 ) {
            perc =(n*100)/total;
        }
        addR( perc, 3 );
        add( "% ");
        return this;
    }
    
    public String trim( String s, int maxLen ) {
        if ( s.length() > maxLen ) {
            return s.substring(0, maxLen);
        } else {
            return s;
        }
    }
    
    public void addSpaces( int n ) {
        addChars( n, ' ' );
    }

    public void addChars( int n, char c ) {
        for (int i=0; i < n; i++ ) {
            sb.append( c );
        }
    }

    @Override
    public String toString() {
        return sb.toString(); 
    }
    
    

    
}
