//package interactive.metadata.bmcontainer;
//
//import interactive.util.InteractiveToolkit;
//
//import java.util.ArrayList;
//import java.util.BitSet;
//
///**
// * Created by yizhouyan on 8/7/18.
// */
//public class BitsetContainerIStart extends BitsetContainer {
//
//    public BitsetContainerIStart(ArrayList<Integer> noneZeroBits, int bitsetSize) {
//        super(noneZeroBits, bitsetSize);
//    }
//
//    public BitsetContainerIStart(short[] noneZeroBits, int bitsetSize) {
//        super(noneZeroBits, bitsetSize);
//    }
//
//    public BitsetContainerIStart(BitSet bitset){
//        super(bitset);
//    }
//
//    @Override
//    public String printBitset() {
//        return "BitSetContainerForStart: " + super.printBitset();
//    }
//
//    public int getNextStartPoint(int pos, int startPos) {
//        return bitset.nextSetBit(startPos);
//    }
//
//    public int getContainerSupport() {
//        return bitset.cardinality();
//    }
//
//    public int getStartAfterPos(int afterPos, int pos) {
//        return bitset.nextSetBit(afterPos+1);
//    }
//
//    public int getPosAfterPos(int afterPos, int pos) {
//        return 0;
//    }
//
//    public int getPosBeforePos(int beforePos, int pos) {
//        return 0;
//    }
//
//    public int getStartBeforePos(int beforePos, int pos) {
//        return bitset.previousSetBit(beforePos-1);
//    }
//}
