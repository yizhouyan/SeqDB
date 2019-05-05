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
 * Created by yizhouyan on 4/25/18.
 */
public class JoinTwoOccurrencesTestLong {
    private SequenceStorage storage;
    private SingleSeqWrapup singleSequence;
    private int sequenceLength;

    void setUp_1() {
        this.storage = new SequenceStorage();

        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65548];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[65533] = 1;
        oneSeq[65534] = 2;
        oneSeq[65535] = 3;
        oneSeq[65536] = 4;
        oneSeq[65537] = 5;
        oneSeq[65538] = 2;
        oneSeq[65539] = 3;
        oneSeq[65540] = 4;
        oneSeq[65541] = 1;
        oneSeq[65542] = 2;
        oneSeq[65543] = 4;
        oneSeq[65544] = 5;
        oneSeq[65545] = 3;
        oneSeq[65546] = 4;
        oneSeq[65547] = 5;
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        storage.setInputData(input);
        input.setInputTimeStamp(inputTS);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(oneSeq.length);
    }

    @Test
    public void queryOnSingleSequenceTest_JoinTwoMatch_backNext(){
        setUp_1();
        short [] curQuery = {1,2,3,4,5};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, curQuery);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(curQuery, bestMatchStartPoints);
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
        short [] oneSeq = new short[65548];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[65533] = 1;
        oneSeq[65534] = 2;
        oneSeq[65535] = 3;
        oneSeq[65536] = 3;
        oneSeq[65537] = 4;
        oneSeq[65538] = 2;
        oneSeq[65539] = 3;
        oneSeq[65540] = 4;
        oneSeq[65541] = 1;
        oneSeq[65542] = 1;
        oneSeq[65543] = 2;
        oneSeq[65544] = 5;
        oneSeq[65545] = 3;
        oneSeq[65546] = 4;
        oneSeq[65547] = 5;
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
    public void queryOnSingleSequenceTest_JoinTwoMatch_backNext_2(){
        setUp_2();
        short [] curQuery = {1,2,3,4};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, curQuery);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(curQuery, bestMatchStartPoints);
        int bestMatchStartPoint = 0;
        if(bestMatchStartPoints.containsKey(0))
            bestMatchStartPoint = bestMatchStartPoints.get(0);
        QueryResult results = query.queryOnSingleSequence(singleSequence, patternWrapupHashMap.get(0),
                bestMatchStartPoint);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),2);
    }

    void setUp_3() {
        this.storage = new SequenceStorage();

        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = new short[65548];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[3] = 4;
        oneSeq[4] = 5;
        oneSeq[65534] = 1;
        oneSeq[65536] = 2;
        oneSeq[65537] = 1;
        oneSeq[65538] = 2;
        oneSeq[65539] = 3;
        oneSeq[65540] = 4;
        oneSeq[65541] = 1;
        oneSeq[65542] = 2;
        oneSeq[65543] = 4;
        oneSeq[65544] = 5;
        oneSeq[65545] = 3;
        oneSeq[65546] = 4;
        oneSeq[65547] = 5;
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
    public void queryOnSingleSequenceTest_JoinTwoMatch_1(){
        setUp_3();
        short [] curQuery = {1,2,3,4,5};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, curQuery);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(curQuery, bestMatchStartPoints);
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
        short [] oneSeq = new short[65548];
        oneSeq[0] = 3;
        oneSeq[1] = 4;
        oneSeq[2] = 5;
        oneSeq[3] = 1;
        oneSeq[4] = 2;
        oneSeq[65536] = 1;
        oneSeq[65537] = 2;
        oneSeq[65538] = 5;
        oneSeq[65539] = 3;
        oneSeq[65540] = 4;
        oneSeq[65541] = 1;
        oneSeq[65542] = 2;
        oneSeq[65543] = 5;
        oneSeq[65544] = 5;
        oneSeq[65545] = 3;
        oneSeq[65546] = 4;

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
    public void queryOnSingleSequenceTest_JoinTwoMatch_2(){
        setUp_4();
        short [] curQuery = {1,2,3,4};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, curQuery);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(curQuery, bestMatchStartPoints);
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
        short [] oneSeq = new short[65548];
        oneSeq[0] = 1;
        oneSeq[1] = 2;
        oneSeq[2] = 3;
        oneSeq[65534] = 1;
        oneSeq[65535] = 2;
        oneSeq[65536] = 3;
        oneSeq[65537] = 4;
        oneSeq[65538] = 5;
        oneSeq[65539] = 3;
        oneSeq[65540] = 4;
        oneSeq[65546] = 4;
        oneSeq[65547] = 5;
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
    public void queryOnSingleSequenceTest_JoinTwoMatch_frontNext(){
        setUp_6();
        short [] curQuery = {1,2,3,4,5};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, curQuery);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(curQuery, bestMatchStartPoints);
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
        short [] oneSeq = {3,4,5,1,2,1,2,1,3,4,1,2,5,3,4,3,4};
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,0,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
//        singleSequence.printFreqSeqSet();
    }

    @Test
    public void queryOnSingleSequenceTest_JoinTwoMatch_nomatch(){
        setUp_5();
        short [] curQuery = {1,2,3,4};
        FPQueryWithOneTrie query = new FPQueryWithOneTrie(storage, curQuery);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
                storage.getFreqPatternTrie().findMatchFreqPatternForDevicesWithOpt(curQuery, bestMatchStartPoints);
        int bestMatchStartPoint = 0;
        if(bestMatchStartPoints.containsKey(0))
            bestMatchStartPoint = bestMatchStartPoints.get(0);
        QueryResult results = query.queryOnSingleSequence(singleSequence, patternWrapupHashMap.get(0),
                bestMatchStartPoint);
        assertEquals(results.getSupportCount(),0);
    }
}