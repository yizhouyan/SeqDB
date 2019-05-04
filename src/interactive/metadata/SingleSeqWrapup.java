package interactive.metadata;

import java.util.*;

/**
 * Save information of all the frequent patterns for one single original sequence (one single device)
 * Created by yizhouyan on 4/23/18.
 */
public class SingleSeqWrapup {
    private short [] originalSequence;
    private long [] originalTimeStamps;
    private int seqLength;
    private HashMap<Integer, ArrayList<FreqPatternWrapup>> freqPatternsMap;
    private ArrayList<Integer> patternLengths;
//    private ArrayList<FreqPatternWrapup> freqPatternList;
//    private TrieGroupNode<FreqPatternWrapup> freqPatternTrie;
//    private FST freqPatternFST;

    public SingleSeqWrapup(int seqLength, short [] originalSequence, long [] originalTimeStamps) {
        this.seqLength = seqLength;
        this.freqPatternsMap = new HashMap<Integer, ArrayList<FreqPatternWrapup>>();
        this.patternLengths = new ArrayList<>();
        this.originalSequence = originalSequence;
        this.originalTimeStamps = originalTimeStamps;
    }

    public void addFreqSeq(FreqPatternWrapup single) {
        int curLen = single.getPatternLength();
        if(freqPatternsMap.containsKey(curLen))
            freqPatternsMap.get(curLen).add(single);
        else{
            patternLengths.add(curLen);
            freqPatternsMap.put(curLen, new ArrayList<FreqPatternWrapup>());
            freqPatternsMap.get(curLen).add(single);
        }
    }

    public void sortFreqPatternBySupport(){
        Collections.sort(patternLengths, Collections.reverseOrder());
        for(Map.Entry<Integer, ArrayList<FreqPatternWrapup>> freqPatternList: freqPatternsMap.entrySet()){
            Collections.sort(freqPatternList.getValue());
        }
    }

//    public void sortFreqPatternByLength(){
//        Collections.sort(freqPatternList, new Comparator<FreqPatternWrapup>(){
//            public int compare(FreqPatternWrapup s1, FreqPatternWrapup s2){
//                if(s1.getFreqPatternInString().length > s2.getFreqPatternInString().length)
//                    return -1;
//                else if(s1.getFreqPatternInString().length < s2.getFreqPatternInString().length)
//                    return 1;
//                else
//                    return 0;
//            }});
//    }

//    public void buildFreqPatternTrie(ArrayList<FreqPatternWrapup> freqPatternWithBMs){
//        this.freqPatternTrie = new TrieGroupNode<FreqPatternWrapup>();
//        for(FreqPatternWrapup freqPattern: freqPatternWithBMs){
//            this.freqPatternTrie.insert(freqPattern.getFreqPatternInString(), freqPattern);
//        }
//    }

//    public void buildFreqPatternFST(ArrayList<FreqPatternWrapup> freqPatternWithBMs){
//        ArrayList<FreqPatternWrapup> newArrayList = new ArrayList<>(freqPatternWithBMs);
//        Collections.sort(newArrayList);
//        this.freqPatternFST = new FST(newArrayList);
//    }

//    public ArrayList<FreqPatternWrapup> findAllPrefixesInTrie(short [] query){
//        return this.freqPatternTrie.findAllPrefixes(query);
//    }

//    public FreqPatternWrapup findLongestPrefixesInTrie(short [] query){
//        return this.freqPatternTrie.findLongestPrefixes(query);
//    }

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
        for(Map.Entry<Integer, ArrayList<FreqPatternWrapup>> freqPatternList: freqPatternsMap.entrySet()) {
            for (FreqPatternWrapup single : freqPatternList.getValue()) {
                System.out.println(single.toString());
            }
        }
    }

    public void printFreqSeqSet(int minSupport){
        System.out.println();
        int count = 0;
        System.out.println("Length of sequence: " + seqLength);
        for(Map.Entry<Integer, ArrayList<FreqPatternWrapup>> freqPatternList: freqPatternsMap.entrySet()) {
            for (FreqPatternWrapup single : freqPatternList.getValue()) {
//        for(FreqPatternWrapup single: freqPatternList){
                if (single.getIndependentSupport() >= minSupport) {
                    System.out.println(single.toString());
                    count++;
                }
            }
        }
//        }
        System.out.println("Number of Frequent Pattern: " + count);
    }

    public void printFreqSeqSet(int minSupport, HashMap<Short, String> dictionary){
        System.out.println();
        int count = 0;

        System.out.println("Length of sequence: " + seqLength);
        for(Map.Entry<Integer, ArrayList<FreqPatternWrapup>> freqPatternList: freqPatternsMap.entrySet()) {
            for(FreqPatternWrapup single: freqPatternList.getValue()) {
//        for(FreqPatternWrapup single: freqPatternList){
                if (single.getIndependentSupport() >= minSupport) {
                    System.out.println(single.toString(dictionary));
                    count++;
                }
            }
//        }
        }
        System.out.println("Total lengths: ");
        for(Integer i: patternLengths)
            System.out.println(i);
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

//    public ArrayList<FreqPatternWrapup> getFreqPatternList() {
//        return freqPatternList;
//    }
//
//    public void setFreqPatternList(ArrayList<FreqPatternWrapup> freqSeqSet) {
//        this.freqPatternList = freqSeqSet;
//    }


    public HashMap<Integer, ArrayList<FreqPatternWrapup>> getFreqPatternsMap() {
        return freqPatternsMap;
    }

    public void setFreqPatternsMap(HashMap<Integer, ArrayList<FreqPatternWrapup>> freqPatternsMap) {
        this.freqPatternsMap = freqPatternsMap;
    }

    public short[] getOriginalSequence() {return originalSequence; }

    public ArrayList<Integer> getPatternLengths() {
        return patternLengths;
    }

    public void setPatternLengths(ArrayList<Integer> patternLengths) {
        this.patternLengths = patternLengths;
    }
//    public TrieGroupNode<FreqPatternWrapup> getFreqPatternTrie() {
//        return freqPatternTrie;
//    }

//    public FST getFreqPatternTrie() {
//        return freqPatternFST;
//    }
//
//    public void setFreqPatternTrie(TrieGroupNode<FreqPatternWrapup> freqPatternTrie) {
//        this.freqPatternTrie = freqPatternTrie;
//    }
}
