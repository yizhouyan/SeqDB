package interactive.metadata;

import java.util.*;

/**
 * Created by yizhouyan on 6/15/17.
 */
public class InvertedIndex {
    private int bitsetSize = 100;
    private int increaseRate = 100;
    private ArrayList<FreqPatternWrapup> freqPatternWrapupInList;
    private HashMap<Short, BitSet> invertedIndexOfFreqPatterns;

    public InvertedIndex(int bitsetSize, int increaseRate) {
        this.freqPatternWrapupInList = new ArrayList<FreqPatternWrapup>();
        this.invertedIndexOfFreqPatterns = new HashMap<Short, BitSet>();
        this.bitsetSize = bitsetSize;
    }

    public void addFreqSeqToList(ArrayList<FreqPatternWrapup> currentFreqSeqs) {
        if (freqPatternWrapupInList.size() + currentFreqSeqs.size() > bitsetSize) {
            // need to enlarge bitsets
            int previousBitSetSize = bitsetSize;
            // compute increase number
            int numIncrease = (int) Math.ceil((freqPatternWrapupInList.size() + currentFreqSeqs.size() - bitsetSize) *
                    1.0 / increaseRate);
            bitsetSize += increaseRate * numIncrease;
            HashMap<Short, BitSet> newInvertedIndexOfFS = new HashMap<Short, BitSet>();
            for (Map.Entry<Short, BitSet> entry : invertedIndexOfFreqPatterns.entrySet()) {
                BitSet newTempBitSet = new BitSet(bitsetSize);
                newTempBitSet.or(entry.getValue());
                newInvertedIndexOfFS.put(entry.getKey(), newTempBitSet);
            }
            this.invertedIndexOfFreqPatterns = newInvertedIndexOfFS;
        }

        // start adding frequent sequence to list
        int startIndex = freqPatternWrapupInList.size();
        for (FreqPatternWrapup freqPattern : currentFreqSeqs) {
            freqPatternWrapupInList.add(freqPattern);
            short[] itemsInCurFreqPattern = freqPattern.getFreqPatternInString();
            for (short item : itemsInCurFreqPattern) {
                if (!invertedIndexOfFreqPatterns.containsKey(item)) {
                    BitSet currentItemBS = new BitSet(bitsetSize);
                    invertedIndexOfFreqPatterns.put(item, currentItemBS);
                }
                invertedIndexOfFreqPatterns.get(item).set(startIndex, true);
            }
            startIndex++;
        }
    }

    /**
     * Get sequences with the same first element and all other elements 
     * @param queryPattern
     * @return
     */
    public ArrayList<FreqPatternWrapup> getSequencesWithAllElements(short[] queryPattern){
       ArrayList<FreqPatternWrapup> returnSeqList = new ArrayList<FreqPatternWrapup>();
       BitSet currentBitSet = new BitSet(bitsetSize);
       currentBitSet.set(0, bitsetSize-1, true);
       for(short item: queryPattern){
           if(invertedIndexOfFreqPatterns.containsKey(item))
               currentBitSet.and(invertedIndexOfFreqPatterns.get(item));
           else
               return returnSeqList;
       }

        for (int i = currentBitSet.nextSetBit(0); i >= 0; i = currentBitSet.nextSetBit(i+1)) {
            // if has the same first element
            returnSeqList.add(this.freqPatternWrapupInList.get(i));
            if (i == Integer.MAX_VALUE) {
                break; // or (i+1) would overflow
            }
        }
       return returnSeqList;
    }

    public String freqSeqInString() {
        String finalResult = "Frequent Sequence: \n";
        for (FreqPatternWrapup str : this.freqPatternWrapupInList) {
            finalResult += str.getPatternInString() + "\n";
        }
        return finalResult;
    }

    public String bitSetsInString() {
        String finalResults = "BitSets: \n";
        for (Map.Entry<Short, BitSet> entry : this.invertedIndexOfFreqPatterns.entrySet()) {
            finalResults += entry.getKey() + "\t" + entry.getValue() + "\n";
        }
        return finalResults;
    }

    public int getBitsetSize() {
        return bitsetSize;
    }

    public void setBitsetSize(int bitsetSize) {
        this.bitsetSize = bitsetSize;
    }

    public ArrayList<FreqPatternWrapup> getFreqPatternWrapupInList() {
        return freqPatternWrapupInList;
    }

    public void setFreqPatternWrapupInList(ArrayList<FreqPatternWrapup> freqPatternWrapupInList) {
        this.freqPatternWrapupInList = freqPatternWrapupInList;
    }

    public HashMap<Short, BitSet> getInvertedIndexOfFreqPatterns() {
        return invertedIndexOfFreqPatterns;
    }

    public void setInvertedIndexOfFreqPatterns(HashMap<Short, BitSet> invertedIndexOfFreqPatterns) {
        this.invertedIndexOfFreqPatterns = invertedIndexOfFreqPatterns;
    }
}
