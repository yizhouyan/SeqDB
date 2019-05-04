package interactive.metadata;

import inputdata.LocalParameter;
import interactive.index.SequenceStorage;
import interactive.mining.prefixspan.PrefixSpanTool;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by yizhouyan on 6/26/18.
 */
class FreqPatternWrapupTest {
    @Test
    void findAddingPosition() {
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(1);
        temp.add(3);
        temp.add(5);
        temp.add(8);

        assertEquals(2, FreqPatternWrapup.findAddingPosition(temp,0,4));
        assertEquals(0, FreqPatternWrapup.findAddingPosition(temp,0,0));
        assertEquals(3, FreqPatternWrapup.findAddingPosition(temp,1,6));
        assertEquals(4, FreqPatternWrapup.findAddingPosition(temp,1,9));
    }


    public SequenceStorage sequenceStorage;

    void setup() {
        String inputFile = "../data/realdata/oneSequenceTS.csv";
        sequenceStorage = new SequenceStorage();
        sequenceStorage.readInputDataFromFile(inputFile);
        sequenceStorage.setLocalParameterForStorage(new LocalParameter(2, 0, 5,
                5000, 60000));
    }

    @Test
    void testSuperPatterns() {
        setup();
        for (int i = 0; i < sequenceStorage.getInputData().getInputStringArray().size(); i++) {
            if (this.sequenceStorage.getInputData().getInputStringArray().get(i).length > Constants.SHORT_MAX_LIMIT) {
                System.out.println("Sequence too long: " + this.sequenceStorage.getInputData().getInputStringArray().get(i).length);
            }
            PrefixSpanTool pst = new PrefixSpanTool(sequenceStorage.getInputData().getInputStringArray().get(i),
                    sequenceStorage.getInputData().getInputTimeStamp().get(i),
                    sequenceStorage.getLocalParameterForStorage());
            pst.prefixSpanCalculate();
            SingleSeqWrapup freqSeqTree = pst.generateBitMapTree();
            for(FreqPatternWrapup freqPatternWrapups: freqSeqTree.getFreqPatternList()){
                System.out.println("Previous Pattern: ");
                System.out.println(freqPatternWrapups.printPattern(this.sequenceStorage.getInputData().getInputStringArray().get(i).length));
                System.out.println("\n");
            }
        }
    }

    @Test
    void testFreqSeqCompare_1(){
        ArrayList<FreqPatternWrapup> freqPatterns = new ArrayList<FreqPatternWrapup>();
        FreqPatternWrapup p1 = new FreqPatternWrapup("102,97,114");
        FreqPatternWrapup p2 = new FreqPatternWrapup("101,114,97");
        freqPatterns.add(p1);
        freqPatterns.add(p2);
        Collections.sort(freqPatterns);
        assertEquals(p2, freqPatterns.get(0));
    }

    @Test
    void testFreqSeqCompare_2(){
        ArrayList<FreqPatternWrapup> freqPatterns = new ArrayList<FreqPatternWrapup>();
        FreqPatternWrapup p1 = new FreqPatternWrapup("101,97,114");
        FreqPatternWrapup p2 = new FreqPatternWrapup("101,114,97");
        freqPatterns.add(p1);
        freqPatterns.add(p2);
        Collections.sort(freqPatterns);
        assertEquals(p1, freqPatterns.get(0));
    }

    @Test
    void testFreqSeqCompare_3(){
        ArrayList<FreqPatternWrapup> freqPatterns = new ArrayList<FreqPatternWrapup>();
        FreqPatternWrapup p1 = new FreqPatternWrapup("101,97,114");
        FreqPatternWrapup p2 = new FreqPatternWrapup("101,97");
        freqPatterns.add(p1);
        freqPatterns.add(p2);
        Collections.sort(freqPatterns);
        assertEquals(p2, freqPatterns.get(0));
    }
}