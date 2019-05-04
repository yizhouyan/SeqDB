package interactive.query;

import interactive.index.SequenceStorage;
import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.SingleSeqWrapup;
import inputdata.InputData;
import inputdata.LocalParameter;
import interactive.metadata.QueryResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by yizhouyan on 4/25/18.
 */
class JoinTwoOccurrencesTest {
    private SequenceStorage storage;
    private SingleSeqWrapup singleSequence;
    private int sequenceLength;

    void setUp_3() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = {1,2,3,4,5,1,2,1,2,3,4,1,2,3,5,5,4,5,1,2,5,3,4,5};
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
    void queryOnSingleSequenceTest_JoinTwoMatch_1(){
        setUp_3();
        short [] curQuery = {1,2,3,4,5};
        FPQuery query = new FPQuery(storage, curQuery);
        QueryResult results = query.queryOnSingleSequence(singleSequence);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),2);
        assertEquals((int)results.getAllStartPoints().get(0), 0);
    }


    void setUp_4() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = {3,4,5,1,2,1,2,5,3,4,1,2,5,5,3,4};
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
    void queryOnSingleSequenceTest_JoinTwoMatch_2(){
        setUp_4();
        short [] curQuery = {1,2,3,4};
        FPQuery query = new FPQuery(storage, curQuery);

        QueryResult results = query.queryOnSingleSequence(singleSequence);
        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(results.getSupportCount(),1);
        assertEquals((int)results.getAllStartPoints().get(0), 5);
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
    void queryOnSingleSequenceTest_JoinTwoMatch_nomatch(){
        setUp_5();
        short [] curQuery = {1,2,3,4};
        FPQuery query = new FPQuery(storage, curQuery);

        QueryResult results = query.queryOnSingleSequence(singleSequence);
        assertEquals(results.getSupportCount(),0);
    }
}