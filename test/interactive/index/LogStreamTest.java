package interactive.index;

import inputdata.LocalParameter;
import interactive.metadata.Constants;
import interactive.metadata.SingleSeqWrapup;
import interactive.mining.prefixspan.PrefixSpanTool;
import org.junit.jupiter.api.Test;

/**
 * Created by yizhouyan on 8/11/18.
 */
class LogStreamTest {
    public SequenceStorage sequenceStorage;

    void setup() {
        String inputFile = "../data/realdata/logstream.csv";
        sequenceStorage = new SequenceStorage();
        sequenceStorage.readInputDataFromFile(inputFile);
        sequenceStorage.setLocalParameterForStorage(new LocalParameter(2, 0, 5,
                5000, 60000));
    }

    @Test
    void buildIndex() {
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
            freqSeqTree.printFreqSeqSet(2);
        }
    }

}