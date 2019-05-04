package interactive.index;

import inputdata.LocalParameter;

import interactive.metadata.Constants;
import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.SingleSeqWrapup;
import interactive.mining.prefixspan.PrefixSpanTool;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by yizhouyan on 8/11/18.
 */
class SequenceStorageTest {
    public SequenceStorage sequenceStorage;

    void setup() {
        String inputFile = "../data/realdata/oneSequenceTS.csv";
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
            HashMap<Short, String> dictionary = sequenceStorage.getInputData().getDictionaryShortToString();
            SingleSeqWrapup freqSeqTree = pst.generateBitMapTree();
            freqSeqTree.printFreqSeqSet(2, dictionary);
        }
    }

    void setup_2(){
        String inputFile = "../data/realdata/5sequenceTS.csv";
        sequenceStorage = new SequenceStorage();
        sequenceStorage.readInputDataFromFile(inputFile);
        sequenceStorage.setLocalParameterForStorage(new LocalParameter(2, 0, 5,
                5000, 60000));
    }

    @Test
    void buildIndex_for5() {
        setup_2();
        for (int i = 0; i < sequenceStorage.getInputData().getInputStringArray().size(); i++) {
            if (this.sequenceStorage.getInputData().getInputStringArray().get(i).length > Constants.SHORT_MAX_LIMIT) {
                System.out.println("Sequence too long: " + this.sequenceStorage.getInputData().getInputStringArray().get(i).length);
            }
            PrefixSpanTool pst = new PrefixSpanTool(sequenceStorage.getInputData().getInputStringArray().get(i),
                    sequenceStorage.getInputData().getInputTimeStamp().get(i),
                    sequenceStorage.getLocalParameterForStorage());
            pst.prefixSpanCalculate();
            HashMap<Short, String> dictionary = sequenceStorage.getInputData().getDictionaryShortToString();
            SingleSeqWrapup freqSeqTree = pst.generateBitMapTree();
            freqSeqTree.printFreqSeqSet(2);

        }
    }

    void setup_3(){
        String inputFile = "../data/realdata/5sequenceTS.csv";
        sequenceStorage = new SequenceStorage();
        sequenceStorage.readInputDataFromFile(inputFile);
        sequenceStorage.setLocalParameterForStorage(new LocalParameter(10, 0, 5,
                5000, 60000));
    }
}