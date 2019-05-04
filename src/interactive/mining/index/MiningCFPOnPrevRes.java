package interactive.mining.index;

import interactive.exception.IncorrectStartPointException;
import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.SingleSeqWrapup;
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
        if (this.singleSeqWrapup.getFreqPatternList().size() == 0)
            return null;
        else {
            // create global bitset
            int seqLength = this.singleSeqWrapup.getSeqLength();
            BitSet globalBitSet = new BitSet(seqLength);
            globalBitSet.set(0, seqLength-1, true);
            ArrayList<FreqPatternWrapup> freqSeqFromPrev = this.singleSeqWrapup.getFreqPatternList();
            int indexOfFS = 0;
            int curLength = freqSeqFromPrev.get(0).getPatternLength();

            while (indexOfFS < freqSeqFromPrev.size() && curLength >= 2) {
                // when having some update, update this temp, finally update
                // global after checking all length = curLength
                BitSet tempGlobalBitSet = new BitSet(seqLength);
                tempGlobalBitSet.set(0, seqLength, true);
                HashMap<short[], Integer> tempFreqSeqRes = new HashMap<>();

                while (indexOfFS < freqSeqFromPrev.size()
                        && freqSeqFromPrev.get(indexOfFS).getPatternLength() == curLength) {
                    if (freqSeqFromPrev.get(indexOfFS).getAllSupport() < this.curLocalSupport) {
                        // if even before updating, the count is smaller than
                        // currentLocalSupport, then continue with the next one
                        indexOfFS++;
                        continue;
                    }
                    // first update current bitmap using global bit set,
                    // then update count and startIndex..
                    miningOnPrevFreqPattern(freqSeqFromPrev.get(indexOfFS), globalBitSet, seqLength, tempGlobalBitSet,
                            tempFreqSeqRes);
                    indexOfFS++;
                }
                // update global bit set
                globalBitSet.and(tempGlobalBitSet);
                freqSeqRes.putAll(tempFreqSeqRes);
                if (globalBitSet.cardinality() == 0)
                    break;
                curLength--;
            }
        }
        return this.freqSeqRes;
    }

    public void miningOnPrevFreqPattern(FreqPatternWrapup prevFreqPattern, BitSet globalBitSet, int curSeqLen,
                                        BitSet tempGlobalBitSet, HashMap<short[], Integer> tempFreqSeqRes){
        // get start points and recompute support
        BitSet remainStartPoint = prevFreqPattern.getAllStartPoints();
//        System.out.println(remainStartPoint);
        if(remainStartPoint.cardinality() < this.curLocalSupport)
            return;

        // if support satisfy, check availability of each occurrences and save to tempResult
        short [] originalSeq = singleSeqWrapup.getOriginalSequence();
        long [] originalTS = singleSeqWrapup.getOriginalTimeStamps();
        short [] curFreqPattern = prevFreqPattern.getFreqPatternInString();
        int newSupportCount = 0;

        int nextStartPos = 0;
        int curStartPoint = remainStartPoint.nextSetBit(nextStartPos);
        nextStartPos = curStartPoint + 1;
        int nextStartPoint = (remainStartPoint.cardinality() > 1) ? (remainStartPoint.nextSetBit(nextStartPos)) : curSeqLen;
        int i = 0;
        BitSet tempLocalBitSet = new BitSet(curSeqLen);
        tempLocalBitSet.set(0, curSeqLen, true);

        while (i < remainStartPoint.cardinality()) {
            if(globalBitSet.get(curStartPoint, nextStartPoint).cardinality() > 0){
                BitSet usedEventsBitSet = new BitSet(curSeqLen);
                // find first element
                int curIndex = curStartPoint;
                if(originalSeq[curIndex] != curFreqPattern[0]) {
                    throw new IncorrectStartPointException();
                }
                usedEventsBitSet.set(curIndex, true);
                int firstIndex = curIndex;
                long firstTS = originalTS[firstIndex];
                int prevIndex = firstIndex;
                long prevTS = originalTS[firstIndex];
                int matchPtr = 1;
                curIndex = firstIndex + 1;
                while(curIndex <= nextStartPoint - 1 && matchPtr < curFreqPattern.length) {
                    short curEle = originalSeq[curIndex];
                    long curTS = originalTS[curIndex];
                    if ((curIndex - firstIndex > curSeqGap + 1)
                            || (curIndex - prevIndex > curItemGap + 1)
                            || (curTS - firstTS > curSeqGapTS + 1)
                            || (curTS - prevTS > curItemGapTS + 1)){
                        // cannot accept this new element because it violates the gap constraint
                        break;
                    }
                    if(curEle == curFreqPattern[matchPtr]){
                        usedEventsBitSet.set(curIndex, true);
                        prevIndex = curIndex;
                        prevTS = curTS;
                        matchPtr++;
                    }
                    curIndex++;
                }
                if(matchPtr == curFreqPattern.length) {
                    usedEventsBitSet.and(globalBitSet);
                    if(usedEventsBitSet.cardinality() > 0) {
                        newSupportCount++;
                        usedEventsBitSet.flip(0, curSeqLen);
                        tempLocalBitSet.and(usedEventsBitSet);
                    }
                }
            }
            i++;
            curStartPoint = nextStartPoint;
            nextStartPos = curStartPoint + 1;
            nextStartPoint = (i >= remainStartPoint.cardinality() - 1) ? curSeqLen : (remainStartPoint.nextSetBit(nextStartPos));
        }
        if(newSupportCount >= this.curLocalSupport){
            tempGlobalBitSet.and(tempLocalBitSet);
            tempFreqSeqRes.put(curFreqPattern, newSupportCount);
        }
    }

    public String printMiningResult(){
        String str = "";
        for(Map.Entry<short [], Integer> pattern: freqSeqRes.entrySet()){
            str += InteractiveToolkit.shortArrayToString(pattern.getKey()) + "\t Support: " + pattern.getValue() + "\n";
        }
        return str;
    }
}

