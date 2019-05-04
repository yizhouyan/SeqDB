package interactive.index;

import inputdata.InputData;
import inputdata.LocalParameter;
//import interactive.index.grouptrie.TrieGroup;
import interactive.index.singletrie.Trie;
import interactive.metadata.*;
import interactive.mining.prefixspan.PrefixSpanTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by yizhouyan on 4/20/18.
 */
public class SequenceStorage {
    // local parameter setting when building the index/storage
    private LocalParameter localParameterForStorage;
    private InputData inputData;
    private HashMap<Integer, SingleSeqWrapup> localFreqPatternWrapUps;
    private Trie freqPatternTrie;
//    private TrieGroup freqPatternTrieGroup;

    public SequenceStorage() {
    }

    /**
     * Read input data from file
     *
     * @param inputFile
     */
    public void readInputDataFromFile(String inputFile) {
        this.inputData = new InputData(inputFile);
    }

    public void readInputDataFromFileWithMeta(String inputFile, String metaFile) {
        this.inputData = new InputData(inputFile, metaFile);
    }

    public void buildFreqPatternTrie(){

    }
    /**
     * Build index on the inputFile, given parameters
     */
    public void buildIndex(LocalParameter localParameter) {
        this.localParameterForStorage = localParameter;
        long startTime = System.currentTimeMillis();
//        this.freqPatternTrieGroup = new TrieGroup();
        this.freqPatternTrie = new Trie();
        // start building bitmap representations on each sequnce
        this.localFreqPatternWrapUps = new HashMap<Integer, SingleSeqWrapup>();
        ArrayList<HashSet<String>> featureGroup = new ArrayList<>();
        ArrayList<Integer> numDevicesPerGroup = new ArrayList<>();
        for (int i = 0; i < this.inputData.getInputStringArray().size(); i++) {
//            BasicStatistics.totalNumInputEvents += this.inputData.getInputStringArray().get(i).length;
            PrefixSpanTool pst = new PrefixSpanTool(this.inputData.getInputStringArray().get(i),
                    this.inputData.getInputTimeStamp().get(i),
                    localParameterForStorage);
            pst.prefixSpanCalculate();
            if(pst.getTotalFrequentSeqs().size()>0) {
                SingleSeqWrapup freqSeqTree = pst.generateBitMapTree();
                HashMap<Integer, ArrayList<FreqPatternWrapup>> freqPatternWithBMs = freqSeqTree.getFreqPatternsMap();
//                BasicStatistics.totalNumPatterns += freqPatternWithBMs.size();
//                BasicStatistics.seqWithPatterns += 1;
                if (freqSeqTree.getFreqPatternsMap() != null) {
//                    freqPatternTrieGroup.insertDeviceToTrie(i, freqPatternWithBMs, featureGroup, numDevicesPerGroup);
                    this.freqPatternTrie.markDeviceInTrie(i);
                    for(Map.Entry<Integer, ArrayList<FreqPatternWrapup>> freqPatternList: freqPatternWithBMs.entrySet()) {
                        for (FreqPatternWrapup freqPattern : freqPatternList.getValue()) {
                            this.freqPatternTrie.insert(freqPattern.getFreqPatternInString(), freqPattern, i);
                        }
                    }
//                    for(FreqPatternWrapup freqPattern: freqPatternWithBMs){
//                        BasicStatistics.totalSizePattern += freqPattern.getPatternLength();
//                        BasicStatistics.totalNumOcc += freqPattern.getIndependentSupport();
//                    }
                    // build trie for frequent patterns in the sequence
//                    if (freqPatternWithBMs.size() > 0) {
//                        freqSeqTree.buildFreqPatternFST(freqPatternWithBMs);
//                    }
//                        freqSeqTree.buildFreqPatternTrie(freqPatternWithBMs);
//                    System.out.println("Tree Root Size: " + freqPatternTrie.getRoot().getLinksToChildren().size());
                }
                this.localFreqPatternWrapUps.put(i, freqSeqTree);
            }else{
                this.localFreqPatternWrapUps.put(i, pst.getEmptyBMTree());
            }
            if(i % 1000 == 0)
                System.out.println(i + " Finished");
        }
//        System.out.println("Number of Tries: " + this.freqPatternTrieGroup.getNumOfTrie());
        System.out.println("Building up index takes " + (System.currentTimeMillis() - startTime) * 1.0 / 1000 + " seconds");
//        BasicStatistics.printBasicStatistics();
    }

    public void printSequenceStorage() {
        System.out.println();
        System.out.println("-------------------------------Start Print Sequence Storage--------------------------------");
        for (Map.Entry<Integer, SingleSeqWrapup> freqPattern : this.localFreqPatternWrapUps.entrySet()) {
            freqPattern.getValue().printFreqSeqSet(this.localParameterForStorage.getLocalSupport());
        }
        System.out.println("-------------------------------End Print Sequence Storage--------------------------------");
        System.out.println();
    }

    public void printSimpleSequenceStorage() {
        System.out.println();
        System.out.println("-------------------------------Start Print Sequence Storage--------------------------------");
        for (Map.Entry<Integer, SingleSeqWrapup> freqPattern : this.localFreqPatternWrapUps.entrySet()) {
            for(Map.Entry<Integer, ArrayList<FreqPatternWrapup>> freqPatternList: freqPattern.getValue().getFreqPatternsMap().entrySet()) {
                for (FreqPatternWrapup pattern : freqPatternList.getValue()) {
                    System.out.println(pattern.getFreqPatternInString() + "\t" + pattern.getAllSupport());
                }
            }
        }
        System.out.println("-------------------------------End Print Sequence Storage--------------------------------");
        System.out.println();
    }

    public LocalParameter getLocalParameterForStorage() {
        return localParameterForStorage;
    }

    public void setLocalParameterForStorage(LocalParameter localParameterForStorage) {
        this.localParameterForStorage = localParameterForStorage;
    }

    public InputData getInputData() {
        return inputData;
    }

    public void setInputData(InputData inputData) {
        this.inputData = inputData;
    }

    public HashMap<Integer, SingleSeqWrapup> getLocalFreqPatternWrapUps() {
        return localFreqPatternWrapUps;
    }

    public void setLocalFreqPatternWrapUps(HashMap<Integer, SingleSeqWrapup> localFreqPatternWrapUps) {
        this.localFreqPatternWrapUps = localFreqPatternWrapUps;
    }

    public Trie getFreqPatternTrie() {
        return freqPatternTrie;
    }

    public void setFreqPatternTrie(Trie freqPatternTrie) {
        this.freqPatternTrie = freqPatternTrie;
    }


//    public TrieGroup getFreqPatternTrieGroup() {
//        return freqPatternTrieGroup;
//    }
//
//    public void setFreqPatternTrieGroup(TrieGroup freqPatternTrieGroup) {
//        this.freqPatternTrieGroup = freqPatternTrieGroup;
//    }
}
