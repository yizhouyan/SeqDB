package interactive.query;

import interactive.index.SequenceStorage;
import interactive.metadata.*;

import java.util.*;

/**
 * Created by yizhouyan on 4/24/18.
 */
public class FPQuery {
    private SequenceStorage sequenceStorage;
    private short[] queryStr;

    public FPQuery(SequenceStorage sequenceStorage, short[] queryStr) {
        this.sequenceStorage = sequenceStorage;
        this.queryStr = queryStr;
    }

    public long queryOnAllSequences() {
        HashMap<Integer, QueryResult> patternOccurrences = new HashMap<Integer, QueryResult>();
        long startTime = System.nanoTime();
        for (int i = 0; i < this.sequenceStorage.getInputData().getInputStringArray().size(); i++) {
            if (this.sequenceStorage.getLocalFreqPatternWrapUps().containsKey(i)) {
                SingleSeqWrapup curSequence = this.sequenceStorage.getLocalFreqPatternWrapUps().get(i);
                QueryResult curBitSetRes = queryOnSingleSequence(curSequence);
                if (curBitSetRes.getSupportCount() > 0)
                    patternOccurrences.put(i, curBitSetRes);
            } else {
                SequenceScan singleScan = new SequenceScan(sequenceStorage.getInputData().getInputStringArray().get(i),
                        sequenceStorage.getInputData().getInputTimeStamp().get(i),
                        queryStr, sequenceStorage.getLocalParameterForStorage().getItemGap(),
                        sequenceStorage.getLocalParameterForStorage().getSeqGap(),
                        sequenceStorage.getLocalParameterForStorage().getItemGapTS(),
                        sequenceStorage.getLocalParameterForStorage().getSeqGapTS());
                QueryResult curBitSetRes = singleScan.scanSequenceForMatch();
                if (curBitSetRes.getSupportCount() > 0)
                    patternOccurrences.put(i, curBitSetRes);
            }
        }
        return System.nanoTime()-startTime;
                //patternOccurrences;
    }

    public QueryResult queryOnSingleSequence(SingleSeqWrapup curSequence) {
        if (curSequence.getFreqPatternMap() == null || curSequence.getFreqPatternMap().size() == 0)
            return scanSequenceForQuery(curSequence);
        // find longest match candidate pattern
        String queryInStr = Arrays.toString(queryStr);
        if(curSequence.getFreqPatternMap().containsKey(queryInStr)){
            FreqPatternWrapup matchedFreqPattern = curSequence.getFreqPatternMap().get(queryInStr);
            QueryResultCache matchedResult = matchedFreqPattern.getCompleteQueryResult();
            return matchedResult;
        }else{
            return scanSequenceForQuery(curSequence);
        }
    }

    public QueryResult scanSequenceForQuery(SingleSeqWrapup curSequence) {
//        System.out.println("Query Method: Sequence Scan!");
        SequenceScan singleScan = new SequenceScan(curSequence.getOriginalSequence(),
                curSequence.getOriginalTimeStamps(), queryStr,
                sequenceStorage.getLocalParameterForStorage().getItemGap(),
                sequenceStorage.getLocalParameterForStorage().getSeqGap(),
                sequenceStorage.getLocalParameterForStorage().getItemGapTS(),
                sequenceStorage.getLocalParameterForStorage().getSeqGapTS());
        return singleScan.scanSequenceForMatch();
    }
}

