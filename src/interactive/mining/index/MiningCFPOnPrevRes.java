package interactive.mining.index;

import interactive.exception.IncorrectStartPointException;
import interactive.metadata.Constants;
import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.SingleSeqWrapup;
import interactive.metadata.bmcontainer.BitsetContainer;
import interactive.metadata.bmcontainer.ContainerList;
import interactive.metadata.bmcontainer.IBMContainer;
import interactive.metadata.bmcontainer.ManageStartPoints;
import interactive.util.InteractiveToolkit;

import java.util.*;

/**
 * Created by yizhouyan on 4/24/18.
 */
public class MiningCFPOnPrevRes {
    private SingleSeqWrapup singleSeqWrapup;
    private int curLocalSupport;
    private int curItemGap;
    private int curSeqGap;
    private long curItemGapTS;
    private long curSeqGapTS;
    private HashMap<short[], Integer> freqSeqRes;

    public MiningCFPOnPrevRes(SingleSeqWrapup singleSeqWrapup, int curSupport,
                              int curItemGap, int curSeqGap, long curItemGapTS,
                              long curSeqGapTS){
        this.singleSeqWrapup = singleSeqWrapup;
        this.curItemGap = curItemGap;
        this.curLocalSupport = curSupport;
        this.curSeqGap = curSeqGap;
        this.curItemGapTS = curItemGapTS;
        this.curSeqGapTS = curSeqGapTS;
    }

    public HashMap<short[], Integer> FreqPatternMiningOnPrev() {
        this.freqSeqRes = new HashMap<short[], Integer>();
        if (this.singleSeqWrapup.getFreqPatternsMap().size() == 0)
            return null;
        else {
            int seqLength = this.singleSeqWrapup.getSeqLength();
            ArrayList<Integer> freqLengths = this.singleSeqWrapup.getPatternLengths();
            for(int curLength : freqLengths){
                int indexOfFS = 0;
                ArrayList<FreqPatternWrapup> freqSeqFromPrev = this.singleSeqWrapup.getFreqPatternsMap().get(curLength);
                while (indexOfFS < freqSeqFromPrev.size()) {
                    FreqPatternWrapup curPattern = freqSeqFromPrev.get(indexOfFS);
                    if(curPattern.getAllSupport() >= this.curLocalSupport){
                        miningOnPrevFreqPattern(curPattern, seqLength);
                    }else
                        break;
                    indexOfFS++;
                }
            }
        }
        return this.freqSeqRes;
    }

    public void miningOnPrevFreqPattern(FreqPatternWrapup prevFreqPattern,  int curSeqLen){
        // get start points and recompute support
        BitSet remainStartPoint = prevFreqPattern.getAllStartPoints(curSeqLen, freqSeqRes, singleSeqWrapup.getOriginalSequence(),
                singleSeqWrapup.getOriginalTimeStamps(), curItemGap, curSeqGap, curItemGapTS, curSeqGapTS, curLocalSupport);
//        System.out.println(remainStartPoint);
        if(remainStartPoint == null || remainStartPoint.cardinality() < this.curLocalSupport)
            return;
        freqSeqRes.put(prevFreqPattern.getFreqPatternInString(), remainStartPoint.cardinality());

//        // if support satisfy, check availability of each occurrences and save to tempResult
//        short [] originalSeq = singleSeqWrapup.getOriginalSequence();
//        long [] originalTS = singleSeqWrapup.getOriginalTimeStamps();
//        short [] curFreqPattern = prevFreqPattern.getFreqPatternInString();
//        int newSupportCount = 0;
//
//        int nextStartPos = 0;
//        int curStartPoint = remainStartPoint.nextSetBit(nextStartPos);
//        nextStartPos = curStartPoint + 1;
//        int nextStartPoint = (remainStartPoint.cardinality() > 1) ? (remainStartPoint.nextSetBit(nextStartPos)) : curSeqLen;
//        int i = 0;
//
//        while (i < remainStartPoint.cardinality()) {
//            // find first element
//            int curIndex = curStartPoint;
//            if(originalSeq[curIndex] != curFreqPattern[0]) {
//                throw new IncorrectStartPointException();
//            }
//            int firstIndex = curIndex;
//            long firstTS = originalTS[firstIndex];
//            int prevIndex = firstIndex;
//            long prevTS = originalTS[firstIndex];
//            int matchPtr = 1;
//            curIndex = firstIndex + 1;
//            while(curIndex <= nextStartPoint - 1 && matchPtr < curFreqPattern.length) {
//                short curEle = originalSeq[curIndex];
//                long curTS = originalTS[curIndex];
//                if ((curIndex - firstIndex > curSeqGap + 1)
//                        || (curIndex - prevIndex > curItemGap + 1)
//                        || (curTS - firstTS > curSeqGapTS + 1)
//                        || (curTS - prevTS > curItemGapTS + 1)){
//                    // cannot accept this new element because it violates the gap constraint
//                    break;
//                }
//                if(curEle == curFreqPattern[matchPtr]){
//                    prevIndex = curIndex;
//                    prevTS = curTS;
//                    matchPtr++;
//                }
//                curIndex++;
//            }
//            if(matchPtr == curFreqPattern.length) {
//                newSupportCount++;
//            }
//            i++;
//            curStartPoint = nextStartPoint;
//            nextStartPos = curStartPoint + 1;
//            nextStartPoint = (i >= remainStartPoint.cardinality() - 1) ? curSeqLen : (remainStartPoint.nextSetBit(nextStartPos));
//        }
//        if(newSupportCount >= this.curLocalSupport){
//            tempFreqSeqRes.put(curFreqPattern, newSupportCount);
//        }
    }

    public String printMiningResult(){
        String str = "";
        for(Map.Entry<short [], Integer> pattern: freqSeqRes.entrySet()){
            str += InteractiveToolkit.shortArrayToString(pattern.getKey()) + "\t Support: " + pattern.getValue() + "\n";
        }
        return str;
    }
}

