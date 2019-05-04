package interactive.index;

import inputdata.LocalParameter;

import interactive.index.grouptrie.TrieGroup;
import interactive.index.singletrie.Trie;
import interactive.metadata.Constants;
import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.QueryResult;
import interactive.metadata.SingleSeqWrapup;
import interactive.mining.prefixspan.PrefixSpanTool;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by yizhouyan on 8/11/18.
 */
public class SequenceStorageTest {
    public SequenceStorage sequenceStorage;

    void setup() {
        String inputFile = "../data/realdata/oneSequenceTS.csv";
        sequenceStorage = new SequenceStorage();
        sequenceStorage.readInputDataFromFile(inputFile);
        sequenceStorage.setLocalParameterForStorage(new LocalParameter(2, 0, 5,
                5000, 60000));
    }

    @Test
    public void buildIndex() {
        setup();
        for (int i = 0; i < sequenceStorage.getInputData().getInputStringArray().size(); i++) {
            if (this.sequenceStorage.getInputData().getInputStringArray().get(i).length > Constants.SHORT_MAX_LIMIT) {
                System.out.println("Sequence too long: " + this.sequenceStorage.getInputData().getInputStringArray().get(i).length);
            }
            PrefixSpanTool pst = new PrefixSpanTool(sequenceStorage.getInputData().getInputStringArray().get(i),
                    sequenceStorage.getInputData().getInputTimeStamp().get(i),
                    sequenceStorage.getLocalParameterForStorage());
            pst.prefixSpanCalculate();
            HashMap<Short, String> dictionary = sequenceStorage.getInputData().getDictionaryShortToString();
            SingleSeqWrapup freqSeqTree = pst.generateBitMapTree();
            freqSeqTree.printFreqSeqSet(0, dictionary);
        }
    }

    void setup_2(){
        String inputFile = "../data/realdata/5sequenceTS.csv";
        sequenceStorage = new SequenceStorage();
        sequenceStorage.readInputDataFromFile(inputFile);
        sequenceStorage.setLocalParameterForStorage(new LocalParameter(2, 0, 5,
                5000, 60000));
    }

    @Test
    public void buildIndex_for5() {
        setup_2();
        sequenceStorage.setFreqPatternTrie(new Trie());
        for (int i = 0; i < sequenceStorage.getInputData().getInputStringArray().size(); i++) {
            if (this.sequenceStorage.getInputData().getInputStringArray().get(i).length > Constants.SHORT_MAX_LIMIT) {
                System.out.println("Sequence too long: " + this.sequenceStorage.getInputData().getInputStringArray().get(i).length);
            }
            PrefixSpanTool pst = new PrefixSpanTool(sequenceStorage.getInputData().getInputStringArray().get(i),
                    sequenceStorage.getInputData().getInputTimeStamp().get(i),
                    sequenceStorage.getLocalParameterForStorage());
            pst.prefixSpanCalculate();
            HashMap<Short, String> dictionary = sequenceStorage.getInputData().getDictionaryShortToString();
            SingleSeqWrapup freqSeqTree = pst.generateBitMapTree();
            freqSeqTree.printFreqSeqSet(0);
            if (freqSeqTree.getFreqPatternsMap() != null) {
                sequenceStorage.getFreqPatternTrie().markDeviceInTrie(i);
                for(Map.Entry<Integer, ArrayList<FreqPatternWrapup>> freqPatternList: freqSeqTree.getFreqPatternsMap().entrySet()) {
                    for (FreqPatternWrapup freqPattern : freqPatternList.getValue()) {
                        sequenceStorage.getFreqPatternTrie().insert(freqPattern.getFreqPatternInString(), freqPattern, i);
                    }
                }
            }
        }
        this.sequenceStorage.getFreqPatternTrie().findLongestPrefixes(new short[]{0,1,2,3,4}).printTrieNodeContent();
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> freqPatternWrapupHashMap =
                this.sequenceStorage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(new short[]{0,1,2,3,4}, bestMatchStartPoints);
        for(Map.Entry<Integer, FreqPatternWrapup> freqPatternWrapupEntry: freqPatternWrapupHashMap.entrySet()){
            System.out.println(freqPatternWrapupEntry.getValue().getPatternInString());
            int bestMatchStartPoint = 0;
            if(bestMatchStartPoints.containsKey(freqPatternWrapupEntry.getKey()))
                bestMatchStartPoint = bestMatchStartPoints.get(freqPatternWrapupEntry.getKey());
            System.out.println(bestMatchStartPoint);
        }
    }


    void setup_3(){
        String inputFile = "../data/realdata/5sequenceTS.csv";
        sequenceStorage = new SequenceStorage();
        sequenceStorage.readInputDataFromFile(inputFile);
        sequenceStorage.setLocalParameterForStorage(new LocalParameter(10, 0, 5,
                5000, 60000));
    }

//    @Test
//    void buildIndex_for5_groupTrie() {
//        setup_3();
//        sequenceStorage.setFreqPatternTrieGroup(new TrieGroup());
//        ArrayList<HashSet<String>> featureGroup = new ArrayList<>();
//        ArrayList<Integer> numDevicesPerGroup = new ArrayList<>();
//        for (int i = 0; i < sequenceStorage.getInputData().getInputStringArray().size(); i++) {
//            PrefixSpanTool pst = new PrefixSpanTool(sequenceStorage.getInputData().getInputStringArray().get(i),
//                    sequenceStorage.getInputData().getInputTimeStamp().get(i),
//                    sequenceStorage.getLocalParameterForStorage());
//            pst.prefixSpanCalculate();
//            HashMap<Short, String> dictionary = sequenceStorage.getInputData().getDictionaryShortToString();
//            if(pst.getTotalFrequentSeqs().size()>0) {
//                SingleSeqWrapup freqSeqTree = pst.generateBitMapTree();
//            freqSeqTree.printFreqSeqSet(0);
//
//                if (freqSeqTree.getFreqPatternList() != null) {
//                    sequenceStorage.getFreqPatternTrieGroup().insertDeviceToTrie(i, freqSeqTree.getFreqPatternList(),
//                            featureGroup, numDevicesPerGroup);
//                }
//            }
//        }
//        System.out.println("Size of Trie: " + sequenceStorage.getFreqPatternTrieGroup().getNumOfTrie());
//        for(int oneGroup: numDevicesPerGroup)
//            System.out.println(oneGroup);
//        HashMap<Integer, FreqPatternWrapup> freqPatternWrapupHashMap =
//                this.sequenceStorage.getFreqPatternTrieGroup().findMatchFreqPatternForDevices(new short[]{0,1,2,3,4});
//        for(Map.Entry<Integer, FreqPatternWrapup> freqPatternWrapupEntry: freqPatternWrapupHashMap.entrySet()){
//            System.out.println(freqPatternWrapupEntry.getValue().getPatternInString());
//        }
//    }

}