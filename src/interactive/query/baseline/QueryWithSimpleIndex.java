//package interactive.experiments;
//
//import interactive.index.SequenceStorage;
//import inputdata.LocalParameter;
//import interactive.query.FPQuery;
//import interactive.query.SequenceScan;
//
///**
// * Created by yizhouyan on 4/30/18.
// */
//public class QueryWithSimpleIndex {
//    public static void main(String [] args){
//        SequenceStorage sequenceStorage = new SequenceStorage();
//        sequenceStorage.readInputDataFromFile(ExpParameters.exp_inputDir);
//        String [] queryStr = ExpParameters.exp_query.split(",");
//        short [] queryInShort = new short[queryStr.length];
//        for(int i = 0; i< queryStr.length; i++){
//            queryInShort[i] = Short.parseShort(queryStr[i]);
//        }
//        sequenceStorage.buildIndex(new LocalParameter(ExpParameters.exp_localSupport,
//                ExpParameters.exp_eventGap, ExpParameters.exp_seqGap, ExpParameters.exp_localSupport));
//
//
//
//
//        long startTime = System.currentTimeMillis();
//
//        FPQuery fpQuery = new FPQuery(sequenceStorage, queryInShort);
//        System.out.println("Indexing Method takes " +(System.currentTimeMillis()-startTime)* 1.0/1000 + " seconds");
//    }
//}
