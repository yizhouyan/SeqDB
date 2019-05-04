package interactive.index.fst;

import interactive.metadata.FreqPatternWrapup;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by yizhouyan on 9/24/18.
 */
public class LoudsSparse {
    private int height; // trie height
    private int startLevel; // louds-sparse encoding starts at this level
    private int nodeCountDense; // number of nodes in louds-dense encoding
    private int childCountDense; // number of children in louds-dense encoding

    private ArrayList<Short> labels;
    private ArrayList<FreqPatternWrapup> freqPatternList;
    private BitSet childIndicatorBits;
    private BitSet loudsBits;

    public LoudsSparse(FSTBuilder builder){
        this.height = builder.getLabels().size();
        this.startLevel = builder.getSparseStartLevel();

        nodeCountDense = 0;
//        for(int level = 0; level < startLevel; level++)
//            nodeCountDense += builder.getNodeCounts().get(level);

        if(startLevel == 0)
            this.childCountDense = 0;
//        else
//            this.childCountDense = nodeCountDense + builder.getNodeCounts().get(startLevel) -1;

        generateLabels(builder.getLabels(), this.startLevel, height);
        generateFreqPatternList(builder.getSuffixes(), this.startLevel, height);

        ArrayList<Integer> numItemsPerLevel = new ArrayList<>();
        for(int level = 0; level < height; level++){
            numItemsPerLevel.add(builder.getLabels().get(level).size());
        }
        this.childIndicatorBits = concatenateBitSets(builder.getChildIndicatorBits(), numItemsPerLevel, startLevel, height);
        this.loudsBits = concatenateBitSets(builder.getLoudsBits(), numItemsPerLevel, startLevel, height);
//        truncateTrie();
    }

//    private void truncateTrie(){
//        ArrayList<Short> newLabels = new ArrayList<>();
//        BitSet newChildIndicatorBits = new BitSet();
//        BitSet newLoudsBits = new BitSet();
//
//        int pos = 0;
//        int curSetBit = loudsBits.nextSetBit(pos);
//        int nextSetBit = loudsBits.nextSetBit(curSetBit +1);
//        nextSetBit = (nextSetBit == -1)? loudsBits.length(): nextSetBit;
//
//        while(curSetBit < loudsBits.length()) {
//            if((nextSetBit-curSetBit) > 1 || (labels.get(curSetBit) != Config.kTerminator)) {
//                for (int i = curSetBit; i < nextSetBit; i++) {
//                    int index = newLabels.size();
//                    newLabels.add(labels.get(i));
//                    newChildIndicatorBits.set(index, childIndicatorBits.get(i));
//                    newLoudsBits.set(index, loudsBits.get(i));
//                }
//            }
//            curSetBit = nextSetBit;
//            nextSetBit = loudsBits.nextSetBit(curSetBit +1);
//            nextSetBit = (nextSetBit == -1)? loudsBits.length(): nextSetBit;
//        }
//
//        this.labels = newLabels;
//        this.childIndicatorBits = newChildIndicatorBits;
//        this.loudsBits = newLoudsBits;
//    }

    private BitSet concatenateBitSets(ArrayList<BitSet> bitsetPerLevel, ArrayList<Integer> numBitsPerLevel,
                                      int startLevel, int endLevel){
        BitSet newBitSet = new BitSet();
        int bitShift = 0;
        for(int level = startLevel; level < endLevel; level++){
            if(numBitsPerLevel.get(level) == 0)
                continue;
            for(int i = 0; i < numBitsPerLevel.get(level); i++){
                newBitSet.set(bitShift + i, bitsetPerLevel.get(level).get(i));
            }
            bitShift += numBitsPerLevel.get(level);
        }
        return newBitSet;
    }

    private void generateFreqPatternList(ArrayList<ArrayList<FreqPatternWrapup>> freqPatternLists,
                                         int startLevel, int endLevel){
        this.freqPatternList = new ArrayList<>();
        for(int i = startLevel; i< endLevel; i++)
            for(FreqPatternWrapup freqPattern: freqPatternLists.get(i))
                if(freqPattern != null)
                    freqPatternList.add(freqPattern);
    }

    private void generateLabels(ArrayList<ArrayList<Short>> labelsPerLevel, int startLevel, int endLevel){
        this.labels = new ArrayList<>();
        for(int i = startLevel; i< endLevel; i++){
            labels.addAll(labelsPerLevel.get(i));
        }
    }

    private int findOneLabelInRange(short key, int pos){
        int curSetBit = loudsBits.nextSetBit(pos);
        int nextSetBit = loudsBits.nextSetBit(curSetBit +1);
        nextSetBit = (nextSetBit == -1)? loudsBits.length(): nextSetBit;

        for(int i = curSetBit; i< nextSetBit; i++){
            if(labels.get(i) == key)
                return i;
        }
        return -1;
    }

    public FreqPatternWrapup lookupKey(short [] keys, int inNodeNum){
        int nodeNum = inNodeNum;
        int pos = 0;
        FreqPatternWrapup result = null;
        for(int level = startLevel; level< keys.length; level++){
            int matchPos = findOneLabelInRange(keys[level], pos);
            if(matchPos == -1)
                return result;
            if(!childIndicatorBits.get(matchPos))
                return result;
            // move to child
            pos = childNodePos(matchPos);
            if(labels.get(pos) == Config.kTerminator)
                result = freqPatternList.get(rank0(childIndicatorBits, pos));
        }
        if(labels.get(pos) == Config.kTerminator)
        // Now pos should be the value itself
            return freqPatternList.get(rank0(childIndicatorBits, pos));
        else
            return result;
    }

    private int rank0(BitSet bitset, int pos){
        return pos - bitset.get(0, pos).cardinality();
    }

    private int childNodePos(int pos){
        return select1(loudsBits, rank1(childIndicatorBits, pos) + 1);
    }

    public static int rank1(BitSet bitset, int pos){
        return bitset.get(0, pos).cardinality();
    }

    public static int select1(BitSet bitset, int pos){
        int i = 0;
        int count = 0;
        int result = -1;
        while(count <= pos) {
            if(bitset.nextSetBit(i) >= 0) {
                result = bitset.nextSetBit(i);
                i = result + 1;
                count++;
            }else
                return -1;
        }
        return result;
    }
}
