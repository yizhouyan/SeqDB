package interactive.query;

import inputdata.InputData;
import inputdata.LocalParameter;
import interactive.index.SequenceStorage;
import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.QueryResult;
import interactive.metadata.SingleSeqWrapup;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Created by yizhouyan on 4/28/18.
 */
public class JoinPatternElementTestLong {
    private SequenceStorage storage;
    private SingleSeqWrapup singleSequence;
    private int sequenceLength;

    void setUp_1() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65540];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[65535] = 1;
        oneSeq[65536] = 2;
        oneSeq[65537] = 3;
        oneSeq[65538] = 4;
        oneSeq[65539] = 2;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);

        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(oneSeq.length);
    }

    @Test
    public void joinPatternElementTest_frontFreqBackEle_twoContainer(){
        setUp_1();
        short [] oneSeq = {1,2,3,4};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(oneSeq, bestMatchStartPoints);
        int bestMatchStartPoint = 0;
        if(bestMatchStartPoints.containsKey(0))
            bestMatchStartPoint = bestMatchStartPoints.get(0);
        QueryResult results = query.queryOnSingleSequence(singleSequence, patternWrapupHashMap.get(0),
                bestMatchStartPoint);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
    }

    public void setUp_10() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65541];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[65533] = 1;
        oneSeq[65534] = 2;
        oneSeq[65535] = 3;
        oneSeq[65537] = 4;
        oneSeq[65540] = 2;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);

        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,0,10,5000, 60000));
        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(oneSeq.length);
    }


    void setUp_3() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65540];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[65535] = 1;
        oneSeq[65536] = 2;
        oneSeq[65537] = 4;
        oneSeq[65538] = 1;
        oneSeq[65539] = 2;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(oneSeq.length);
    }

    @Test
    public void joinPatternElementTest_frontFreqBackEle_twoContainer_hasnextStart(){
        setUp_3();
        short [] oneSeq = {1,2,4};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(oneSeq, bestMatchStartPoints);
        int bestMatchStartPoint = 0;
        if(bestMatchStartPoints.containsKey(0))
            bestMatchStartPoint = bestMatchStartPoints.get(0);
        QueryResult results = query.queryOnSingleSequence(singleSequence, patternWrapupHashMap.get(0),
                bestMatchStartPoint);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
    }

    void setUp_2() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65540];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
//        oneSeq[65535] = 1;
        oneSeq[65536] = 1;
        oneSeq[65537] = 2;
        oneSeq[65538] = 4;
        oneSeq[65539] = 1;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(oneSeq.length);
    }

    @Test
    public void joinPatternElementTest_frontFreqBackEle_oneContainer(){
        setUp_2();
        short [] oneSeq = {1,2,4};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(oneSeq, bestMatchStartPoints);
        int bestMatchStartPoint = 0;
        if(bestMatchStartPoints.containsKey(0))
            bestMatchStartPoint = bestMatchStartPoints.get(0);
        QueryResult results = query.queryOnSingleSequence(singleSequence, patternWrapupHashMap.get(0),
                bestMatchStartPoint);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
    }

    void setUp_4() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65540];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[3] = 3;
        oneSeq[65534] = 1;
        oneSeq[65535] = 2;
        oneSeq[65537] = 4;
        oneSeq[65539] = 1;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(oneSeq.length);
    }

    @Test
    public void joinPatternElementTest_frontFreqBackEle_twoContainer_hasnextStart_2(){
        setUp_4();
        short [] oneSeq = {1,2,4};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(oneSeq, bestMatchStartPoints);
        int bestMatchStartPoint = 0;
        if(bestMatchStartPoints.containsKey(0))
            bestMatchStartPoint = bestMatchStartPoints.get(0);
        QueryResult results = query.queryOnSingleSequence(singleSequence, patternWrapupHashMap.get(0),
                bestMatchStartPoint);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
    }

    void setUp_5() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65540];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
//        oneSeq[65535] = 1;
        oneSeq[65536] = 5;
        oneSeq[65537] = 1;
        oneSeq[65538] = 2;
        oneSeq[65539] = 3;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(oneSeq.length);
    }

    @Test
    public void joinPatternElementTest_frontEleBackFreq_OneContainer(){
        setUp_5();
        short [] oneSeq = {5,1,2,3};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(oneSeq, bestMatchStartPoints);
        int bestMatchStartPoint = 0;
        if(bestMatchStartPoints.containsKey(0))
            bestMatchStartPoint = bestMatchStartPoints.get(0);
        QueryResult results = query.queryOnSingleSequence(singleSequence, patternWrapupHashMap.get(0),
                bestMatchStartPoint);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
    }

    void setUp_6() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65540];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[65534] = 5;
        oneSeq[65535] = 1;
        oneSeq[65536] = 2;
        oneSeq[65537] = 3;
        oneSeq[65538] = 2;
        oneSeq[65539] = 3;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(oneSeq.length);
    }

    @Test
    public void joinPatternElementTest_frontEleBackFreq_twoContainers(){
        setUp_6();
        short [] oneSeq = {5,1,2,3};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(oneSeq, bestMatchStartPoints);
        int bestMatchStartPoint = 0;
        if(bestMatchStartPoints.containsKey(0))
            bestMatchStartPoint = bestMatchStartPoints.get(0);
        QueryResult results = query.queryOnSingleSequence(singleSequence, patternWrapupHashMap.get(0),
                bestMatchStartPoint);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
    }

    void setUp_7() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65541];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[65534] = 5;
        oneSeq[65535] = 1;
        oneSeq[65536] = 2;
        oneSeq[65537] = 3;
        oneSeq[65538] = 1;
        oneSeq[65539] = 2;
        oneSeq[65540] = 3;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(oneSeq.length);
    }

    @Test
    public void joinPatternElementTest_frontEleBackFreq_twoContainers_2(){
        setUp_7();
        short [] oneSeq = {5,1,2,3};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(oneSeq, bestMatchStartPoints);
        int bestMatchStartPoint = 0;
        if(bestMatchStartPoints.containsKey(0))
            bestMatchStartPoint = bestMatchStartPoints.get(0);
        QueryResult results = query.queryOnSingleSequence(singleSequence, patternWrapupHashMap.get(0),
                bestMatchStartPoint);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
    }

    void setUp_8() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65542];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[65534] = 5;
        oneSeq[65536] = 1;
        oneSeq[65537] = 2;
        oneSeq[65538] = 3;
        oneSeq[65539] = 1;
        oneSeq[65540] = 2;
        oneSeq[65541] = 3;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(oneSeq.length);
    }

    @Test
    public void joinPatternElementTest_frontEleBackFreq_twoContainers_3(){
        setUp_8();
        short [] oneSeq = {5,1,2,3};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(oneSeq, bestMatchStartPoints);
        int bestMatchStartPoint = 0;
        if(bestMatchStartPoints.containsKey(0))
            bestMatchStartPoint = bestMatchStartPoints.get(0);
        QueryResult results = query.queryOnSingleSequence(singleSequence, patternWrapupHashMap.get(0),
                bestMatchStartPoint);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
    }

    void setUp_mix() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65547];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[3] = 4;
        oneSeq[65534] = 1;
        oneSeq[65536] = 5;
        oneSeq[65537] = 2;
        oneSeq[65538] = 1;
        oneSeq[65539] = 2;
        oneSeq[65540] = 3;
        oneSeq[65541] = 6;
        oneSeq[65542] = 3;
        oneSeq[65543] = 4;
        oneSeq[65544] = 1;
        oneSeq[65545] = 2;
        oneSeq[65546] = 3;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(oneSeq.length);
    }

    @Test
    public void joinPatternElementTest_frontEleBackFreq_mix(){
        setUp_mix();
        short [] oneSeq = {5,1,2,3,6,3,4};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(oneSeq, bestMatchStartPoints);
        int bestMatchStartPoint = 0;
        if(bestMatchStartPoints.containsKey(0))
            bestMatchStartPoint = bestMatchStartPoints.get(0);
        QueryResult results = query.queryOnSingleSequence(singleSequence, patternWrapupHashMap.get(0),
                bestMatchStartPoint);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
//        assertEquals((int)(results.getAllStartPoints().get(0)), 5);
    }

    void setUp_9() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65538];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[3] = 4;
        oneSeq[65534] = 5;
        oneSeq[65536] = 6;
        oneSeq[65537] = 7;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
//        singleSequence.printFreqSeqSet();
    }


    @Test
    public void noFrequentElementTest(){
        setUp_9();
        short [] oneSeq = {1,3,4};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(oneSeq, bestMatchStartPoints);
        assertEquals(patternWrapupHashMap.size(), 0);
        SequenceScan singleScan = new SequenceScan(storage.getInputData().getInputStringArray().get(0),
                storage.getInputData().getInputTimeStamp().get(0),
                oneSeq, storage.getLocalParameterForStorage().getItemGap(),
                storage.getLocalParameterForStorage().getSeqGap(),
                storage.getLocalParameterForStorage().getItemGapTS(),
                storage.getLocalParameterForStorage().getSeqGapTS());
        QueryResult results = singleScan.scanSequenceForMatch();
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
        assertEquals((int)(results.getAllStartPoints().get(0)), 0);
    }
}