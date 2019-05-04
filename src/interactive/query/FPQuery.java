//package interactive.query;
//
//import interactive.index.SequenceStorage;
//import interactive.metadata.*;
//import interactive.metadata.bmcontainer.*;
//
//import java.util.*;
//
///**
// * Created by yizhouyan on 4/24/18.
// */
//public class FPQuery {
//    private SequenceStorage sequenceStorage;
//    private short[] queryStr;
//
//    public FPQuery(SequenceStorage sequenceStorage, short[] queryStr) {
//        this.sequenceStorage = sequenceStorage;
//        this.queryStr = queryStr;
//    }
//
//    public double queryOnAllSequences() {
//        long startTime = System.currentTimeMillis();
//        HashMap<Integer, QueryResult> patternOccurrences = new HashMap<Integer, QueryResult>();
//        for (int i = 0; i < this.sequenceStorage.getInputData().getInputStringArray().size(); i++) {
//            if (this.sequenceStorage.getLocalFreqPatternWrapUps().containsKey(i)) {
//                // get pattern occurrence on each single sequence
//                QueryResult curBitSetRes = queryOnSingleSequence(this.sequenceStorage.getLocalFreqPatternWrapUps().get(i));
//                if (curBitSetRes.getSupportCount() > 0)
//                    patternOccurrences.put(i, curBitSetRes);
//            } else {
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
//        return (System.currentTimeMillis() - startTime) * 1.0 / 1000;
//                //patternOccurrences;
//    }
//
//    public QueryResult queryOnSingleSequence(SingleSeqWrapup curSequence) {
//        if (curSequence.getFreqPatternTrie() == null)
//            return scanSequenceForQuery(curSequence);
//        // find longest match candidate pattern
//        FreqPatternWrapup matchedFreqPattern = curSequence.findLongestPrefixesInTrie(queryStr);
//        if (matchedFreqPattern == null)
//            return scanSequenceForQuery(curSequence);
//        else{
//            QueryResultCache matchedResult = matchedFreqPattern.getCompleteQueryResult(curSequence.getSeqLength(), curSequence.getOriginalSequence(),
//                    curSequence.getOriginalTimeStamps(), sequenceStorage.getLocalParameterForStorage().getItemGap(),
//                    sequenceStorage.getLocalParameterForStorage().getSeqGap(),
//                    sequenceStorage.getLocalParameterForStorage().getItemGapTS(),
//                    sequenceStorage.getLocalParameterForStorage().getSeqGapTS());
//            if(matchedFreqPattern.getPatternLength() == this.queryStr.length) {
//                return matchedResult;
//            }
//            else {
//                return findPatternOccurrenceFromFreq(curSequence, matchedResult);
//            }
//        }
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
//    public QueryResult findPatternOccurrenceFromFreq(SingleSeqWrapup curSequence, QueryResultCache matchedResult) {
//        if (curSequence.getSeqLength() > Constants.SHORT_MAX_LIMIT + 1)
//            return findPatternOccFromFreqLong_forward(curSequence, matchedResult);
//        else
//            return findPatternOccurrenceFromFreqForShort(curSequence, matchedResult);
//    }
//
//    public QueryResult findPatternOccFromFreqLong_forward(SingleSeqWrapup curSequence, QueryResultCache matchedResult) {
////        System.out.println("Query Method: Search from frequent pattern (for long)!");
//        int bitsetSize = curSequence.getSeqLength();
//        ArrayList<IBMContainer> newStartPointsCL = new ArrayList<>();
//        int numSupport = 0;
//
//        int itemGap = sequenceStorage.getLocalParameterForStorage().getItemGap();
//        long itemGapTS = sequenceStorage.getLocalParameterForStorage().getItemGapTS();
//        int seqGap = sequenceStorage.getLocalParameterForStorage().getSeqGap();
//        long seqGapTS = sequenceStorage.getLocalParameterForStorage().getSeqGapTS();
//
//        ContainerList oldStartsCL = (ContainerList) matchedResult.getStartPoints();
//
//
//        for (int index = 0; index < oldStartsCL.numContainers(); index++) {
//            // for each container, find the infrequent pattern after each occurrence
//            BitSet matchPatternStarts = ((BitsetContainer) oldStartsCL.getBitsetContainerList().get(index)).getBitset();
//            int curBitsetSize = (index == oldStartsCL.numContainers() - 1) ?
//                    (bitsetSize % (Constants.SHORT_MAX_LIMIT + 1)) :
//                    (Constants.SHORT_MAX_LIMIT + 1);
//            int start = index * (Constants.SHORT_MAX_LIMIT +1);
//            int end = Math.max((index+1) * (Constants.SHORT_MAX_LIMIT + 1), bitsetSize);
//            short [] subOriginalSeq = Arrays.copyOfRange(curSequence.getOriginalSequence(), start, end);
//            long [] subOriginalTS = Arrays.copyOfRange(curSequence.getOriginalTimeStamps(), start, end);
//            BitSet newStartPoints = new BitSet(curBitsetSize);
//            newStartPoints.set(0, curBitsetSize, false);
//
//            int containerSupport = matchPatternStarts.cardinality();
//            int nextStartPos = 0;
//            int curStartPoint = matchPatternStarts.nextSetBit(nextStartPos);
//            nextStartPos = curStartPoint + 1;
//            int nextStartPoint = (matchPatternStarts.cardinality() > 1) ? (matchPatternStarts.nextSetBit(nextStartPos)) : curBitsetSize;
//            int i = 0;
//
//            while (i < containerSupport) {
//                int curIndex = curStartPoint;
////                while(curIndex <= nextStartPoint-1 && subOriginalSeq[curIndex] != queryStr[0]){
////                    curIndex++;
////                }
//                if(subOriginalSeq[curIndex] != queryStr[0]) {
//                    System.out.println("Start point not found in super pattern (in FPQuery)!");
//                    continue;
//                }
//                int firstIndex = curIndex;
//                long firstTS = subOriginalTS[firstIndex];
//                int prevIndex = firstIndex;
//                long prevTS = subOriginalTS[firstIndex];
//                int matchPtr = 1;
//                curIndex = firstIndex + 1;
//
//                while(curIndex <= nextStartPoint - 1 && matchPtr < queryStr.length) {
//                    short curEle = subOriginalSeq[curIndex];
//                    long curTS = subOriginalTS[curIndex];
//                    if ((curIndex - firstIndex > seqGap + 1)
//                            || (curIndex - prevIndex > itemGap + 1)
//                            || (curTS - firstTS > seqGapTS + 1)
//                            || (curTS - prevTS > itemGapTS + 1)){
//                        // cannot accept this new element because it violates the gap constraint
//                        break;
//                    }
//                    if(curEle == queryStr[matchPtr]){
//                        prevIndex = curIndex;
//                        prevTS = curTS;
//                        matchPtr++;
//                    }
//                    curIndex++;
//                }
//                if(curIndex > curBitsetSize-1 && index < oldStartsCL.numContainers()-1 && matchPtr < queryStr.length){
//                    // part of the match in the next container
//                    BitSet nextMatchPatternStarts = ((BitsetContainer) oldStartsCL.getBitsetContainerList().get(index + 1)).getBitset();
//                    int firstStartPoint = nextMatchPatternStarts.nextSetBit(0);
//                    int nextBitsetSize = (index + 1 == oldStartsCL.numContainers() - 1) ?
//                            (bitsetSize % (Constants.SHORT_MAX_LIMIT + 1)) :
//                            (Constants.SHORT_MAX_LIMIT + 1);
//                    firstStartPoint = (firstStartPoint == -1) ? nextBitsetSize: firstStartPoint;
//
//                    int nextStart = (index+1) * (Constants.SHORT_MAX_LIMIT +1);
//                    int nextEnd = Math.min((index+2) * (Constants.SHORT_MAX_LIMIT + 1), bitsetSize);
//                    short [] nextOriginalSeq = Arrays.copyOfRange(curSequence.getOriginalSequence(), nextStart, nextEnd);
//                    long [] nextOriginalTS = Arrays.copyOfRange(curSequence.getOriginalTimeStamps(), nextStart, nextEnd);
//
//                    int nextIndex = 0;
//                    prevIndex = prevIndex-(Constants.SHORT_MAX_LIMIT +1);
//                    int subFirstIndex = firstIndex - (Constants.SHORT_MAX_LIMIT + 1);
//
//                    while(nextIndex <= firstStartPoint-1 && matchPtr < queryStr.length){
//                        short curEle = nextOriginalSeq[nextIndex];
//                        long curTS = nextOriginalTS[nextIndex];
//                        if ((nextIndex - subFirstIndex > seqGap + 1)
//                                || (nextIndex - prevIndex > itemGap + 1)
//                                || (curTS - firstTS > seqGapTS + 1)
//                                || (curTS - prevTS > itemGapTS + 1)){
//                            // cannot accept this new element because it violates the gap constraint
//                            break;
//                        }
//                        if(curEle == queryStr[matchPtr]){
//                            prevIndex = nextIndex;
//                            prevTS = curTS;
//                            matchPtr++;
//                        }
//                        nextIndex ++;
//                    }
//                }
//                if(matchPtr == queryStr.length) {
//                    newStartPoints.set(firstIndex, true);
//                    numSupport++;
//                }
//                i++;
//                curStartPoint = nextStartPoint;
//                nextStartPos = curStartPoint + 1;
//                nextStartPoint = (i >= matchPatternStarts.cardinality() - 1) ? curBitsetSize : (matchPatternStarts.nextSetBit(nextStartPos));
//            }
//            newStartPointsCL.add(new BitsetContainer(newStartPoints));
//        }
//        return new QueryResultCache(numSupport, new ContainerList(newStartPointsCL), queryStr.length);
//    }
//
//    public BitSet copyBitSetSegment(BitSet segment, BitSet target, int startPos) {
//        int pos = 0;
//        while (pos < segment.length()) {
//            int nextSet = segment.nextSetBit(pos);
//            target.set(nextSet + startPos, true);
//            pos = nextSet + 1;
//        }
//        return target;
//    }
//
//    public QueryResult findPatternOccurrenceFromFreqForShort(SingleSeqWrapup curSequence, QueryResultCache matchedResult) {
////        System.out.println("Query Method: Search from frequent pattern!");
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
//        BitSet matchPatternStarts = ((BitsetContainer) matchedResult.getStartPoints()).getBitset();
//        int nextStartPos = 0;
//        int curStartPoint = matchPatternStarts.nextSetBit(nextStartPos);
//        nextStartPos = curStartPoint + 1;
//        int nextStartPoint = (matchPatternStarts.cardinality() > 1) ? (matchPatternStarts.nextSetBit(nextStartPos)) : bitsetSize;
//        int i = 0;
//        while (i < matchPatternStarts.cardinality()) {
//            // find first element
//            int curIndex = curStartPoint;
////            while(curIndex <= nextStartPoint-1 && originalSeq[curIndex] != queryStr[0]){
////                curIndex++;
////            }
//            if(originalSeq[curIndex] != queryStr[0]) {
//                System.out.println("Start point not found in super pattern (in FPQuery)!");
//                continue;
//            }
//            int firstIndex = curIndex;
//            long firstTS = originalTS[firstIndex];
//            int prevIndex = firstIndex;
//            long prevTS = originalTS[firstIndex];
//            int matchPtr = 1;
//            curIndex = firstIndex + 1;
//
//            while(curIndex <= nextStartPoint - 1 && matchPtr < queryStr.length) {
//                short curEle = originalSeq[curIndex];
//                long curTS = originalTS[curIndex];
//                if ((curIndex - firstIndex > seqGap + 1)
//                        || (curIndex - prevIndex > itemGap + 1)
//                        || (curTS - firstTS > seqGapTS + 1)
//                        || (curTS - prevTS > itemGapTS + 1)){
//                    // cannot accept this new element because it violates the gap constraint
//                    break;
//                }
//                if(curEle == queryStr[matchPtr]){
//                    prevIndex = curIndex;
//                    prevTS = curTS;
//                    matchPtr++;
//                }
//                curIndex++;
//            }
//            if(matchPtr == queryStr.length)
//                newStartPoints.set(firstIndex, true);
//            i++;
//            curStartPoint = nextStartPoint;
//            nextStartPos = curStartPoint + 1;
//            nextStartPoint = (i >= matchPatternStarts.cardinality() - 1) ? bitsetSize : (matchPatternStarts.nextSetBit(nextStartPos));
//        }
//        return new QueryResultCache(newStartPoints.cardinality(), new BitsetContainer(newStartPoints), queryStr.length);
//    }
//}
//
