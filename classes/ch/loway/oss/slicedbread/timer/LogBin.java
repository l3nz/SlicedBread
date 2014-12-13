package ch.loway.oss.slicedbread.timer;

import ch.loway.oss.slicedbread.misc.StrFmt;
import java.util.List;

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
    
    public boolean isOpenEnded() {
        return maxValue == Integer.MAX_VALUE;
    }
    
    /**
     * Pretty-prints a list of LogBin stats.
     * 
     * @param l
     * @return 
     */
    
    public static String printAsText(List<LogBin> l) {

        if (l == null) {
            return "- No bins -";
        }

        int maxBin = 0;
        int totReq = 0;
        for (int i = 0; i < l.size(); i++) {
            LogBin b = l.get(i);
            if (b.getHits() > 0) {
                maxBin = i;
                totReq += b.getHits();
            }

        }

        StrFmt f = new StrFmt();

        for (int i = 0; i < maxBin; i++) {
            LogBin b = l.get(i);
            f.add("Bin ")
                    .addR(b.getIntervalMin(), 5)
                    .add(b.isOpenEnded() ? "+ " : "ms")
                    .addPerc(b.getHits(), totReq)
                    .addR(b.getHits(), 8)
                    .addHistogram(b.getHits(), totReq, 15)
                    .add("\n");

        }

        return f.toString();
    }
    
    
    
}
