package interactive.mining;

import inputdata.InputData;
import inputdata.LocalParameter;
import interactive.index.SequenceStorage;
import interactive.metadata.SingleSeqWrapup;
import interactive.mining.index.MiningCFPOnPrevRes;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yizhouyan on 4/27/18.
 */
public class FPMiningLongTest {
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
        oneSeq[5] = 1;
        oneSeq[7] = 2;
        oneSeq[8] = 3;
        oneSeq[11] = 1;
        oneSeq[12] = 2;
        oneSeq[13] = 3;
        oneSeq[14] = 4;
        oneSeq[19] = 2;
        oneSeq[20] = 3;
        oneSeq[21] = 4;
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
        storage.buildIndex(new LocalParameter(2,1,10,5000,60000));
//        storage.printSequenceStorage();
        singleSequence = storage.getLocalFreqPatternWrapUps().get(0);
        singleSequence.printFreqSeqSet(0);
    }

    @Test
    public void miningLocalFreqPatternsOnPrev() {
        setUp_1();
        MiningCFPOnPrevRes miningSingleSeq = new MiningCFPOnPrevRes(singleSequence, 3,
         0, 10, 5000, 60000);
        miningSingleSeq.FreqPatternMiningOnPrev();
        System.out.println(miningSingleSeq.printMiningResult());
    }

    //TODO more test on interactive mining
}