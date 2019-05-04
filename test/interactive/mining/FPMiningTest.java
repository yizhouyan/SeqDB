package interactive.mining;

import inputdata.InputData;
import interactive.index.SequenceStorage;
import inputdata.LocalParameter;
import interactive.metadata.SingleSeqWrapup;
import interactive.mining.index.MiningCFPOnPrevRes;
import interactive.mining.baseline.close.SingleClosePatternMiningTS;
import interactive.mining.baseline.contextual.SingleContextMiningTS;
import interactive.mining.index.MiningCloseOnPrevRes;
import interactive.mining.index.MiningMaxOnPrevRes;
import interactive.mining.baseline.maximum.SingleMaxPatternMiningTS;
import interactive.mining.baseline.top.inputs.InputSequenceWithTS;
import interactive.mining.baseline.top.parameterspace.LocalParameterSpaceWithTS;
import interactive.mining.baseline.top.utils.Toolbox;
import org.junit.Test;


import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by yizhouyan on 4/27/18.
 */
public class FPMiningTest {
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
    public void miningMaxBaseline(){
        String inputStr = "1|0,2|0,3|0,4|0,1|0,2|0,3|0,1|0,2|0,3|0,4|0,5|0,1|0,2|0,3|0,1|0,3|0,4|0,1|0,2|0,4|0";
        LocalParameterSpaceWithTS localParameterSpace =
                new LocalParameterSpaceWithTS(2, 0, 10,
                        5000, 60000);
        InputSequenceWithTS inputSequence = new InputSequenceWithTS(inputStr);
        SingleMaxPatternMiningTS obj = new SingleMaxPatternMiningTS(inputSequence, localParameterSpace);
        ArrayList<String> inputStringArray = new ArrayList<>();
        inputStringArray.add(inputStr);
        HashSet<String> globalFrequentElements = Toolbox.getGlobalFrequentElements(inputStringArray, 1,
                2);
        Set<String> freqPatttern = obj.findFreqSeqInOneString(globalFrequentElements);
        for(String str: freqPatttern) {
            System.out.println(str);
        }
    }

    @Test
    public void miningMaxBaseline_2(){
        String inputStr = "1|0,2|0,3|0,4|0,1|0,2|0,3|0,1|0,2|0,3|0,4|0,5|0,1|0,2|0,3|0,1|0,3|0,4|0,1|0,2|0,4|0";
        LocalParameterSpaceWithTS localParameterSpace =
                new LocalParameterSpaceWithTS(3, 0, 10,
                        5000, 60000);
        InputSequenceWithTS inputSequence = new InputSequenceWithTS(inputStr);
        SingleMaxPatternMiningTS obj = new SingleMaxPatternMiningTS(inputSequence, localParameterSpace);
        ArrayList<String> inputStringArray = new ArrayList<>();
        inputStringArray.add(inputStr);
        HashSet<String> globalFrequentElements = Toolbox.getGlobalFrequentElements(inputStringArray, 1,
                3);
        Set<String> freqPatttern = obj.findFreqSeqInOneString(globalFrequentElements);
        for(String str: freqPatttern) {
            System.out.println(str);
        }
    }

    @Test
    public void miningMaxLocalFreqPatternsOnPrev() {
        setUp_1();
        MiningMaxOnPrevRes miningSingleSeq = new MiningMaxOnPrevRes(singleSequence, 3,2,
                0, 10, 5000, 60000);
        HashMap<short[], Integer> results =  miningSingleSeq.FreqPatternMiningOnPrev();
        assertEquals(2, results.size());
        assertTrue(results.values().contains(3));
        assertTrue(results.values().contains(4));
        System.out.println(miningSingleSeq.printMiningResult());
    }

    @Test
    public void miningMaxLocalFreqPatternsOnPrev_support2() {
        setUp_1();
        MiningMaxOnPrevRes miningSingleSeq = new MiningMaxOnPrevRes(singleSequence, 2,2,
                0, 10, 5000, 60000);
        HashMap<short[], Integer> results =  miningSingleSeq.FreqPatternMiningOnPrev();
        System.out.println(miningSingleSeq.printMiningResult());
    }


    @Test
    public void miningCloseLocalFreqPatternsOnPrev_1() {
        setUp_1();
        MiningCloseOnPrevRes miningSingleSeq = new MiningCloseOnPrevRes(singleSequence, 2,2,
                0, 10, 5000, 60000);
        assertEquals(6,miningSingleSeq.FreqPatternMiningOnPrev().size());
        System.out.println(miningSingleSeq.printMiningResult());
    }



    @Test
    public void miningCloseLocalFreqPatternsOnPrev() {
        setUp_1();
        MiningCloseOnPrevRes miningSingleSeq = new MiningCloseOnPrevRes(singleSequence, 3,2,
                0, 10, 5000, 60000);
        assertEquals(3,miningSingleSeq.FreqPatternMiningOnPrev().size());
        System.out.println(miningSingleSeq.printMiningResult());
    }

    @Test
    public void miningCloseBaseline(){
        String inputStr = "1|0,2|0,3|0,4|0,1|0,2|0,3|0,1|0,2|0,3|0,4|0,5|0,1|0,2|0,3|0,1|0,3|0,4|0,1|0,2|0,4|0";
        LocalParameterSpaceWithTS localParameterSpace =
                new LocalParameterSpaceWithTS(2, 0, 10,
                        5000, 60000);
        InputSequenceWithTS inputSequence = new InputSequenceWithTS(inputStr);
        SingleClosePatternMiningTS obj = new SingleClosePatternMiningTS(inputSequence, localParameterSpace);
        ArrayList<String> inputStringArray = new ArrayList<>();
        inputStringArray.add(inputStr);
        HashSet<String> globalFrequentElements = Toolbox.getGlobalFrequentElements(inputStringArray, 1,
                2);
        Set<String> freqPatttern = obj.findFreqSeqInOneString(globalFrequentElements);
        for(String str: freqPatttern) {
            System.out.println(str);
        }
    }

    @Test
    public void miningCloseBaseline_2(){
        String inputStr = "1|0,2|0,3|0,4|0,1|0,2|0,3|0,1|0,2|0,3|0,4|0,5|0,1|0,2|0,3|0,1|0,3|0,4|0,1|0,2|0,4|0";
        LocalParameterSpaceWithTS localParameterSpace =
                new LocalParameterSpaceWithTS(3, 0, 10,
                        5000, 60000);
        InputSequenceWithTS inputSequence = new InputSequenceWithTS(inputStr);
        SingleClosePatternMiningTS obj = new SingleClosePatternMiningTS(inputSequence, localParameterSpace);
        ArrayList<String> inputStringArray = new ArrayList<>();
        inputStringArray.add(inputStr);
        HashSet<String> globalFrequentElements = Toolbox.getGlobalFrequentElements(inputStringArray, 1,
                3);
        Set<String> freqPatttern = obj.findFreqSeqInOneString(globalFrequentElements);
        for(String str: freqPatttern) {
            System.out.println(str);
        }
    }


        @Test
    public void miningLocalFreqPatternsOnPrev() {
        setUp_1();
        MiningCFPOnPrevRes miningSingleSeq = new MiningCFPOnPrevRes(singleSequence, 2,
         0, 10, 5000, 60000);
        miningSingleSeq.FreqPatternMiningOnPrev();
        System.out.println(miningSingleSeq.printMiningResult());
    }

    @Test
    public void miningLocalFreqPatternsOnPrev_2() {
        setUp_1();
        MiningCFPOnPrevRes miningSingleSeq = new MiningCFPOnPrevRes(singleSequence, 3,
                0, 10, 5000, 60000);
        miningSingleSeq.FreqPatternMiningOnPrev();
        System.out.println(miningSingleSeq.printMiningResult());
    }

    @Test
    public void miningContextBaseline(){
        String inputStr = "1|0,2|0,3|0,4|0,1|0,2|0,3|0,1|0,2|0,3|0,4|0,5|0,1|0,2|0,3|0,1|0,3|0,4|0,1|0,2|0,4|0";
        LocalParameterSpaceWithTS localParameterSpace =
                new LocalParameterSpaceWithTS(2, 0, 10,
                        5000, 60000);
        InputSequenceWithTS inputSequence = new InputSequenceWithTS(inputStr);
        SingleContextMiningTS obj = new SingleContextMiningTS(inputSequence, localParameterSpace);
        ArrayList<String> inputStringArray = new ArrayList<>();
        inputStringArray.add(inputStr);
        HashSet<String> globalFrequentElements = Toolbox.getGlobalFrequentElements(inputStringArray, 1,
                2);
        Set<String> freqPatttern = obj.findFreqSeqInOneString(globalFrequentElements);
        for(String str: freqPatttern) {
            System.out.println(str);
        }
    }

    @Test
    public void miningContextBaseline_2(){
        String inputStr = "1|0,2|0,3|0,4|0,1|0,2|0,3|0,1|0,2|0,3|0,4|0,5|0,1|0,2|0,3|0,1|0,3|0,4|0,1|0,2|0,4|0";
        LocalParameterSpaceWithTS localParameterSpace =
                new LocalParameterSpaceWithTS(3, 0, 10,
                        5000, 60000);
        InputSequenceWithTS inputSequence = new InputSequenceWithTS(inputStr);
        SingleContextMiningTS obj = new SingleContextMiningTS(inputSequence, localParameterSpace);
        ArrayList<String> inputStringArray = new ArrayList<>();
        inputStringArray.add(inputStr);
        HashSet<String> globalFrequentElements = Toolbox.getGlobalFrequentElements(inputStringArray, 1,
                3);
        Set<String> freqPatttern = obj.findFreqSeqInOneString(globalFrequentElements);
        for(String str: freqPatttern) {
            System.out.println(str);
        }
    }
}