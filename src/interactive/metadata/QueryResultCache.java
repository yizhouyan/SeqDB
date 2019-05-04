package interactive.metadata;

import interactive.util.InteractiveToolkit;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * bitmap representation for one single pattern
 * Created by yizhouyan on 4/23/18.
 */
public class QueryResultCache extends QueryResult{
    protected BitSet startPoints;
    protected int patternLen;

//    public QueryResultCache(int support, ArrayList<Integer> startPoints,
//                            int sequenceLength, int patternLength) {
//        super(support);
//        this.startPoints = ContainerSelecter.generateBitSet(startPoints, sequenceLength, supportCount);
//        this.patternLen = patternLength;
//    }

    public QueryResultCache(int support, BitSet startPoints, int patternLength) {
        super(support);
        this.startPoints = startPoints;
        this.patternLen = patternLength;
    }

    public QueryResultCache(int support, int patternLength) {
        super(support);
        this.patternLen = patternLength;
    }

    public QueryResultCache clone() {
        QueryResultCache newWrapUp = new QueryResultCache(this.supportCount, this.startPoints, this.patternLen);
        return newWrapUp;
    }

    public String pirntSingleFS(int bitsetSize) {
        String str = "Start Index: ";
        str += startPoints;
        str += "\n";
        str += "Support # :" + this.supportCount;
        return str;
    }

    public BitSet getStartPoints() {
        return startPoints;
    }

    public void setStartPoints(BitSet startPoints) {
        this.startPoints = startPoints;
    }

    public int getPatternLen() {
        return patternLen;
    }

    public void setPatternLen(int patternLen) {
        this.patternLen = patternLen;
    }

    public ArrayList<Integer> getAllStartPoints(){
        return InteractiveToolkit.bits2Ints(startPoints);
    }
}
