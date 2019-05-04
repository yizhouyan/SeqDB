package interactive.remote;

import interactive.index.SequenceStorage;
//import interactive.metadata.BasicStatistics;
import interactive.metadata.SingleSeqWrapup;
import interactive.metadata.FreqPatternWrapup;
import inputdata.LocalParameter;
//import interactive.mining.index.FPMining;
//import interactive.query.FPQueryWithGroupTries;
//import interactive.query.FPQueryWithOneTrie;
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
//            for (int i = 2; i < 30; i++) {
//                localSupport = i;
//                FPMining fpMining = new FPMining(this.sequenceStorage, localSupport, itemGap, seqGap, itemGapTS, seqGapTS);
//                long curTime = 0;
//                if(methodType == 0)
//                    curTime = fpMining.miningContextualLocalFreqPatternsOnPrev();
//                else if(methodType == 1)
//                    curTime = fpMining.miningMaxLocalFreqPatternsOnPrev();
//                else if(methodType == 2)
//                    curTime = fpMining.miningCloseLocalFreqPatternsOnPrev();
//                minTime = Math.min(curTime, minTime);
//                maxTime = Math.max(curTime, maxTime);
//                sumTime += curTime;
//            }
            System.out.println("Min Time: " + minTime * 1.0 / 1000);
            System.out.println("Max Time: " + maxTime * 1.0 / 1000);
            System.out.println("Sum Time: " + sumTime * 1.0 / 1000);
            System.out.println("Average Time: " + sumTime * 1.0 / (1000 * 28));
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
    public void executeQuery() throws RemoteException {
        ArrayList<Double> timeCosts = new ArrayList<>();
//        ArrayList<String> queries = InteractiveToolkit.generateQuery();
//        ArrayList<String> queries = InteractiveToolkit.getQuerysFromFile("GlobalFrequent_logFile");
        ArrayList<String> queries = InteractiveToolkit.getQuerysFromFile("GlobalFrequent");
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
//                FPQueryWithOneTrie fpQuery = new FPQueryWithOneTrie(this.sequenceStorage, queryInShort);
////                FPQueryWithGroupTries fpQuery = new FPQueryWithGroupTries(this.sequenceStorage, queryInShort);
//////                FPQuery fpQuery = new FPQuery(this.sequenceStorage, this.sequenceStorage.getInputData().translateQuery(qry));
//                double timeCost1 = fpQuery.queryOnAllSequencesWithOneTrie();
//                double timeCost2 = fpQuery.queryOnAllSequencesWithOneTrie();
//                double timeCost3 = fpQuery.queryOnAllSequencesWithOneTrie();
//                double timeCost = (timeCost1 + timeCost2 + timeCost3) / 3;
//                timeCosts.add(timeCost);
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Results_indexquery_globalFrequent_onetrie"));
//            BufferedWriter writer = new BufferedWriter(new FileWriter("Results_indexquery_globalFrequent_logFile_synthetic_opt"));
            for(Double d: timeCosts) {
                writer.write(d+"");
                writer.newLine();
            }
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void getGlobalFreqPattern(int globalSupport, boolean outputToFile) {
        if(checkStorageAvailability()) {
            HashMap<String, Integer> globalFreqPattern = new HashMap<>();
            for(Map.Entry<Integer, SingleSeqWrapup> entry:sequenceStorage.getLocalFreqPatternWrapUps().entrySet()){
                for(FreqPatternWrapup pattern: entry.getValue().getFreqPatternList()) {
                    if (globalFreqPattern.containsKey(pattern.getPatternInString()))
                        globalFreqPattern.put(pattern.getPatternInString(), globalFreqPattern.get(pattern.getPatternInString())+1);
                    else{
                        globalFreqPattern.put(pattern.getPatternInString(),1);
                    }
                }
            }
            for(Map.Entry<String, Integer> pattern: globalFreqPattern.entrySet()){
                if(pattern.getValue() >= globalSupport)
                    System.out.println("Pattern: " + pattern.getKey() + ", Frequency: " + pattern.getValue());
            }
            if(outputToFile){
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("GlobalFrequent_logFile"));
                    for(String s: globalFreqPattern.keySet()) {
                        writer.write(s);
                        writer.newLine();
                    }
                    writer.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            HashMap<String, Integer> countEvents = new HashMap<>();
            for(String s: globalFreqPattern.keySet()) {
                String []splits = s.split(",");
                for(String ss: splits){
                    countEvents.put(ss, countEvents.getOrDefault(ss,0) + 1);
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
