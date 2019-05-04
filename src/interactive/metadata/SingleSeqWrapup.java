package interactive.metadata;

import interactive.index.trie.Trie;

import java.util.*;

/**
 * Save information of all the frequent patterns for one single original sequence (one single device)
 * Created by yizhouyan on 4/23/18.
 */
public class SingleSeqWrapup {
    private short [] originalSequence;
    private long [] originalTimeStamps;
    private int seqLength;
    private ArrayList<FreqPatternWrapup> freqPatternList;
    private Trie<FreqPatternWrapup> freqPatternTrie;

    public SingleSeqWrapup(int seqLength, short [] originalSequence, long [] originalTimeStamps) {
        this.seqLength = seqLength;
        this.freqPatternList = new ArrayList<FreqPatternWrapup>();
        this.originalSequence = originalSequence;
        this.originalTimeStamps = originalTimeStamps;
    }

    public void addFreqSeq(FreqPatternWrapup single) {
        this.freqPatternList.add(single);
    }

    public void sortFreqPatternByLength(){
        Collections.sort(freqPatternList, new Comparator<FreqPatternWrapup>(){
            public int compare(FreqPatternWrapup s1, FreqPatternWrapup s2){
                if(s1.getFreqPatternInString().length > s2.getFreqPatternInString().length)
                    return -1;
                else if(s1.getFreqPatternInString().length < s2.getFreqPatternInString().length)
                    return 1;
                else
                    return 0;
            }});
    }


    public void buildFreqPatternTrie(ArrayList<FreqPatternWrapup> freqPatternWithBMs){
        this.freqPatternTrie = new Trie<FreqPatternWrapup>();
        for(FreqPatternWrapup freqPattern: freqPatternWithBMs){
            this.freqPatternTrie.insert(freqPattern.getFreqPatternInString(), freqPattern);
        }
    }

    public ArrayList<FreqPatternWrapup> findAllPrefixesInTrie(short [] query){
        return this.freqPatternTrie.findAllPrefixes(query);
    }

    public FreqPatternWrapup findLongestPrefixesInTrie(short [] query){
        return this.freqPatternTrie.findLongestPrefixes(query);
    }

//    public FreqPatternWrapup findLongestPrefixesInTrie(short [] query){
//        return this.freqPatternFST.lookupKey(query);
//    }


//    public SingleSeqWrapup clone(){
//        SingleSeqWrapup newWrapUp = new SingleSeqWrapup(this.seqLength, this.originalSequence);
//        ArrayList<FreqPatternWrapup> newSeqs = new ArrayList<FreqPatternWrapup>();
//        for(FreqPatternWrapup single: this.freqPatternList){
//            newSeqs.add(single.clone());
//        }
//        newWrapUp.setFreqPatternList(newSeqs);
////        newWrapUp.setFreqPatternTrie(this.freqPatternTrie);
//        return newWrapUp;
//    }

    public void printAllPatternSet(){
        System.out.println();
        System.out.println("Length of sequence: " + seqLength);
        for(FreqPatternWrapup single: freqPatternList){
            System.out.println(single.toString());
        }
    }

    public void printFreqSeqSet(int minSupport){
        System.out.println();
        int count = 0;
        System.out.println("Length of sequence: " + seqLength);
        for(FreqPatternWrapup single: freqPatternList){
            if(single.getAllSupport() >= minSupport) {
                System.out.println(single.toString());
                count++;
            }
        }
        System.out.println("Number of Frequent Pattern: " + count);
    }

    public void printFreqSeqSet(int minSupport, HashMap<Short, String> dictionary){
        System.out.println();
        int count = 0;
        System.out.println("Length of sequence: " + seqLength);
        for(FreqPatternWrapup single: freqPatternList){
            if(single.getAllSupport() >= minSupport) {
                System.out.println(single.toString(dictionary));
                count++;
            }
        }
        System.out.println("Number of Frequent Pattern: " + count);
    }

    public long[] getOriginalTimeStamps() {
        return originalTimeStamps;
    }

    public void setOriginalTimeStamps(long[] originalTimeStamps) {
        this.originalTimeStamps = originalTimeStamps;
    }

    public int getSeqLength() {
        return seqLength;
    }

    public void setSeqLength(int seqLength) {
        this.seqLength = seqLength;
    }

    public ArrayList<FreqPatternWrapup> getFreqPatternList() {
        return freqPatternList;
    }

    public void setFreqPatternList(ArrayList<FreqPatternWrapup> freqSeqSet) {
        this.freqPatternList = freqSeqSet;
    }

    public short[] getOriginalSequence() {return originalSequence; }

    public Trie<FreqPatternWrapup> getFreqPatternTrie() {
        return freqPatternTrie;
    }

    public void setFreqPatternTrie(Trie<FreqPatternWrapup> freqPatternTrie) {
        this.freqPatternTrie = freqPatternTrie;
    }

}
