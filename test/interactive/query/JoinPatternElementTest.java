package interactive.query;

import interactive.index.SequenceStorage;
import interactive.metadata.*;
import inputdata.InputData;
import inputdata.LocalParameter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by yizhouyan on 4/28/18.
 */
class JoinPatternElementTest {
    private SequenceStorage storage;
    private SingleSeqWrapup singleSequence;
    private int sequenceLength;

    void setUp_1() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = {1,2,3,4,1,2,1,2,3,1,2,5,3,5};
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
        singleSequence.printFreqSeqSet(2);
    }

    @Test
    void joinPatternElementTest_frontFreqBackEle_1(){
        setUp_1();
        short [] oneSeq = {1,2,3,4};
        FPQuery query = new FPQuery(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        QueryResult results = query.queryOnSingleSequence(singleSequence);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
        assertEquals((int)(results.getAllStartPoints().get(0)), 0);
    }

    void setUp_2() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = {1,2,3,4,1,5,2,1,2,3,1,2,3};
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
    void joinPatternElementTest_frontEleBackFreq_2(){
        setUp_2();
        short [] oneSeq = {5,1,2,3};
        FPQuery query = new FPQuery(storage, oneSeq);
        QueryResult results;
        // get pattern occurrence on each single sequence
        results = query.queryOnSingleSequence(singleSequence);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
        assertEquals((int)(results.getAllStartPoints().get(0)), 5);
    }

    void setUp_3() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = {1,2,3,4,1,5,2,1,2,3,6,3,4,1,2,3};
        inputStringArray.add(oneSeq);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
//        singleSequence.printFreqSeqSet();
    }

    @Test
    void joinPatternElementTest_frontEleBackFreq_3(){
        setUp_3();
        short [] oneSeq = {5,1,2,3,6,3,4};
        FPQuery query = new FPQuery(storage, oneSeq);
        QueryResult results = query.queryOnSingleSequence(singleSequence);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
        assertEquals((int)(results.getAllStartPoints().get(0)), 5);
    }

    void setUp_4() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = {1,2,3,4,5,6,7};
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
    void noFrequentElementTest(){
        setUp_4();
        short [] oneSeq = {1,3,4};
        SequenceScan singleScan = new SequenceScan(storage.getInputData().getInputStringArray().get(0),
                storage.getInputData().getInputTimeStamp().get(0),
                oneSeq, storage.getLocalParameterForStorage().getItemGap(),
                storage.getLocalParameterForStorage().getSeqGap(),
                storage.getLocalParameterForStorage().getItemGapTS(),
                storage.getLocalParameterForStorage().getSeqGapTS());
        QueryResult results = singleScan.scanSequenceForMatch();
        assertEquals(results.getSupportCount(),1);
        assertEquals((int)(results.getAllStartPoints().get(0)), 0);
    }

    void setUp_5() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = {1,2,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5};
        inputStringArray.add(oneSeq);

        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);

        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);
        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,1,10,5000, 60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(2);
    }

    @Test
    void joinPatternElementTest_Array(){
        setUp_5();
        short [] oneSeq = {1,2,4};
        FPQuery query = new FPQuery(storage, oneSeq);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        QueryResult results = query.queryOnSingleSequence(singleSequence);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),2);
        assertEquals((int)(results.getAllStartPoints().get(0)), 148);
    }

    @Test
    void joinPatternElementTest_Array2(){
        setUp_5();
        short [] oneSeq = {1,2,3};
        FPQuery query = new FPQuery(storage, oneSeq);

        QueryResult results = query.queryOnSingleSequence(singleSequence);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),2);
        assertEquals((int)(results.getAllStartPoints().get(0)), 0);
    }

    @Test
    void joinPatternElementTest_Array3(){
        setUp_5();
        short [] oneSeq = {1,2,3,4,5};
        FPQuery query = new FPQuery(storage, oneSeq);

        QueryResult results = query.queryOnSingleSequence(singleSequence);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
        assertEquals((int)(results.getAllStartPoints().get(0)), 296);
    }

    @Test
    void joinPatternElementTest_Array4(){
        setUp_5();
        short [] oneSeq = {1,3,4,5};
        FPQuery query = new FPQuery(storage, oneSeq);

        QueryResult results = query.queryOnSingleSequence(singleSequence);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
        assertEquals((int)(results.getAllStartPoints().get(0)), 296);
    }

}