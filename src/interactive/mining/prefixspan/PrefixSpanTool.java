package interactive.mining.prefixspan;

import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.InvertedIndex;
import interactive.metadata.SingleSeqWrapup;
//import interactive.metadata.FreqPatternRelation;
import inputdata.LocalParameter;

import java.util.*;

public class PrefixSpanTool {
    // orginal sequence data, used to generate totalSeqences
    private ArrayList<Short> InputTimeSequence;
    // orginal index (since we truncate the string, we have to save the previous
    // index)
    private ArrayList<Integer> originalIndexes;
    // mininal support
    private int minSupport;
    // maximum gap between items
    private int itemGap;
    // maximum gap between the first item and the last item
    private int seqGap;
    private long itemGapTS;
    private long seqGapTS;
    // save all frequent sequences
//    private HashMap<Short, ArrayList<FreqSequence>> totalFrequentSeqs = new HashMap<Short, ArrayList<FreqSequence>>();
    private ArrayList<FreqSequence> totalFrequentSeqs = new ArrayList<>();
    // All single items, used for enumerate
    private ArrayList<Short> singleItems;
    private int maxLengthOriginal = 0;
    private short[] originalSequence;
    private long [] originalTimeStamps;

    public PrefixSpanTool(short[] originalSeq, long [] originalTimeStamps, LocalParameter localParameter) {
        this.originalSequence = originalSeq;
        this.originalTimeStamps = originalTimeStamps;
        this.minSupport = localParameter.getLocalSupport();
        this.itemGap = localParameter.getItemGap();
        this.seqGap = localParameter.getSeqGap();
        this.itemGapTS = localParameter.getItemGapTS();
        this.seqGapTS = localParameter.getSeqGapTS();
    }

    /**
     * Remove single item that does not satisfy minSupport
     */
    private void removeInitSeqsItem() {
        HashMap<Short, Integer> itemMap = new HashMap<>();
        this.singleItems = new ArrayList<>();
        this.maxLengthOriginal = originalSequence.length;
        for (short temp : originalSequence) {
            if (!itemMap.containsKey(temp))
                itemMap.put(temp, 1);
            else
                itemMap.put(temp, itemMap.get(temp) + 1);
        }

        for (Map.Entry entry : itemMap.entrySet()) {
            short key = (Short) entry.getKey();
            int count = (int) entry.getValue();

            if (count >= minSupport) {
                singleItems.add(key);
            }
        }
        // to do : modify input string: remove all infrequent items
        ArrayList<Short> newSequence = new ArrayList<>();
        this.originalIndexes = new ArrayList<Integer>();

        for (int i = 0; i < originalSequence.length; i++) {
            if (singleItems.contains(originalSequence[i])) {
                newSequence.add(originalSequence[i]);
                originalIndexes.add(i);
            }
        }
        this.InputTimeSequence = newSequence;
        // System.out.println("New String: " + this.InputTimeSequence);
    }

    /**
     * generate set of sequences from original string
     */
    private ArrayList<Sequence> generateSeqencesWithPrefix(short prefix) {
        ArrayList<Sequence> sequenceArray = new ArrayList<Sequence>();
        ArrayList<ItemPair> previousSeq = new ArrayList<ItemPair>();

        int startIndex = 0;
        // first search for starting index
        for (int i = 0; i < InputTimeSequence.size(); i++) {
            if (InputTimeSequence.get(i) == prefix) {
                startIndex = i;
                break;
            }
        }
        for (int i = startIndex; i < InputTimeSequence.size(); i++) {
            if (InputTimeSequence.get(i) == prefix && previousSeq.size() != 0) {
                // save previous sequence to the final sequence array
                sequenceArray.add(new Sequence((ArrayList<ItemPair>) previousSeq.clone()));
                previousSeq.clear();
            }
            previousSeq.add(new ItemPair(InputTimeSequence.get(i) , this.originalIndexes.get(i)));
        }
        if (!previousSeq.isEmpty())
            sequenceArray.add(new Sequence((ArrayList<ItemPair>) previousSeq.clone()));
        return sequenceArray;
    }

    /**
     * print a sequence list
     */
    private void printSeqList(ArrayList<Sequence> seqList) {
        for (Sequence seq : seqList) {
            for (ItemPair item : seq.getItemPairList()) {
                System.out.print(item.getItem() + "," + item.getIndex() + " ");
            }
            System.out.println();
        }
    }

    private boolean findMatchSequence(short s, ArrayList<Sequence> seqList, ArrayList<Integer> tempFS,
                                      ArrayList<Boolean> ifHasNewItemInPrevSeq, ArrayList<Integer> lastIndexes, ArrayList<Integer> firstIndexes) {
        boolean isLarge = false;
        int count = 0;

        // for (Sequence seq : seqList) {
        for (int i = 0; i < seqList.size(); i++) {
            if (seqList.get(i).strIsContained(s, tempFS, true, lastIndexes.get(i), firstIndexes.get(i),
                    originalTimeStamps, this.itemGap, this.seqGap, this.itemGapTS, this.seqGapTS)) {
                count++;
                ifHasNewItemInPrevSeq.add(true);
            } else
                ifHasNewItemInPrevSeq.add(false);
        }

        if (count >= minSupport) {
            isLarge = true;
        }

        return isLarge;
    }

    private boolean findMatchSequence(short s, ArrayList<Sequence> seqList, ArrayList<Integer> tempFS) {
        boolean isLarge = false;
        int count = 0;

        for (Sequence seq : seqList) {
            if (seq.strIsContained(s, tempFS, true)) {
                count++;
            }
        }

        if (count >= minSupport) {
            isLarge = true;
        }

        return isLarge;
    }

//    public void addToFrequentSequenceMap(short prefix, FreqSequence freqSequence) {
//        if (totalFrequentSeqs.containsKey(prefix)) {
//            totalFrequentSeqs.get(prefix).add(0, freqSequence);
//        } else {
//            ArrayList<FreqSequence> freqSequencesList = new ArrayList<>();
//            freqSequencesList.add(0, freqSequence);
//            totalFrequentSeqs.put(prefix, freqSequencesList);
//        }
//    }

    public void addToFrequentSequence(FreqSequence freqSequence) {
        totalFrequentSeqs.add(freqSequence);
    }

    /**
     * frequent pattern mining for each single item, generate sequence sets from
     * the original string then mining each sequence separately
     */
    private void frequentSequenceMining() {
        for (short currentPrefix : singleItems) {
            ArrayList<Sequence> currentSeqArray = generateSeqencesWithPrefix(currentPrefix);
            // printSeqList(currentSeqArray);
            ArrayList<Integer> tempFS = new ArrayList<Integer>();
            boolean isLargerThanSup = findMatchSequence(currentPrefix, currentSeqArray, tempFS);
            if (isLargerThanSup) {
                // add the new frequent sequence and indexes to the final list
                FreqSequence newFS = new FreqSequence();
                ResItemArrayPair newPair = new ResItemArrayPair(currentPrefix);
                newPair.setIndexes(tempFS);
                newFS.addItemToSequence(newPair);
//                addToFrequentSequence(newFS);

                // truncate current sequences and generate new available
                // sequence list
                Sequence tempSeq;
                ArrayList<Sequence> tempSeqList = new ArrayList<>();
                for (Sequence s2 : currentSeqArray) {
                    // check if the sequence contains currentPrefix
                    if (s2.strIsContained(currentPrefix, tempFS, false)) {
                        tempSeq = s2.extractItem(currentPrefix);
                        tempSeqList.add(tempSeq);
                    }
                }
                ArrayList<Short> newSingleItems = new ArrayList<Short>(singleItems);
                newSingleItems.remove(Short.valueOf(currentPrefix));
                recursiveSearchSeqs(newFS, tempSeqList, newSingleItems, tempFS, currentPrefix);
            }
        }
    }

    /**
     * recursively search for sequences
     *
     * @param beforeSeq      previous generate frequent sequence
     * @param afterSeqList   the sequences after filtering
     * @param newSingleItems the sequences without the first prefix
     * @param lastIndexes    the indexes of previous item, used for gap...if(current
     *                       -previous index) < gap, then stop
     */
    private void recursiveSearchSeqs(FreqSequence beforeSeq, ArrayList<Sequence> afterSeqList,
                                     ArrayList<Short> newSingleItems, ArrayList<Integer> lastIndexes,
                                     short currentPrefix) {
        ItemPair tempItemSet;
        Sequence tempSeq2;

        for (short s : newSingleItems) {
            ArrayList<Integer> tempFS = new ArrayList<Integer>();
            ArrayList<Boolean> ifHasNewItemInPrevSeq = new ArrayList<Boolean>();
            boolean isLargerThanSup = findMatchSequence(s, afterSeqList, tempFS, ifHasNewItemInPrevSeq, lastIndexes,
                    beforeSeq.getItemPairList().get(0).index);
            if (isLargerThanSup) {
                // add the new frequent sequence and indexes to the final list

                FreqSequence newFS = beforeSeq.copyFreqSeqence(ifHasNewItemInPrevSeq);
                ResItemArrayPair newPair = new ResItemArrayPair(s);
                newPair.setIndexes(tempFS);
                newFS.addItemToSequence(newPair);
                addToFrequentSequence(newFS);

                // truncate current sequences and generate new available
                // sequence list
                Sequence tempSeq;
                ArrayList<Sequence> tempSeqList = new ArrayList<>();
                // for (Sequence s2 : afterSeqList) {
                for (int i = 0; i < afterSeqList.size(); i++) {
                    // check if the sequence contains currentPrefix
                    if (afterSeqList.get(i).strIsContained(s, tempFS, false, lastIndexes.get(i),
                            beforeSeq.getItemPairList().get(0).index.get(i), originalTimeStamps, this.itemGap, this.seqGap,
                            this.itemGapTS, this.seqGapTS)) {
                        tempSeq = afterSeqList.get(i).extractItem(s);
                        tempSeqList.add(tempSeq);
                    }
                }
                recursiveSearchSeqs(newFS, tempSeqList, newSingleItems, tempFS, currentPrefix);
            }
        }
    }

    /**
     * frequent sequence mining
     */
    public void prefixSpanCalculate() {
        removeInitSeqsItem();
        frequentSequenceMining();
        // printTotalFreSeqs();
    }

    public void addToFreqPatternWithBMs(HashMap<Short, ArrayList<FreqPatternWrapup>> freqPatternWithBMs, FreqPatternWrapup curPattern){
        short prefix = curPattern.getFreqPatternInString()[0];
        if(freqPatternWithBMs.containsKey(prefix)){
            freqPatternWithBMs.get(prefix).add(0, curPattern);
        }else{
            freqPatternWithBMs.put(prefix, new ArrayList<FreqPatternWrapup>());
            freqPatternWithBMs.get(prefix).add(0, curPattern);
        }
    }

    public SingleSeqWrapup getEmptyBMTree(){
        return new SingleSeqWrapup(this.maxLengthOriginal, originalSequence, originalTimeStamps);
    }

    public SingleSeqWrapup generateBitMapTree() {
//        System.out.println(totalFrequentSeqs.size());
        SingleSeqWrapup newFreqSeqTree = new SingleSeqWrapup(this.maxLengthOriginal, originalSequence, originalTimeStamps);
        // sort current frequent pattern list in descending order (ABC --> AB --> AC --> A)
        Collections.sort(totalFrequentSeqs);
        int currentFreqSeqIndex = 0;
        int startLength = totalFrequentSeqs.get(currentFreqSeqIndex).getItemNumInFreqSeq();
        InvertedIndex freqPatternWithOccs = new InvertedIndex(100, 100);
        HashMap<FreqPatternWrapup, FreqSequence> mapToOriginalFS = new HashMap<>();
        while (currentFreqSeqIndex < totalFrequentSeqs.size()) {
            int currentLength = totalFrequentSeqs.get(currentFreqSeqIndex).getItemNumInFreqSeq();
            ArrayList<FreqPatternWrapup> tempFreqPatternWrapupList = new ArrayList<>();
            while (currentFreqSeqIndex < totalFrequentSeqs.size() && currentLength == startLength) {
                FreqSequence seq = totalFrequentSeqs.get(currentFreqSeqIndex);
                if (seq.getItemPairList().size() > 0) {
//                    FreqPatternWrapup currentFPBM =
                    generateFreqPatternWithRelation(seq, freqPatternWithOccs,
                            tempFreqPatternWrapupList, mapToOriginalFS, newFreqSeqTree);
                }
                currentFreqSeqIndex++;
                if (currentFreqSeqIndex == totalFrequentSeqs.size())
                    break;
                currentLength = totalFrequentSeqs.get(currentFreqSeqIndex).getItemNumInFreqSeq();
            }
            if (tempFreqPatternWrapupList.size() > 0) {
                 freqPatternWithOccs.addFreqSeqToList(tempFreqPatternWrapupList);
            }
            startLength--;
        }
        newFreqSeqTree.sortFreqPatternBySupport();
        return newFreqSeqTree;
    }

    public void generateFreqPatternWithRelation(FreqSequence currentFreqSeq,
                                                             InvertedIndex existingFreqPatterns,
                                                             ArrayList<FreqPatternWrapup> tempFreqPatternWrapupList,
                                                             HashMap<FreqPatternWrapup, FreqSequence> mapToOriginalFS,
                                                             SingleSeqWrapup newFreqSeqTree) {
//        System.out.println("generate isolate frequent pattern bitmap for " + currentFreqSeq.getFreqSeqInString());
        ArrayList<ResItemArrayPair> currentItemArrayPair = currentFreqSeq.getItemPairList();
        FreqPatternWrapup newFreqPatternWrapup = new FreqPatternWrapup(currentFreqSeq.getFreqSeqInString());
        short [] currentSeq = currentFreqSeq.getFreqSeqInShortArray();
        ArrayList<FreqPatternWrapup> superPatterns = existingFreqPatterns.getSequencesWithAllElements(currentSeq);
        // remove start points that are included in super patterns
        ArrayList<Integer> curStartPoints = currentItemArrayPair.get(0).index;
        newFreqPatternWrapup.setAllSupport(curStartPoints.size());
        ArrayList<FreqPatternWrapup> newSuperPatterns = new ArrayList<>();
        boolean saveToIndex = true;
        for(FreqPatternWrapup prevPattern: superPatterns){
            ArrayList<Integer> matchIndexes = new ArrayList<>();
            if(shortArrayContains(prevPattern.getFreqPatternInString(), currentSeq, matchIndexes)) {
                newSuperPatterns.add(prevPattern);
                ArrayList<Integer> prevStarts = mapToOriginalFS.get(prevPattern).getItemPairList().get(matchIndexes.get(0)).index;
                for (int tempRemove : prevStarts) {
                    curStartPoints.remove(new Integer(tempRemove));
                }
                if(prevPattern.getAllSupport() == newFreqPatternWrapup.getAllSupport())
                    return;
            }
        }
        int remainingStarts = curStartPoints.size();
        newFreqPatternWrapup.setIndependentSupport(remainingStarts);
        newFreqPatternWrapup.generateBitSet(curStartPoints, this.maxLengthOriginal);
        newFreqPatternWrapup.setSuperPatterns(newSuperPatterns);

//        if(remainingStarts >= minSupport) {
        if(saveToIndex){
            tempFreqPatternWrapupList.add(newFreqPatternWrapup);
            mapToOriginalFS.put(newFreqPatternWrapup, currentFreqSeq);
        }
        newFreqSeqTree.addFreqSeq(newFreqPatternWrapup);
    }

    private boolean shortArrayContains(short [] strList1, short [] strList2, ArrayList<Integer> matchIndexes) {
        boolean isContained = false;
        for (int i = 0; i < strList1.length - strList2.length + 1; i++) {
            int k = i;
            int j = 0;
            int lastIndex = k;
            matchIndexes.clear();
            while (k < strList1.length && j < strList2.length) {
                if(k - lastIndex - 1 > itemGap)
                    break;
                if (strList1[k] == strList2[j]) {
                    lastIndex = k;
                    matchIndexes.add(k);
                    k++;
                    j++;
                } else {
                    k++;
                }
            }
            if (j == strList2.length) {
                isContained = true;
                break;
            }
        }
        return isContained;
    }

    public void printTotalFreqSeqsSimple() {
        Collections.sort(totalFrequentSeqs);
        for (FreqSequence tempSeq : totalFrequentSeqs) {
            System.out.print("<");
            for (ResItemArrayPair itemPair : tempSeq.getItemPairList()) {
                System.out.print(itemPair.item + ", ");
                // generate index
                System.out.print("[");
                for (Integer indexForLetter : itemPair.index)
                    System.out.print(indexForLetter + " ");
                System.out.print("] ");
            }
            System.out.print(">, ");
        }
        System.out.println();
    }

    /**
     * print final results
     */
//    public void printTotalFreSeqs() {
//        System.out.println("Frequent Sequence Results");
//
//        ArrayList<FreqSequence> seqList;
//        HashMap<Short, ArrayList<FreqSequence>> seqMap = totalFrequentSeqs;
//
//        int count = 0;
//        for (short s : singleItems) {
//            count = 0;
//            System.out.println();
//            System.out.println();
//
//            seqList = (ArrayList<FreqSequence>) seqMap.get(s);
//            for (FreqSequence tempSeq : seqList) {
//                count++;
//                System.out.print("<");
//                for (ResItemArrayPair itemPair : tempSeq.getItemPairList()) {
//                    System.out.print(itemPair.item + ", ");
//                    // generate index
//                    System.out.print("[");
//                    for (Integer indexForLetter : itemPair.index)
//                        System.out.print(indexForLetter + " ");
//                    System.out.print("] ");
//                }
//                System.out.print(">, ");
//
//                if (count == 5) {
//                    count = 0;
//                    System.out.println();
//                }
//            }
//        }
//    }

    public ArrayList<FreqSequence> getTotalFrequentSeqs() {
        return totalFrequentSeqs;
    }public void setTotalFrequentSeqs(ArrayList<FreqSequence> totalFrequentSeqs) {
        this.totalFrequentSeqs = totalFrequentSeqs;
    }


    public static void main(String[] args) {
        // String s =
        // "A,B,C,D,1,2,3,4,5,6,7,8,A,B,C,D,11,12,35,36,57,89,100,D,C,B,A,22,23,24,26,27,28,29,30,D,C,B,A,31,32,33,43,54,67,87,A,B,D,C";
        String s = "A,A,B,C,D,A,B,C,C,B,A,C,B,A,A,B,C";
        // String s =
        // "21,22,23,24,25,26,27,28,24,14,27,29,30,27,31,30,27,31,30,27,31,30,27,31,30,32,27,28,24,14,33,34,0,24,35,36,14,37,38,14,14,39,40,41,42,43,44,45,46,47,26,32,27,28,24,26,32,26,32,26,32,40,26,32,48,14,14,49,50,51,52,48,14,14,49,50,53,54,42,55,56,57,58,59,60,61,62,63,26,32,26,32,26,32,26,32,26,32,64,23,24,25,27,28,24,14,27,29,30,27,31,30,27,31,30,27,31,30,27,31,30,26,32,27,28,24,14,27,28,24,14,27,29,30,27,31,30,27,31,30,33,34,0,24,35,36,14,37,38,14,40,41,14,39,47,42,43,44,45,46,26,32,27,28,24,26,32,26,32,26,32,40,26,32,48,14,14,49,50,51,52,48,14,14,49,50,53,54,42,55,56,57,58,59,63,60,61,62,26,32,26,32,26,32,26,32,26,32,64,26,23,24,25,27,28,24,14,27,29,30,27,31,30,27,31,30,27,31,30,32,27,31,30,33,34,0,24,35,36,14,37,38,14,40,41,65,14,66,42,43,67,67,44,45,46,47,68,69,70,26,32,71,72,73,71,72,73,0,74,75,76,77,78,79,78,79,80,26,32,81,69,4,11,76,21,22,26,32,68,21,22,68,21,22,77,78,79,78,79,80,26,32,68,21,22,68,40,21,22,77,78,79,78,79,80,26,32,68,21,22,48,14,14,82,50,51,52,48,14,14,82,50,53,54,42,55,56,57,58,59,68,63,21,22,60,61,62,68,77,78,79,78,79,78,79,80,26,32,21,22,68,21,22,68,21,22,77,78,79,78,79,80,26,32,68,21,22,68,21,77,78,79,78,79,80,26,32,22,68,21,22,68,21,22,77,78,79,78,79,80,26,32,68,21,22,68,21,22,77,78,79,78,79,80,26,32,64,27,28,24,14,68,83,84,69,77,78,79,80,26,32,4,11,21,22";

        int localSupport = 2;
        int itemGap = 3;
        int seqGap = 6;
        long itemGapTS = 5000;
        long seqGapTS = 60000;
        LocalParameter localParameter = new LocalParameter(localSupport, itemGap, seqGap, itemGapTS, seqGapTS);
//        PrefixSpanTool pst = new PrefixSpanTool(s.split(","), localParameter);
//        pst.prefixSpanCalculate();
//        pst.printTotalFreSeqs();
//		BMSequence freqSeqSet = pst.generateBitMaps();
//		 freqSeqSet.printFreqSeqSet();
//		GenerateFSOnPrevRes moreComputation = new GenerateFSOnPrevRes(freqSeqSet, 3, 0, 6);
//		moreComputation.FreqSeqMiningOnPrev();
    }
}
