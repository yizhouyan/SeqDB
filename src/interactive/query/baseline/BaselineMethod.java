//package interactive.experiments;
//
//import interactive.index.SequenceStorage;
//import interactive.metadata.QueryResult;
//import interactive.query.SequenceScan;
//import interactive.util.InteractiveToolkit;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by yizhouyan on 4/30/18.
// */
//public class BaselineMethod {
//    public static void main(String[] args) {
//        SequenceStorage sequenceStorage = new SequenceStorage();
//        sequenceStorage.readInputDataFromFile(ExpParameters.exp_inputDir);
//        List<short[]> inputData = sequenceStorage.getInputData().getInputStringArray();
//        HashMap<Integer, QueryResult> patternOccurrences = new HashMap<Integer, QueryResult>();
//
//        String qry = "";
//        String[] queryStr = qry.split(",");
//        short[] queryInShort = new short[queryStr.length];
//        for (int i = 0; i < queryStr.length; i++) {
//            queryInShort[i] = Short.parseShort(queryStr[i]);
//        }
//
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < inputData.size(); i++) {
//            SequenceScan singleScan = new SequenceScan(inputData.get(i), queryInShort,
//                    ExpParameters.exp_eventGap, ExpParameters.exp_seqGap);
//            QueryResult curBitSetRes = singleScan.scanSequenceForMatch();
//            if (curBitSetRes.getSupportCount() > 0)
//                patternOccurrences.put(i, curBitSetRes);
//        }
//        System.out.println("Query takes: " + (System.currentTimeMillis() - startTime) * 1.0 / 1000);
//    }
//}
