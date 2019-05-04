package interactive.index;

import inputdata.InputData;
import inputdata.LocalParameter;
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

    /**
     * Build index on the inputFile, given parameters
     */
    public void buildIndex(LocalParameter localParameter) {
        this.localParameterForStorage = localParameter;
        long startTime = System.currentTimeMillis();
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
                ArrayList<FreqPatternWrapup> freqPatternWithBMs = freqSeqTree.getFreqPatternList();
//                BasicStatistics.totalNumPatterns += freqPatternWithBMs.size();
//                BasicStatistics.seqWithPatterns += 1;
                if (freqSeqTree.getFreqPatternList() != null) {
                    if (freqPatternWithBMs.size() > 0)
                        freqSeqTree.buildFreqPatternTrie(freqPatternWithBMs);
//                    for(FreqPatternWrapup freqPattern: freqPatternWithBMs){
//                        BasicStatistics.totalSizePattern += freqPattern.getPatternLength();
//                        BasicStatistics.totalNumOcc += freqPattern.getIndependentSupport();
//                    }
                }
                this.localFreqPatternWrapUps.put(i, freqSeqTree);
            }else{
                this.localFreqPatternWrapUps.put(i, pst.getEmptyBMTree());
            }
        }
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
            for (FreqPatternWrapup pattern : freqPattern.getValue().getFreqPatternList()) {
                System.out.println(pattern.getFreqPatternInString() + "\t" + pattern.getAllSupport());
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
}
