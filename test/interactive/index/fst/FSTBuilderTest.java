//package interactive.index.fst;
//
//import interactive.metadata.FreqPatternWrapup;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Created by yizhouyan on 9/25/18.
// */
//class FSTBuilderTest {
//    @Test
//    void testLongSize() {
//        System.out.println(Config.kMSbMask);
//        System.out.println((Config.kMSbMask.shiftRight(1)));
//        System.out.println(Config.kMSbMask.and(Config.kMSbMask.shiftRight(1)));
//        System.out.println(!new BigInteger("16140901064495857664", 10).and(Config.kMSbMask).equals(BigInteger.ZERO));
//        System.out.println(!new BigInteger("9223372036854775808", 10).and(Config.kMSbMask.shiftRight(1)).equals(BigInteger.ZERO));
//    }
//
//
//    @Test
//    void testFSTBuild(){
//        ArrayList<FreqPatternWrapup> freqPatterns = new ArrayList<FreqPatternWrapup>();
////        freqPatterns.add(new FreqPatternWrapup("6"));
////        freqPatterns.add(new FreqPatternWrapup("6,1,18"));
////        freqPatterns.add(new FreqPatternWrapup("6,1,19"));
////        freqPatterns.add(new FreqPatternWrapup("6,1,19,20"));
////        freqPatterns.add(new FreqPatternWrapup("6,1,20"));
////        freqPatterns.add(new FreqPatternWrapup("19"));
////        freqPatterns.add(new FreqPatternWrapup("20,15,16"));
////        freqPatterns.add(new FreqPatternWrapup("20,15,25"));
//        freqPatterns.add(new FreqPatternWrapup("102"));
//        freqPatterns.add(new FreqPatternWrapup("102,97,114"));
//        freqPatterns.add(new FreqPatternWrapup("102,97,115"));
//        freqPatterns.add(new FreqPatternWrapup("102,97,115,116"));
//        freqPatterns.add(new FreqPatternWrapup("102,97,116"));
//        freqPatterns.add(new FreqPatternWrapup("115"));
//        freqPatterns.add(new FreqPatternWrapup("116,111,112"));
//        freqPatterns.add(new FreqPatternWrapup("116,111,121"));
//        FST fst = new FST(freqPatterns);
//        System.out.println(fst.lookupKey(new short[]{102,97,110}));
//    }
//
//    @Test
//    void testFSTSort(){
//        ArrayList<FreqPatternWrapup> freqPatterns = new ArrayList<FreqPatternWrapup>();
//
//        FreqPatternWrapup p1 = new FreqPatternWrapup("102");
//        FreqPatternWrapup p2 = new FreqPatternWrapup("116,111,112");
//        FreqPatternWrapup p3 = new FreqPatternWrapup("102,97,115");
//        FreqPatternWrapup p4 = new FreqPatternWrapup("115");
//        FreqPatternWrapup p5 = new FreqPatternWrapup("102,97,115,116");
//        FreqPatternWrapup p6 = new FreqPatternWrapup("102,97,116");
//        FreqPatternWrapup p7 = new FreqPatternWrapup("102,97,114");
//        FreqPatternWrapup p8 = new FreqPatternWrapup("116,111,121");
//        freqPatterns.add(p1);
//        freqPatterns.add(p2);
//        freqPatterns.add(p3);
//        freqPatterns.add(p4);
//        freqPatterns.add(p5);
//        freqPatterns.add(p6);
//        freqPatterns.add(p7);
//        freqPatterns.add(p8);
//        Collections.sort(freqPatterns);
//        assertEquals(freqPatterns.get(0), p1);
//        assertEquals(freqPatterns.get(1), p7);
//        assertEquals(freqPatterns.get(2), p3);
//        assertEquals(freqPatterns.get(3), p5);
//        assertEquals(freqPatterns.get(4), p6);
//        assertEquals(freqPatterns.get(5), p4);
//        assertEquals(freqPatterns.get(6), p2);
//        assertEquals(freqPatterns.get(7), p8);
//    }
//
//}