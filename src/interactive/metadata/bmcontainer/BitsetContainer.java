//package interactive.metadata.bmcontainer;
//
//import interactive.metadata.Constants;
//import interactive.query.WaitingList;
//import interactive.query.WaitingListTool;
//import interactive.util.InteractiveToolkit;
//
//import java.util.ArrayList;
//import java.util.BitSet;
//import java.util.HashMap;
//
///**
// * Created by yizhouyan on 8/7/18.
// */
//public class BitsetContainer {
//    protected BitSet bitset;
//
//    public BitsetContainer(ArrayList<Integer> noneZeroBits, int bitsetSize) {
//        this.bitset = new BitSet(bitsetSize);
//        this.bitset.set(0, bitsetSize, false);
//        for (Integer indexForLetter : noneZeroBits) {
//            this.bitset.set(indexForLetter, true);
//        }
//    }
//
//    @Override
//    public ArrayList<Integer> getAllSetPoints() {
//        return InteractiveToolkit.bits2Ints(bitset);
//    }
//
//    public IBMContainer createBitSetContainer(int bitsetSize) {
//        return new BitsetContainer((BitSet) bitset.clone());
//    }
//
//    public int cardinality() {
//        return bitset.cardinality();
//    }
//
//    public BitsetContainer(BitSet bitSet){
//        this.bitset = bitSet;
//    }
//
//    public String printBitset() {
//        return "BitSetContainer: " + bitset.toString();
//    }
//
//    public BitSet getBitset() {
//        return bitset;
//    }
//
//    public void setBitset(BitSet bitset) {
//        this.bitset = bitset;
//    }
//}
