package interactive.metadata;

import interactive.mining.prefixspan.ResItemArrayPair;
import interactive.util.InteractiveToolkit;

import java.util.*;

/**
 * Created by yizhouyan on 5/27/18.
 */
public class FreqPatternWrapup implements IPattern, Comparable {
    protected short[] freqPattern;
    protected BitSet startPoints;
    protected int allSupport = 0;

    public FreqPatternWrapup(String freqPatternInString) {
        String[] freqPatternSplits = freqPatternInString.split(",");
        freqPattern = new short[freqPatternSplits.length];
        for (int i = 0; i < freqPatternSplits.length; i++)
            freqPattern[i] = Short.parseShort(freqPatternSplits[i]);
    }

    public FreqPatternWrapup(short[] freqPattern, BitSet startPoints) {
        this.freqPattern = freqPattern.clone();
        this.startPoints = startPoints;
    }

    public void generateBitSet(ArrayList<Integer> currentSetPoints, int sequenceLength) {
        this.startPoints = new BitSet(sequenceLength);
        this.startPoints.set(0, sequenceLength, false);
        for (Integer indexForLetter : currentSetPoints) {
            this.startPoints.set(indexForLetter, true);
        }
    }

    public String toString() {
        String str = InteractiveToolkit.shortArrayToString(freqPattern)
                +"\nAll Support:" + allSupport
                + "\nStart Points: \n";
        if (this.startPoints != null) {
            str += this.startPoints + "\n";
        }
        return str;
    }

    public String toString(HashMap<Short, String> dictionary) {
        String str = InteractiveToolkit.shortArrayToString(freqPattern, dictionary)
                +"\nAll Support:" + allSupport
                + "\nStart Points: \n";
        if (this.startPoints != null) {
            str += this.startPoints + "\n";
        }

        return str;
    }

    public QueryResultCache getCompleteQueryResult(){
        BitSet curStartPoints = (BitSet) startPoints.clone();
        return new QueryResultCache(curStartPoints.cardinality(), curStartPoints, freqPattern.length);
    }

    public BitSet getAllStartPoints(){
        BitSet curStartPoints = (BitSet) startPoints.clone();
        return curStartPoints;
    }

//    public QueryResult getQueryResult(int numSeqBits){
//        if(this.queryResultCache !=null)
//            return queryResultCache;
////        BitSet sequenceInBit = new BitSet(numSeqBits);
//        int supportCount = this.supportCount;
//        ArrayList<Integer> startIndex = new ArrayList<>();
//        ArrayList<Integer> endIndex = new ArrayList<>();
//        // first mark its own start and end in the sequence
//        if(bitSet !=null && bitSet.size() > 0){
//            for(SingleBitSet singleBitSet: bitSet){
//                int [] tempStart = singleBitSet.getStartPoints();
//                int [] bitsTrue = InteractiveToolkit.bitSetToIntArray(singleBitSet.getBitset());
//                for(int i: tempStart){
////                    for(int j : bitsTrue)
////                        sequenceInBit.set(i + j, true);
//                    startIndex.add(i);
//                    endIndex.add(i+bitsTrue[bitsTrue.length-1]);
//                }
//            }
//        }
//        FreqPatternWrapup parentSequence = this.superPattern;
//        FreqPatternWrapup curSequence = this;
//        while(parentSequence != null){
//            // get start, end, bitmap from parent sequences
//            if(parentSequence.getBitSet()!=null && parentSequence.getBitSet().size() > 0){
//                // first compute global remove position between two patterns
//                Integer [] removePos = InteractiveToolkit.computeRemovePositions(parentSequence.freqPattern, freqPattern);
//                for(SingleBitSet singleBitSet: parentSequence.bitSet){
//                    int [] tempStart = singleBitSet.getStartPoints();
//                    int startPos = 0;
//                    Integer [] bitsTrue = InteractiveToolkit.computeNewBitSet(singleBitSet.getBitset(), removePos);
//                    for(int i: tempStart){
////                        for(int j : bitsTrue)
////                            sequenceInBit.set(i + j, true);
//                        int curAddPos = findAddingPosition(startIndex, startPos, i);
//                        startIndex.add(curAddPos, i);
//                        endIndex.add(curAddPos, i+bitsTrue[bitsTrue.length-1]);
//                        startPos = curAddPos + 1;
//                    }
//                }
//            }
//            curSequence = parentSequence;
//            parentSequence = parentSequence.superPattern;
//        }
////        Collections.sort(startIndex);
////        Collections.sort(endIndex);
//        queryResultCache = new QueryResult(supportCount, startIndex, endIndex); //sequenceInBit
//        return queryResultCache;
//    }

    public static int findAddingPosition(ArrayList<Integer> curStart, int start, int addEle) {
        for (int i = start; i < curStart.size(); i++) {
            if (addEle < curStart.get(i))
                return i;
        }
        return curStart.size();
    }

    public BitSet getStartPoints() {
        return startPoints;
    }

    public short[] getFreqPatternInString() {
        return freqPattern;
    }

    public void setFreqPatternInString(short[] freqPatternInString) {
        this.freqPattern = freqPatternInString;
    }

    @Override
    public String getPatternInString() {
        return InteractiveToolkit.shortArrayToString(freqPattern);
    }

    @Override
    public int getPatternLength() {
        return freqPattern.length;
    }

    @Override
    public boolean isFrequentPattern() {
        return true;
    }

    @Override
    public String printPattern(int bitSize) {
        return this.toString();
    }

    public int getAllSupport() {
        return allSupport;
    }

    public void setAllSupport(int allSupport) {
        this.allSupport = allSupport;
    }

    @Override
    public int compareTo(Object o) {
        short [] objectPattern = ((FreqPatternWrapup) o).freqPattern;
        short [] curPattern = this.freqPattern;
        int toIndex = Math.min(curPattern.length, objectPattern.length);
        for(int i = 0; i < toIndex; i++) {
            if(objectPattern[i] > curPattern[i])
                return -1;
            else if(objectPattern[i] < curPattern[i])
                return 1;
        }
        if(objectPattern.length > toIndex)
            return -1;
        else if(curPattern.length > toIndex)
            return 1;
        else
            return 0;
    }
}
