package interactive.query;

import inputdata.InputData;
import inputdata.LocalParameter;
import interactive.index.SequenceStorage;
import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.QueryResult;
import interactive.metadata.SingleSeqWrapup;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by yizhouyan on 4/25/18.
 */
class FPQueryOptimizationTest {
    private SequenceStorage storage;
    private SingleSeqWrapup singleSequence;
    private int sequenceLength;

    void setUp_1() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = {1,2,3,4,1,2,3,1,2,3,1,2,3,1,2,3,5,2,3,4,1,3,4,1,2,4};
        inputStringArray.add(oneSeq);
        this.sequenceLength = inputStringArray.get(0).length;
        input.setInputStringArray(inputStringArray);

        long [] timeStamps = new long[oneSeq.length];
        List<long []> inputTS = new ArrayList<>();
        inputTS.add(timeStamps);
        input.setInputTimeStamp(inputTS);

        storage.setInputData(input);
        storage.buildIndex(new LocalParameter(2,0,10,5000,60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(2);
    }

    @Test
    void queryOnSingleSequenceTest_ExactMatch_1(){
        setUp_1();
        short [] singleQuery = {1,2,3,4};
        FPQuery query = new FPQuery(storage, singleQuery);
        HashMap<Integer, Integer> bestMatchStartPoints = new HashMap<>();
        QueryResult results = query.queryOnSingleSequence(singleSequence);
        //        System.out.println(results.pirntSingleFS(sequenceLength));
        assertEquals(1,results.getSupportCount());
        assertEquals(0, (int)(results.getAllStartPoints().get(0)));
    }

}