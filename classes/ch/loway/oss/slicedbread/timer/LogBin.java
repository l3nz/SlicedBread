package ch.loway.oss.slicedbread.timer;

/**
 * This class represents one of the bins that are used for stats collection.
 * Each bin has a minimum and a maximum allowed value.
 * 
 * @author lenz
 */
public class LogBin {
    
    int minValue = 0;
    int maxValue = 0;
    int nValues = 0;
    
    public static LogBin build( int min, int max, int nVals ) {
        
        LogBin b = new LogBin();
        b.minValue = min;
        b.maxValue = max;
        b.nValues  = nVals;
        return b;
    }
    
    public int getIntervalMin() {
        return minValue;
    }
    
    public int getIntervalMax() {
        return maxValue;
    }
    
    public int getHits() {
        return nValues;
    }

    @Override
    public String toString() {
        return " i[" + getIntervalMin() +"," + getIntervalMax() +"]=" + getHits();
    }
    
    
    
    
}
