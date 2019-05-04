package interactive.metadata;

import inputdata.LocalParameter;
import interactive.index.SequenceStorage;
import interactive.metadata.bmcontainer.IBMContainer;
import interactive.mining.prefixspan.PrefixSpanTool;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;


/**
 * Created by yizhouyan on 6/26/18.
 */
public class FreqPatternWrapupTest {
    @Test
    public void findAddingPosition() {
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

    public void setup() {
        String inputFile = "../data/realdata/oneSequenceTS.csv";
        sequenceStorage = new SequenceStorage();
        sequenceStorage.readInputDataFromFile(inputFile);
        sequenceStorage.setLocalParameterForStorage(new LocalParameter(2, 0, 5,
                5000, 60000));
    }

    @Test
    public void testSuperPatterns() {
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
            for(Map.Entry<Integer, ArrayList<FreqPatternWrapup>> freqPatternList: freqSeqTree.getFreqPatternsMap().entrySet()) {
                for (FreqPatternWrapup freqPatternWrapups : freqPatternList.getValue()) {
                    System.out.println("Previous Pattern: ");
                    System.out.println(freqPatternWrapups.printPattern(this.sequenceStorage.getInputData().getInputStringArray().get(i).length));
                    System.out.println("Absolute Pattern Starts: ");
                    QueryResultCache newStartPoints =
                            freqPatternWrapups.getCompleteQueryResult(this.sequenceStorage.getInputData().getInputStringArray().get(i).length,
                                    sequenceStorage.getInputData().getInputStringArray().get(i),
                                    sequenceStorage.getInputData().getInputTimeStamp().get(i), 0, 5, 5000, 60000);
                    System.out.println("Size of Start Points: " + newStartPoints.startPoints.cardinality());
                    System.out.println(newStartPoints.startPoints);
                    System.out.println("\n\n\n");
                }
            }
        }
    }
}