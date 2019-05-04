//package interactive.mining;
//
//import inputdata.LocalParameter;
//import interactive.metadata.*;
//import interactive.mining.prefixspan.PrefixSpanTool;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.BitSet;
//import java.util.HashMap;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
///**
// * Created by yizhouyan on 4/27/18.
// */
//public class PrefixSpanTest {
//    @Test
//    void miningLocalFreqPatternsOnPrev() {
//        LocalParameter localParameter = new LocalParameter(2,3,5,
//                5000, 60000,2);
//        String s = "1,2,3,1,2,3,1,3,2,1,3";
//        short [] inputs = new short[11];
//        String [] splits = s.split(",");
//        for(int i = 0; i< splits.length; i++)
//            inputs[i] = Short.parseShort(splits[i]);
//        PrefixSpanTool tool = new PrefixSpanTool(inputs, localParameter);
//        tool.prefixSpanCalculate();
////        tool.printTotalFreqSeqsSimple();
////        HashMap<SingleBitSet, ArrayList<Integer>> bitsetAndStartPoints = new HashMap<>();
//        ArrayList<FreqPatternWrapup> freqPatternWithBMsList = new ArrayList<>();
//        SingleSeqWrapup bmtree = tool.generateBitMapTree(0, freqPatternWithBMsList);
////        bmtree.transferToArrayStartPoints(bitsetAndStartPoints);
//        bmtree.printFreqSeqSet();
//        for(FreqPatternWrapup freqPatternWrapup : freqPatternWithBMsList){
//            System.out.println("Frequent Pattern: " + freqPatternWrapup.getPatternInString());
////            QueryResult rs = freqPatternWrapup.getQueryResult(11);
////            System.out.println(rs.pirntSingleFS(11));
//        }
//    }
//
//    @Test
//    void testBitSetEqual(){
//        // create 2 bitsets
//        BitSet bitset1 = new BitSet(8);
//        BitSet bitset2 = new BitSet(8);
//
//        // assign values to bitset1
//        bitset1.set(0);
//        bitset1.set(1);
//        bitset1.set(2);
//        bitset1.set(3);
//
//        // assign values to bitset2
//        bitset2.set(0);
//        bitset2.set(1);
//        bitset2.set(2);
//        bitset2.set(3);
//
//        // print the sets
//        System.out.println("Bitset1:" + bitset1);
//        System.out.println("Bitset2:" + bitset2);
//
//        // check equality for bitset1 and bitset 2
//        System.out.println("" + bitset1.equals(bitset2));
//    }
//
//    @Test
//    void strArrayContains_nogap() {
//        short [] str1 = {2,1,3,4};
//        short [] str2 = {1,4};
//        assertTrue(PrefixSpanTool.strArrayContainsWithSamePrefix(str1, str2));
//    }
//
//    @Test
//    void strArrayContains_3() {
//        short [] str1 = {2,1,3,4};
//        short [] str2 = {3,4,1};
//        assertFalse(PrefixSpanTool.strArrayContainsWithSamePrefix(str1, str2));
//    }
//}