//package interactive.query;
//
//import interactive.exception.IncorrectStartPointException;
//import interactive.index.SequenceStorage;
//import interactive.metadata.FreqPatternWrapup;
//import interactive.metadata.QueryResult;
//import interactive.metadata.QueryResultCache;
//import interactive.metadata.SingleSeqWrapup;
//
//import java.util.BitSet;
//import java.util.HashMap;
//
///**
// * Created by yizhouyan on 4/24/18.
// */
//public class FPQueryWithGroupTries {
//    private SequenceStorage sequenceStorage;
//    private short[] queryStr;
//
//    public FPQueryWithGroupTries(SequenceStorage sequenceStorage, short[] queryStr) {
//        this.sequenceStorage = sequenceStorage;
//        this.queryStr = queryStr;
//    }
//
//    public double queryOnAllSequencesWithOneTrie() {
//        long startTime = System.currentTimeMillis();
//        HashMap<Integer, QueryResult> patternOccurrences = new HashMap<Integer, QueryResult>();
//        HashMap<Integer, FreqPatternWrapup> patternWrapupHashMap =
//                this.sequenceStorage.getFreqPatternTrieGroup().findMatchFreqPatternForDevices(queryStr);
//
//        for (int i = 0; i < this.sequenceStorage.getInputData().getInputStringArray().size(); i++) {
////            System.out.println("i = " + i);
//            if (patternWrapupHashMap.containsKey(i)) {
//                // get pattern occurrence on each single sequence
//                QueryResult curBitSetRes = queryOnSingleSequence(this.sequenceStorage.getLocalFreqPatternWrapUps().get(i),
//                        patternWrapupHashMap.get(i));
//                if (curBitSetRes.getSupportCount() > 0)
//                    patternOccurrences.put(i, curBitSetRes);
//            }
//            else {
//                SequenceScan singleScan = new SequenceScan(sequenceStorage.getInputData().getInputStringArray().get(i),
//                        sequenceStorage.getInputData().getInputTimeStamp().get(i),
//                        queryStr, sequenceStorage.getLocalParameterForStorage().getItemGap(),
//                        sequenceStorage.getLocalParameterForStorage().getSeqGap(),
//                        sequenceStorage.getLocalParameterForStorage().getItemGapTS(),
//                        sequenceStorage.getLocalParameterForStorage().getSeqGapTS());
//                QueryResult curBitSetRes = singleScan.scanSequenceForMatch();
//                if (curBitSetRes.getSupportCount() > 0)
//                    patternOccurrences.put(i, curBitSetRes);
//            }
//        }
////        System.out.println("Pattern query takes " + (System.currentTimeMillis() - startTime) * 1.0 / 1000 + " seconds");
//        //TODO save current query result to cache
//        return (System.currentTimeMillis() - startTime) * 1.0 / 1000;
//        //patternOccurrences;
//    }
//
//    public QueryResult queryOnSingleSequence(SingleSeqWrapup curSequence, FreqPatternWrapup bestMatchPattern) {
//            int bestMatchStartPos = 0;
////            System.out.println("Best Match Frequent Pattern: " + bestMatchPattern.printPattern(curSequence.getSeqLength()));
////            return scanSequenceForQuery(curSequence);
//            QueryResultCache matchedResult = bestMatchPattern.getCompleteQueryResult(curSequence.getSeqLength(), curSequence.getOriginalSequence(),
//                    curSequence.getOriginalTimeStamps(), sequenceStorage.getLocalParameterForStorage().getItemGap(),
//                    sequenceStorage.getLocalParameterForStorage().getSeqGap(),
//                    sequenceStorage.getLocalParameterForStorage().getItemGapTS(),
//                    sequenceStorage.getLocalParameterForStorage().getSeqGapTS());
//            if(bestMatchPattern.getPatternLength() == this.queryStr.length) {
//                return matchedResult;
//            }
//            else {
//                return findPatternOccurrenceFromFreq(curSequence, matchedResult, bestMatchStartPos);
//            }
//    }
//
//    public QueryResult scanSequenceForQuery(SingleSeqWrapup curSequence) {
////        System.out.println("Query Method: Sequence Scan!");
//        SequenceScan singleScan = new SequenceScan(curSequence.getOriginalSequence(),
//                curSequence.getOriginalTimeStamps(), queryStr,
//                sequenceStorage.getLocalParameterForStorage().getItemGap(),
//                sequenceStorage.getLocalParameterForStorage().getSeqGap(),
//                sequenceStorage.getLocalParameterForStorage().getItemGapTS(),
//                sequenceStorage.getLocalParameterForStorage().getSeqGapTS());
//        return singleScan.scanSequenceForMatch();
//    }
//
//    public QueryResult findPatternOccurrenceFromFreq(SingleSeqWrapup curSequence, QueryResultCache matchedResult,
//                                                     int bestMatchStartPos) {
//        int bitsetSize = curSequence.getSeqLength();
//        BitSet newStartPoints = new BitSet(bitsetSize);
//        newStartPoints.set(0, bitsetSize, false);
//
//        int itemGap = sequenceStorage.getLocalParameterForStorage().getItemGap();
//        long itemGapTS = sequenceStorage.getLocalParameterForStorage().getItemGapTS();
//        int seqGap = sequenceStorage.getLocalParameterForStorage().getSeqGap();
//        long seqGapTS = sequenceStorage.getLocalParameterForStorage().getSeqGapTS();
//
//        short [] originalSeq = curSequence.getOriginalSequence();
//        long [] originalTS = curSequence.getOriginalTimeStamps();
//
//        BitSet matchPatternStarts = matchedResult.getStartPoints();
//        int nextStartPos = 0;
//        int curStartPoint = matchPatternStarts.nextSetBit(nextStartPos);
//        nextStartPos = curStartPoint + 1;
//        int prevStartPoint = -1;
//        int nextStartPoint = (matchPatternStarts.cardinality() > 1) ? (matchPatternStarts.nextSetBit(nextStartPos)) : bitsetSize;
//        int i = 0;
//
//        while (i < matchPatternStarts.cardinality()) {
//            // find first element
//            int curIndex = curStartPoint;
//            if (originalSeq[curIndex] != queryStr[bestMatchStartPos]) {
//                throw new IncorrectStartPointException();
//            }
//            int firstIndex = curIndex;
//            long firstTS = originalTS[firstIndex];
//            int prevIndex = firstIndex;
//            long prevTS = originalTS[firstIndex];
//            curIndex = firstIndex - 1;
//            int matchPtr = bestMatchStartPos - 1;
//            int savingStartPoint = firstIndex;
//            long savingStartTS = originalTS[firstIndex];
//            // traverse backward
//            while (curIndex >= prevStartPoint + 1 && matchPtr >= 0) {
//                short curEle = originalSeq[curIndex];
//                long curTS = originalTS[curIndex];
//                if ((firstIndex - curIndex > seqGap + 1)
//                        || (prevIndex - curIndex > itemGap + 1)
//                        || (firstTS - curTS > seqGapTS + 1)
//                        || (prevTS - curTS > itemGapTS + 1)) {
//                    // cannot accept this new element because it violates the gap constraint
//                    break;
//                }
//
//                if (curEle == queryStr[matchPtr]) {
//                    prevIndex = curIndex;
//                    savingStartPoint = curIndex;
//                    savingStartTS = originalTS[savingStartPoint];
//                    prevTS = curTS;
//                    matchPtr--;
//                }
//                curIndex--;
//            } // end traverse backward
//            if (matchPtr == -1) {
//                prevIndex = firstIndex;
//                prevTS = originalTS[firstIndex];
//                curIndex = firstIndex + 1;
//                matchPtr = bestMatchStartPos + 1;
//                while (curIndex <= nextStartPoint - 1 && matchPtr < queryStr.length) {
//                    short curEle = originalSeq[curIndex];
//                    long curTS = originalTS[curIndex];
//                    if ((curIndex - savingStartPoint > seqGap + 1)
//                            || (curIndex - prevIndex > itemGap + 1)
//                            || (curTS - savingStartTS > seqGapTS + 1)
//                            || (curTS - prevTS > itemGapTS + 1)) {
//                        // cannot accept this new element because it violates the gap constraint
//                        break;
//                    }
//                    if (curEle == queryStr[matchPtr]) {
//                        prevIndex = curIndex;
//                        prevTS = curTS;
//                        matchPtr++;
//                    }
//                    curIndex++;
//                }
//                if (matchPtr == queryStr.length)
//                    newStartPoints.set(savingStartPoint, true);
//            }
//
//            i++;
//            prevStartPoint = curStartPoint;
//            curStartPoint = nextStartPoint;
//            nextStartPos = curStartPoint + 1;
//            nextStartPoint = (i >= matchPatternStarts.cardinality() - 1) ? bitsetSize : (matchPatternStarts.nextSetBit(nextStartPos));
//        }
//        return new QueryResultCache(newStartPoints.cardinality(), newStartPoints, queryStr.length);
//    }
//}
//
