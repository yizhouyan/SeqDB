package interactive.metadata;

import interactive.metadata.bmcontainer.BitsetContainer;
import interactive.metadata.bmcontainer.BitsetContainerIStart;
import interactive.metadata.bmcontainer.ContainerSelecter;
import interactive.metadata.bmcontainer.IBMContainer;
import interactive.mining.baseline.maximum.FreqSequence;
import interactive.mining.prefixspan.ResItemArrayPair;
import interactive.util.InteractiveToolkit;

import java.util.*;

/**
 * Created by yizhouyan on 5/27/18.
 */
public class FreqPatternWrapup implements IPattern, Comparable {

    protected short[] freqPattern;
    protected IBMContainer startPoints;
    protected int independentSupport = 0;
    protected int allSupport = 0;
    protected ArrayList<FreqPatternWrapup> superPatterns;

    public FreqPatternWrapup(String freqPatternInString) {
        String[] freqPatternSplits = freqPatternInString.split(",");
        freqPattern = new short[freqPatternSplits.length];
        for (int i = 0; i < freqPatternSplits.length; i++)
            freqPattern[i] = Short.parseShort(freqPatternSplits[i]);
        this.superPatterns = new ArrayList<FreqPatternWrapup>();
    }

    public FreqPatternWrapup(short[] freqPattern, IBMContainer startPoints) {
        this.freqPattern = freqPattern.clone();
        this.startPoints = startPoints;
    }

    public ArrayList<Integer> getIntsFromResItemPair(ArrayList<ResItemArrayPair> currentItemArrayPair){
        ArrayList<Integer> allNoneZeros = new ArrayList<>();
        for (ResItemArrayPair itemPair : currentItemArrayPair) {
            allNoneZeros.addAll(itemPair.index);
        }
        return allNoneZeros;
    }

    public void generateBitSet(ArrayList<Integer> currentSetPoints, int sequenceLength) {
        this.startPoints = ContainerSelecter.generateBitSet(currentSetPoints, sequenceLength,
                independentSupport);
    }


//    public void addToBitSets(BitSet currentBS, int startIndex,
//                             short itemGapsForEachFS, short seqGapsForEachFS,
//                             HashMap<SingleBitSet, ArrayList<Integer>> startPointsForSingleBitSet){
//        if(bitSet == null){
//            this.bitSet = new ArrayList<>();
//        }
//        // first find if there is a same bitmap available
//        for(SingleBitSet prevBS: bitSet){
//            if(prevBS.getBitset().equals(currentBS)){
//                startPointsForSingleBitSet.get(prevBS).add(startIndex);
//                return;
//            }
//        }
//        // cannot find match, create a new bitmap candidate
//        SingleBitSet newSingleBitSet = new SingleBitSet(currentBS, itemGapsForEachFS, seqGapsForEachFS);
//        this.bitSet.add(newSingleBitSet);
//        ArrayList<Integer> newStartPoints = new ArrayList<>();
//        newStartPoints.add(startIndex);
//        startPointsForSingleBitSet.put(newSingleBitSet, newStartPoints);
//    }


    public String toString() {
        String str = InteractiveToolkit.shortArrayToString(freqPattern) + "\nIndependent Support:" + independentSupport
                +"\nAll Support:" + allSupport
                + "\nStart Points: \n";
        if (this.startPoints != null) {
            str += this.startPoints.printBitset() + "\n";
        }
        if (superPatterns.size() > 0) {
            for(FreqPatternWrapup superPattern: superPatterns)
                str += "superpattern:" + InteractiveToolkit.shortArrayToString(superPattern.freqPattern) +
                        "\tSupport:" + superPattern.getIndependentSupport() +"\n";
        }
        return str;
    }

    public String toString(HashMap<Short, String> dictionary) {
        String str = InteractiveToolkit.shortArrayToString(freqPattern, dictionary) + "\nIndependent Support:" + independentSupport
                +"\nAll Support:" + allSupport
                + "\nStart Points: \n";
        if (this.startPoints != null) {
            str += this.startPoints.printBitset() + "\n";
        }
        if (superPatterns.size() > 0) {
            for(FreqPatternWrapup superPattern: superPatterns)
                str += "superpattern:" + InteractiveToolkit.shortArrayToString(superPattern.freqPattern, dictionary) +
                        "\tSupport:" + superPattern.getIndependentSupport() +"\n";
        }
        return str;
    }

//    public FreqPatternWrapup clone() {
//        FreqPatternWrapup newWrapUp = new FreqPatternWrapup(this.freqPattern);
//        newWrapUp.setSupportCount(this.supportCount);
//        // copy bit sets
//        ArrayList<SingleBitSet> newBitSets = new ArrayList<>();
//        for(SingleBitSet single: this.bitSet){
//            newBitSets.add(single.clone());
//        }
//        newWrapUp.setBitSet(newBitSets);
//        newWrapUp.setSuperPattern(this.superPattern);
//        return newWrapUp;
//    }


//    public QueryResultCache getIndependentQueryResult(){
//        return new QueryResultCache(independentSupport, startPoints, freqPattern.length);
//    }

    public QueryResultCache getCompleteQueryResult(int seqLength, short [] originalSeq, long [] originalTS,
                                                   int itemGap, int seqGap, long itemGapTS, long seqGapTS){
        BitSet curStartPoints = startPoints.createBitMapContainer(seqLength);
        for(FreqPatternWrapup superPattern: superPatterns){
            curStartPoints = superPattern.getStartPoints().union(freqPattern, superPattern.getFreqPatternInString(),
                    curStartPoints, originalSeq, originalTS,
                    itemGap, seqGap, itemGapTS, seqGapTS);
        }
        return new QueryResultCache(curStartPoints.cardinality(), curStartPoints, freqPattern.length);
    }

    /**
     * Get all start points, if super pattern for BC is ABC, return start point of ABC and find out new start points latter.
     * @param seqLength
     * @return
     */
//    public QueryResultCache getAllStartPoints(int seqLength){
//        BitSet curStartPoints = startPoints.createBitMapContainer(seqLength);
//        for(FreqPatternWrapup superPattern: superPatterns){
//            curStartPoints = superPattern.getStartPoints().union(curStartPoints);
//        }
//        return new QueryResultCache(curStartPoints.cardinality(), curStartPoints, freqPattern.length);
//    }

    public BitSet getAllStartPoints(int seqLength, HashMap<short[], Integer> exisitingFreqPattern,
                                          short [] originalSeq, long [] originalTS,
                                          int itemGap, int seqGap, long itemGapTS, long seqGapTS, int curMineSupport){

        HashSet<short []> superPatternFlat = new HashSet<>();

        for(FreqPatternWrapup superPattern: superPatterns){
            if(exisitingFreqPattern.containsKey(superPattern.getFreqPatternInString())) {
                for (FreqPatternWrapup superFlat: superPattern.getSuperPatterns())
                    superPatternFlat.add(superFlat.getFreqPatternInString());
                superPatternFlat.add(superPattern.getFreqPatternInString());
            }
        }


        int sumStartPoints = independentSupport;
        ArrayList<FreqPatternWrapup> validSuperPatterns = new ArrayList<>();
        for(FreqPatternWrapup superPattern: superPatterns){
            if(!superPatternFlat.contains(superPattern.getFreqPatternInString())) {
                sumStartPoints += superPattern.independentSupport;
                validSuperPatterns.add(superPattern);
            }
        }
        if(sumStartPoints < curMineSupport)
            return null;
        BitSet curStartPoints = startPoints.createBitMapContainer(seqLength);
        for(FreqPatternWrapup superPattern: validSuperPatterns)
            curStartPoints = superPattern.getStartPoints().union(freqPattern, superPattern.getFreqPatternInString(),
                curStartPoints, originalSeq, originalTS,
                itemGap, seqGap, itemGapTS, seqGapTS);
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

    public IBMContainer getStartPoints() {
        return startPoints;
    }

    public short[] getFreqPatternInString() {
        return freqPattern;
    }

    public void setFreqPatternInString(short[] freqPatternInString) {
        this.freqPattern = freqPatternInString;
    }

    public ArrayList<FreqPatternWrapup> getSuperPatterns() {
        return superPatterns;
    }

    public void setSuperPatterns(ArrayList<FreqPatternWrapup> superPatterns) {
        this.superPatterns = superPatterns;
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

    public int getIndependentSupport() {
        return independentSupport;
    }

    public void setIndependentSupport(int independentSupport) {
        this.independentSupport = independentSupport;
    }

    public int getAllSupport() {
        return allSupport;
    }

    public void setAllSupport(int allSupport) {
        this.allSupport = allSupport;
    }

//    @Override
//    public int compareTo(Object o) {
//        short [] objectPattern = ((FreqPatternWrapup) o).freqPattern;
//        short [] curPattern = this.freqPattern;
//        int toIndex = Math.min(curPattern.length, objectPattern.length);
//        for(int i = 0; i < toIndex; i++) {
//            if(objectPattern[i] > curPattern[i])
//                return -1;
//            else if(objectPattern[i] < curPattern[i])
//                return 1;
//        }
//        if(objectPattern.length > toIndex)
//            return -1;
//        else if(curPattern.length > toIndex)
//            return 1;
//        else
//            return 0;
//    }

    @Override
    public int compareTo(Object o) {
        int otherSupport = ((FreqPatternWrapup) o).allSupport;
        int curSupport = this.allSupport;

        if(curSupport > otherSupport)
            return -1;
        else if(otherSupport > curSupport)
            return 1;
        else
            return 0;
    }
}
