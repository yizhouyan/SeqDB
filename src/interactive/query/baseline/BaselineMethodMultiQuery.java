package interactive.query.baseline;

import interactive.index.SequenceStorage;
import interactive.metadata.QueryResult;
import interactive.query.SequenceScan;
import interactive.util.InteractiveToolkit;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yizhouyan on 4/30/18.
 */
public class BaselineMethodMultiQuery {
    public static void main(String[] args) {
        SequenceStorage sequenceStorage = new SequenceStorage();
        sequenceStorage.readInputDataFromFile(ExpParameters.exp_inputDir);
        List<short[]> inputData = sequenceStorage.getInputData().getInputStringArray();
        List<long []> inputTimestamps = sequenceStorage.getInputData().getInputTimeStamp();

        ArrayList<Double> timeCosts = new ArrayList<>();
//        ArrayList<String> queries = InteractiveToolkit.generateQuery();
        ArrayList<String> queries = InteractiveToolkit.getQuerysFromFile("GlobalFrequent_logFile");

        for (String qry : queries) {
            System.out.println(qry);
            qry = "69," + qry;
            String [] queryStr = qry.split(",");
            short [] queryInShort = new short[queryStr.length];
            for(int i = 0; i< queryStr.length; i++){
                queryInShort[i] = Short.parseShort(queryStr[i]);
            }
            double timeCost1 = query(inputData, inputTimestamps, queryInShort);
            double timeCost2 = query(inputData, inputTimestamps, queryInShort);
            double timeCost3 = query(inputData, inputTimestamps, queryInShort);
            double timeCost = (timeCost1 + timeCost2 + timeCost3) / 3;
            timeCosts.add(timeCost);
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Results_scan_globalFrequent_logFile"));
            for (Double d : timeCosts) {
                writer.write(d + "");
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     public static double query(List<short[]> inputData, List<long [] > inputTimestamps, short [] queryInShort ){
        HashMap<Integer, QueryResult> patternOccurrences = new HashMap<Integer, QueryResult>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < inputData.size(); i++) {
            SequenceScan singleScan = new SequenceScan(inputData.get(i), inputTimestamps.get(i), queryInShort,
                    ExpParameters.exp_eventGap, ExpParameters.exp_seqGap,
                    ExpParameters.exp_eventGapTS, ExpParameters.exp_seqGapTS);
            QueryResult curBitSetRes = singleScan.scanSequenceForMatch();
            if (curBitSetRes.getSupportCount() > 0)
                patternOccurrences.put(i, curBitSetRes);
        }
        return (System.currentTimeMillis() - startTime) * 1.0 /1000;
    }
}
