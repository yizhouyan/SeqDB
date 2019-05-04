package interactive.index.fst;

import interactive.metadata.FreqPatternWrapup;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by yizhouyan on 9/24/18.
 */
public class FSTBuilder {
    private boolean includeDense;
    private double sparseDenseRatio;
    private int sparseStartLevel;

    //LOUDS-Sparse bit/byte vectors
    private ArrayList<ArrayList<Short>> labels;
    private ArrayList<BitSet> childIndicatorBits;
    private ArrayList<BitSet> loudsBits;

    //LOUDS-Dense bit vectors
//    private ArrayList<ArrayList<BigInteger>> bitmapLabels;
//    private ArrayList<ArrayList<BigInteger>> bitmapChildIndicatorBits;
//    private ArrayList<ArrayList<BigInteger>> prefixkeyIndicatorBits;

    ArrayList<ArrayList<FreqPatternWrapup>> suffixes;
    ArrayList<Integer> suffixCounts;

    // auxiliary per level bookkeeping vectors
    ArrayList<Integer> nodeCounts;
    ArrayList<Boolean> isLastItemTerminator;


    public FSTBuilder(boolean includeDense, double sparseDenseRatio){
        this.includeDense = includeDense;
        this.sparseDenseRatio = sparseDenseRatio;
        this.sparseStartLevel = 0;
        this.labels = new ArrayList<>();
        this.childIndicatorBits = new ArrayList<>();
        this.loudsBits = new ArrayList<>();
//        this.bitmapLabels = new ArrayList<>();
//        this.bitmapChildIndicatorBits = new ArrayList<>();
//        this.prefixkeyIndicatorBits = new ArrayList<>();
        this.suffixes = new ArrayList<>();
        this.suffixCounts = new ArrayList<>();
        this.nodeCounts = new ArrayList<>();
        this.isLastItemTerminator = new ArrayList<>();
    }

    public static boolean readBit(BitSet bits, int pos){
        return bits.get(pos);
    }

    public static void setBit(BitSet bits, int pos){
        bits.set(pos, true);
    }

    public int getTreeHeight(){
        return labels.size();
    }

//    public ArrayList<ArrayList<BigInteger>> getBitmapLabels(){
//        return bitmapLabels;
//    }
//
//    public ArrayList<ArrayList<BigInteger>> getBitmapChildIndicatorBits(){
//        return bitmapChildIndicatorBits;
//    }
//
//    public ArrayList<ArrayList<BigInteger>> getPrefixkeyIndicatorBits(){
//        return prefixkeyIndicatorBits;
//    }

    public ArrayList<ArrayList<Short>> getLabels(){
        return labels;
    }

    public ArrayList<ArrayList<FreqPatternWrapup>> getSuffixes() {return suffixes; }

    public ArrayList<BitSet> getChildIndicatorBits(){
        return childIndicatorBits;
    }

    public ArrayList<BitSet> getLoudsBits(){
        return loudsBits;
    }

    public ArrayList<Integer> getNodeCounts(){
        return nodeCounts;
    }

    public int getSparseStartLevel(){
        return sparseStartLevel;
    }

    public void build(ArrayList<FreqPatternWrapup> keys){
        assert(keys.size() > 0);
        buildSparse(keys);
//        if(includeDense){
//            determineCutoffLevel();
//            buildDense();
//        }
    }

    private void buildSparse(ArrayList<FreqPatternWrapup> keys){
        for(int i = 0; i< keys.size(); i++){
            int level = skipCommonPrefix(keys.get(i).getFreqPatternInString());
            int curpos = i;
            insertKeyBytesToTrie(keys.get(curpos), level);
        }
    }

    private int skipCommonPrefix(short [] key){
        int level = 0;
        while(level < key.length && isCharCommonPrefix(key[level], level)){
            setBit(childIndicatorBits.get(level), getNumItems(level)-1);
            level++;
        }
        return level;
    }

    private void insertKeyBytesToTrie(FreqPatternWrapup key, int startLevel){
        assert(startLevel < key.getPatternLength());
        int level = startLevel;
        boolean isStartOfNode = false;
        boolean isTerm = false;
        // If it is the start of level, the louds bit needs to be set.
        if(isLevelEmpty(level)){
            isStartOfNode = true;
        }
        // After skipping the common prefix, the first following byte
        // shoud be in an the node as the previous key.
        insertKeyByte(key.getFreqPatternInString()[level], level, isStartOfNode, isTerm, key);
        level ++;

        // All the following bytes inserted must be the start of a
        // new node.
        isStartOfNode = true;
        while(level < key.getPatternLength()){
            insertKeyByte(key.getFreqPatternInString()[level], level, isStartOfNode, isTerm, key);
            level++;
        }
        // The last byte inserted makes key unique in the trie.
        isTerm = true;
        insertKeyByte(Config.kTerminator, level, isStartOfNode, isTerm, key);
        level++;
    }

    private boolean isCharCommonPrefix(short c, int level){
        return (level < getTreeHeight())
                && (!isLastItemTerminator.get(level))
                && (c == labels.get(level).get(labels.get(level).size()-1));
    }

    private boolean isLevelEmpty(int level){
        return(level >= getTreeHeight() || labels.get(level).size() == 0);
    }

    private void insertKeyByte(short c, int level, boolean isStartOfNode, boolean isTerm, FreqPatternWrapup key){
        // level should be at most equal to tree height
        if(level >= getTreeHeight())
            addLevel();
        assert(level < getTreeHeight());

        // sets parent node's child indicator
        if(level > 0)
            setBit(childIndicatorBits.get(level-1), getNumItems(level-1)-1);

        labels.get(level).add(c);
        if(c == Config.kTerminator){
            suffixes.get(level).add(key);
            suffixCounts.set(level, suffixCounts.get(level) + 1);
        }else{
            suffixes.get(level).add(null);
        }

        if(isStartOfNode){
            setBit(loudsBits.get(level), getNumItems(level)-1);
            nodeCounts.set(level, nodeCounts.get(level) + 1);
        }
        isLastItemTerminator.set(level, isTerm);
    }

//    private void determineCutoffLevel(){
//        int cutoffLevel = 0;
//        long denseMem = computeDenseMem(cutoffLevel);
//        long sparseMem = computeSparseMem(cutoffLevel);
//        while((cutoffLevel < getTreeHeight()) && (denseMem * sparseDenseRatio < sparseMem)){
//            cutoffLevel ++;
//            denseMem = computeDenseMem(cutoffLevel);
//            sparseMem = computeSparseMem(cutoffLevel);
//        }
//        sparseStartLevel = cutoffLevel--;
//    }
//
//    private long computeDenseMem(int downtoLevel){
//        assert(downtoLevel <= getTreeHeight());
//        long mem = 0;
//        for(int level = 0; level < downtoLevel; level++){
//            mem  += (2 * Config.kFanout * nodeCounts.get(level));
//            if(level > 0)
//                mem += (nodeCounts.get(level-1) /8 + 1);
//            mem += (suffixCounts.get(level) * 0 /8);
//        }
//        return mem;
//    }

//    private long computeSparseMem(int startLevel){
//        long mem = 0;
//        for(int level = startLevel; level < getTreeHeight(); level++){
//            int numItems = labels.get(level).size();
//            mem += (numItems + 2 * numItems / 8 + 1);
//            mem += (suffixCounts.get(level) * 0 /8);
//        }
//        return mem;
//    }

//    private void buildDense() {
//        for (int level = 0; level < sparseStartLevel; level++) {
//            initDenseVectors(level);
//            if(getNumItems(level) == 0)
//                continue;
//
//            int nodeNum = 0;
//            if(isTerminator(level, 0))
//                setBit(prefixkeyIndicatorBits.get(level), 0);
//            else
//                setLabelAndChildIndicatorBitmap(level, nodeNum, 0);
//            for(int pos = 1; pos < getNumItems(level); pos++){
//                if(isStartOfNode(level, pos)) {
//                    nodeNum++;
//                    if (isTerminator(level, pos)) {
//                        setBit(prefixkeyIndicatorBits.get(level), nodeNum);
//                        continue;
//                    }
//                }
//                setLabelAndChildIndicatorBitmap(level, nodeNum, pos);
//            }
//        }
//    }
//
//    private void initDenseVectors(int level){
//        bitmapLabels.add(new ArrayList<>());
//        bitmapChildIndicatorBits.add(new ArrayList<>());
//        prefixkeyIndicatorBits.add(new ArrayList<>());
//
//        for(int nc = 0; nc < nodeCounts.get(level); nc++){
//            for(int i = 0; i<(int) Config.kFanout; i+= Config.kWordSize){
//                bitmapLabels.get(level).add(BigInteger.ZERO);
//                bitmapChildIndicatorBits.get(level).add(BigInteger.ZERO);
//            }
//            if(nc % Config.kWordSize == 0)
//                prefixkeyIndicatorBits.get(level).add(BigInteger.ZERO);
//        }
//    }

    private void addLevel(){
        this.labels.add(new ArrayList<Short>());
        this.childIndicatorBits.add(new BitSet());
        this.loudsBits.add(new BitSet());
        this.suffixes.add(new ArrayList<FreqPatternWrapup>());
        this.suffixCounts.add(0);

        this.nodeCounts.add(0);
        this.isLastItemTerminator.add(false);
    }

//    private void setLabelAndChildIndicatorBitmap(int level, int nodeNum, int pos){
//        short label = labels.get(level).get(pos);
//        setBit(bitmapLabels.get(level), nodeNum * Config.kFanout + label);
//        if(readBit(childIndicatorBits.get(level), pos)){
//            setBit(bitmapChildIndicatorBits.get(level), nodeNum * Config.kFanout + label);
//        }
//    }

    private int getNumItems(int level){
        return labels.get(level).size();
    }
//    private boolean isStartOfNode(int level, int pos){
//        return readBit(loudsBits.get(level), pos);
//    }
//
//    private boolean isTerminator(int level, int pos){
//        short label = labels.get(level).get(pos);
//        return ((label == Config.kTerminator) && !readBit(childIndicatorBits.get(level), pos));
//    }
}
