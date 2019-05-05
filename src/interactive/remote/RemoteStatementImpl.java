package interactive.remote;

import interactive.index.SequenceStorage;
import interactive.metadata.BasicStatistics;
import interactive.metadata.SingleSeqWrapup;
import interactive.metadata.FreqPatternWrapup;
import inputdata.LocalParameter;
import interactive.mining.index.FPMining;
//import interactive.query.FPQueryWithGroupTries;
import interactive.query.FPQueryWithOneTrie;
import interactive.util.InteractiveToolkit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The RMI server-side implementation of RemoteStatement.
 * Created by yizhouyan on 4/20/18.
 */
public class RemoteStatementImpl extends UnicastRemoteObject implements RemoteStatement {
    private static final long MEGABYTE = 1024L * 1024L;

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    private SequenceStorage sequenceStorage;

    public RemoteStatementImpl() throws RemoteException{

    }

    @Override
    public void setupSequenceIndex(String inputFile, int localSupport, int itemGap,
                                   int seqGap, long itemGapTS, long seqGapTS) throws RemoteException {
        System.out.println("Request Received --- setup index for " + inputFile);
        System.out.println();
        System.out.println("-------------------------------Start Building Index--------------------------------");
        this.sequenceStorage = new SequenceStorage();
        this.sequenceStorage.readInputDataFromFile(inputFile);
        this.sequenceStorage.getInputData().printInputDataStatistics();
        this.sequenceStorage.buildIndex(new LocalParameter(localSupport, itemGap, seqGap, itemGapTS, seqGapTS));
        System.out.println("-------------------------------End Building Index--------------------------------");
        System.out.println();

//        BasicStatistics.printBasicStatistics();

        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is bytes: " + memory);
        System.out.println("Used memory is megabytes: "
                + bytesToMegabytes(memory));

        // print index
//        this.sequenceStorage.printSimpleSequenceStorage();
    }

    @Override
    public void setupSequenceIndex(String inputFile, String metaFile, int localSupport, int itemGap,
                                   int seqGap, long itemGapTS, long seqGapTS) throws RemoteException {
        System.out.println("Request Received --- setup index for " + inputFile);
        System.out.println();
        System.out.println("Input File: " + inputFile + ", local support: " + localSupport);
        System.out.println("-------------------------------Start Building Index--------------------------------");
        this.sequenceStorage = new SequenceStorage();
        this.sequenceStorage.readInputDataFromFileWithMeta(inputFile, metaFile);
        this.sequenceStorage.buildIndex(new LocalParameter(localSupport, itemGap, seqGap, itemGapTS, seqGapTS));
        System.out.println("-------------------------------End Building Index--------------------------------");
        System.out.println();
        // print index
//        this.sequenceStorage.printSimpleSequenceStorage();
    }

    public boolean checkStorageAvailability(){
        if(sequenceStorage!=null)
            return true;
        else {
            System.out.println("Sequence index is not available, please first setup the sequence index!");
            return false;
        }
    }

    @Override
    public void executeMining(int localSupport, int itemGap, int seqGap, long itemGapTS, long seqGapTS, String method)
            throws RemoteException {
        System.out.println("Request Received --- pattern mining on existing index");
        System.out.println();
        System.out.println("-------------------------------Start Pattern Mining on Existing Index--------------------------------");
        if (checkStorageAvailability()) {
            int methodType = checkMethodName(method);
            if(methodType == -1)
                return;
            long minTime = Long.MAX_VALUE;
            long maxTime = 0;
            long sumTime = 0;
            for (int i = 0; i < 10; i++) {
//                localSupport = i;
                FPMining fpMining = new FPMining(this.sequenceStorage, localSupport, itemGap, seqGap, itemGapTS, seqGapTS);
                long curTime = 0;
                System.out.println("Support: " + localSupport + ", " + method);
                if(methodType == 0)
                    curTime = fpMining.miningContextualLocalFreqPatternsOnPrev();
                else if(methodType == 1)
                    curTime = fpMining.miningMaxLocalFreqPatternsOnPrev();
                else if(methodType == 2)
                    curTime = fpMining.miningCloseLocalFreqPatternsOnPrev();
                else if (methodType == 3)
                    curTime = fpMining.miningFreqLocalFreqPatternsOnPrev();
                System.out.println(method + " Pattern Mining on Index takes: " + curTime * 1.0 / (1000) + " seconds");
                minTime = Math.min(curTime, minTime);
                maxTime = Math.max(curTime, maxTime);
                sumTime += curTime;
            }
            System.out.println("Min Time: " + minTime * 1.0 / 1000);
            System.out.println("Max Time: " + maxTime * 1.0 / 1000);
//            System.out.println("Sum Time: " + sumTime * 1.0 / 1000);
            System.out.println("Average Time: " + sumTime * 1.0 / (1000 * 10));
        }
        System.out.println("-------------------------------End Pattern Mining on Existing Index--------------------------------");
        System.out.println();
    }


    public int checkMethodName(String method) {
        int methodName = -1;
        if(method.toLowerCase().equals("context")) {
            methodName = 0;
        }else if(method.toLowerCase().equals("max")){
            methodName = 1;
        }else if(method.toLowerCase().equals("close"))
            methodName = 2;
        else if(method.toLowerCase().equals("freq"))
            methodName = 3;
        return methodName;
    }

    @Override
    public void executeQuery(String qry) throws RemoteException {
        System.out.println("Request Received --- pattern query on existing index");
        System.out.println();
        System.out.println("-------------------------------Start Pattern Query on Existing Index--------------------------------");
        if(checkStorageAvailability()) {
//            FPQuery fpQuery = new FPQuery(this.sequenceStorage, this.sequenceStorage.getInputData().translateQuery(qry));
//            fpQuery.queryOnAllSequences();
            System.out.println("pattern query with existing index");
        }
        System.out.println("-------------------------------End Pattern Query on Existing Index--------------------------------");
        System.out.println();
    }


    @Override
    public void executeQuery(String globalFreqFileName, String outputFileName) throws RemoteException {
        ArrayList<long []> timeCosts = new ArrayList<>();
//        ArrayList<String> queries = InteractiveToolkit.generateQuery();
        ArrayList<String> queries = InteractiveToolkit.getQuerysFromFile(globalFreqFileName);
//        ArrayList<String> queries = InteractiveToolkit.getQuerysFromFile("GlobalFrequent");

        if(checkStorageAvailability()) {
            for(String qry: queries) {
//                qry = "69," + qry;
//                qry = "0," + qry;
                System.out.println(qry);
                String [] queryStr = qry.split(",");
                short [] queryInShort = new short[queryStr.length];
                for(int i = 0; i< queryStr.length; i++){
                    queryInShort[i] = Short.parseShort(queryStr[i]);
                }
                FPQueryWithOneTrie fpQuery = new FPQueryWithOneTrie(this.sequenceStorage, queryInShort);
//                FPQueryWithGroupTries fpQuery = new FPQueryWithGroupTries(this.sequenceStorage, queryInShort);
////                FPQuery fpQuery = new FPQuery(this.sequenceStorage, this.sequenceStorage.getInputData().translateQuery(qry));
                long[] timeCost = fpQuery.queryOnAllSequencesWithOneTrie();
//                long[] timeCost2 = fpQuery.queryOnAllSequencesWithOneTrie();
//                long[] timeCost3 = fpQuery.queryOnAllSequencesWithOneTrie();
//                long[] timeCost = new long[2];
//                for(int i = 0; i< 2; i++)
//                    timeCost[i] = (timeCost1[i] + timeCost2[i] + timeCost3[i]) / 3;
                timeCosts.add(timeCost);
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
//            BufferedWriter writer = new BufferedWriter(new FileWriter("Results_indexquery_globalFrequent_logFile_synthetic_opt"));
            for(int i = 0; i<timeCosts.size(); i++) {
                writer.write(queries.get(i) + "\t" + queries.get(i).split(",").length + "\t" +
                        timeCosts.get(i)[0] + "\t" + timeCosts.get(i)[1] + "\t" + timeCosts.get(i)[2]);
                writer.newLine();
            }
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void getGlobalFreqPattern(int globalSupport, boolean outputToFile, String outputFileName) {
        if(checkStorageAvailability()) {
            HashMap<String, Integer> globalFreqPattern = new HashMap<>();
            for(Map.Entry<Integer, SingleSeqWrapup> entry:sequenceStorage.getLocalFreqPatternWrapUps().entrySet()){
                for(Map.Entry<Integer, ArrayList<FreqPatternWrapup>> freqPatternList: entry.getValue().getFreqPatternsMap().entrySet()) {
                    for (FreqPatternWrapup pattern : freqPatternList.getValue()) {
                        if (globalFreqPattern.containsKey(pattern.getPatternInString()))
                            globalFreqPattern.put(pattern.getPatternInString(), globalFreqPattern.get(pattern.getPatternInString()) + 1);
                        else {
                            globalFreqPattern.put(pattern.getPatternInString(), 1);
                        }
                    }
                }
            }
            HashMap<String, Integer> finalGlobalFreqPattern = new HashMap<>();
            for(Map.Entry<String, Integer> pattern: globalFreqPattern.entrySet()){
                if(pattern.getValue() >= globalSupport){
                    finalGlobalFreqPattern.put(pattern.getKey(), pattern.getValue());
                }
//                    System.out.println("Pattern: " + pattern.getKey() + ", Frequency: " + pattern.getValue());
            }
            System.out.println("Size of Global Frequent Pattern: " + finalGlobalFreqPattern.size());
            if(outputToFile){
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
                    for(String s: finalGlobalFreqPattern.keySet()) {
                        writer.write(s);
                        writer.newLine();
                    }
                    writer.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            HashMap<String, Integer> countEvents = new HashMap<>();
            for(String s: finalGlobalFreqPattern.keySet()) {
                String []splits = s.split(",");
                for(String ss: splits){
                    if(countEvents.containsKey(ss))
                        countEvents.put(ss, countEvents.get(ss) + 1);
                    else
                        countEvents.put(ss,1);
//                    countEvents.put(ss, countEvents.getOrDefault(ss,0) + 1);
                }
            }
            String maxStr ="";
            int maxCount = 0;
            for(Map.Entry<String, Integer> entry: countEvents.entrySet()){
                if(entry.getValue() > maxCount){
                    maxCount = entry.getValue();
                    maxStr = entry.getKey();
                }
            }
            System.out.println("Max Frequency Event: " + maxStr + "," + maxCount);
        }
    }
}
