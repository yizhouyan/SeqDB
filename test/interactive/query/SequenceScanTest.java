//package interactive.query;
//
//import interactive.metadata.QueryResult;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Created by yizhouyan on 4/28/18.
// */
//public class SequenceScanTest {
//    short [] getStringInShort(String str){
//        short [] inputs = new short[str.split(",").length];
//        String [] splits = str.split(",");
//        for(int i = 0; i< splits.length; i++)
//            inputs[i] = Short.parseShort(splits[i]);
//        return inputs;
//    }
//
//    @Test
//    void scanForNoQueryTest() {
//        String input = "1,2,3,4,1,2,3,4";
////        String query = "";
//        short [] query = new short[0];
//
//        SequenceScan newScan = new SequenceScan(getStringInShort(input),query ,0, 10);
//        QueryResult result = newScan.scanSequenceForMatch();
////        System.out.println(result.pirntSingleFS(input.split(",").length));
//        assertEquals(0, result.getSupportCount());
//    }
//
//    @Test
//    void scanForSingleElementTest() {
//        String input = "1,2,3,4,1,1,1,2,3,4";
//        String query = "1";
//        SequenceScan newScan = new SequenceScan(getStringInShort(input), getStringInShort(query),0, 10);
//        QueryResult result = newScan.scanSequenceForMatch();
//        assertEquals(4, result.getSupportCount());
////        System.out.println(result.pirntSingleFS(input.split(",").length));
//    }
//
//    @Test
//    void scanSequenceForMatchTest() {
//        String input = "1,2,1,2,3,4,1,2,3,4";
//        String query = "1,2";
//        SequenceScan newScan = new SequenceScan(getStringInShort(input), getStringInShort(query),0, 10);
//        QueryResult result = newScan.scanSequenceForMatch();
//        System.out.println(result.pirntSingleFS(input.split(",").length));
//        assertEquals(3, result.getSupportCount());
//    }
//
//    @Test
//    void scanSequenceForMatchTest_2() {
//        String input = "1,2,3,1,2,4,3,1,2,3,4";
//        String query = "1,2,3";
//        SequenceScan newScan = new SequenceScan(getStringInShort(input), getStringInShort(query),1, 10);
//        QueryResult result = newScan.scanSequenceForMatch();
//        System.out.println(result.pirntSingleFS(input.split(",").length));
//        assertEquals(3, result.getSupportCount());
//    }
//
//    @Test
//    void scanSequenceForMatchTest_3() {
//        String input = "1,2,3,1,2,4,3,1,2,3,4";
//        String query = "1,2,3";
//        SequenceScan newScan = new SequenceScan(getStringInShort(input), getStringInShort(query),0, 10);
//        QueryResult result = newScan.scanSequenceForMatch();
//        System.out.println(result.pirntSingleFS(input.split(",").length));
//        assertEquals(2, result.getSupportCount());
//    }
//
//    @Test
//    void scanSequenceForMatchTest_4() {
//        String input = "1,2,1,1,2,1,2,1,2,1,2,1,2";
//        String query = "1,2,1,2";
//        SequenceScan newScan = new SequenceScan(getStringInShort(input), getStringInShort(query),0, 10);
//        QueryResult result = newScan.scanSequenceForMatch();
//        System.out.println(result.pirntSingleFS(input.split(",").length));
//        assertEquals(2, result.getSupportCount());
//    }
//
//    @Test
//    void scanSequenceForMatchTest_5() {
//        String input = "1,2,1,1,2,1,2,1,2,1,2,1,2";
//        String query = "1,3";
//        SequenceScan newScan = new SequenceScan(getStringInShort(input), getStringInShort(query),0, 10);
//        QueryResult result = newScan.scanSequenceForMatch();
//        System.out.println(result.pirntSingleFS(input.split(",").length));
//        assertEquals(0, result.getSupportCount());
//    }
//}