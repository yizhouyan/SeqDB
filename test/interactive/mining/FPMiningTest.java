package interactive.mining;

import inputdata.InputData;
import interactive.index.SequenceStorage;
import inputdata.LocalParameter;
import interactive.metadata.SingleSeqWrapup;
import interactive.mining.index.MiningCFPOnPrevRes;
import interactive.mining.index.MiningCloseOnPrevRes;
import interactive.mining.index.MiningMaxOnPrevRes;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by yizhouyan on 4/27/18.
 */
class FPMiningTest {
    private SequenceStorage storage;
    private SingleSeqWrapup singleSequence;
    private int sequenceLength;

    void setUp_1() {
        this.storage = new SequenceStorage();
        List<short[]> inputStringArray = new ArrayList<>();
        InputData input = new InputData();
        short [] oneSeq = {1,2,3,4,1,2,3,1,2,3,4,5,1,2,3,1,3,4,1,2,4};
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
        singleSequence.printFreqSeqSet(0);
    }

    @Test
    void miningLocalFreqPatternsOnPrev() {
        setUp_1();
        MiningCFPOnPrevRes miningSingleSeq = new MiningCFPOnPrevRes(singleSequence, 5,
         0, 10, 5000, 60000);
        miningSingleSeq.FreqPatternMiningOnPrev();
        System.out.println(miningSingleSeq.printMiningResult());
    }

    @Test
    void miningMaxLocalFreqPatternsOnPrev() {
        setUp_1();
        MiningMaxOnPrevRes miningSingleSeq = new MiningMaxOnPrevRes(singleSequence, 3,2,
                0, 10, 5000, 60000);
        miningSingleSeq.FreqPatternMiningOnPrev();
        System.out.println(miningSingleSeq.printMiningResult());
    }

    @Test
    void miningCloseLocalFreqPatternsOnPrev_1() {
        setUp_1();
        MiningCloseOnPrevRes miningSingleSeq = new MiningCloseOnPrevRes(singleSequence, 2,2,
                0, 10, 5000, 60000);
        assertEquals(6,miningSingleSeq.FreqPatternMiningOnPrev().size());
        System.out.println(miningSingleSeq.printMiningResult());
    }

    @Test
    void miningCloseLocalFreqPatternsOnPrev() {
        setUp_1();
        MiningCloseOnPrevRes miningSingleSeq = new MiningCloseOnPrevRes(singleSequence, 3,2,
                0, 10, 5000, 60000);
        assertEquals(3,miningSingleSeq.FreqPatternMiningOnPrev().size());
        System.out.println(miningSingleSeq.printMiningResult());
    }

    //TODO more test on interactive mining
}