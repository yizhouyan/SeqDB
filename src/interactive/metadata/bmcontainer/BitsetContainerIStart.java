package interactive.metadata.bmcontainer;

import interactive.util.InteractiveToolkit;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by yizhouyan on 8/7/18.
 */
public class BitsetContainerIStart extends BitsetContainer implements IStartPointsContainer {

    public BitsetContainerIStart(ArrayList<Integer> noneZeroBits, int bitsetSize) {
        super(noneZeroBits, bitsetSize);
    }

    public BitsetContainerIStart(short[] noneZeroBits, int bitsetSize) {
        super(noneZeroBits, bitsetSize);
    }

    public BitsetContainerIStart(BitSet bitset){
        super(bitset);
    }

    @Override
    public String printBitset() {
        return "BitSetContainerForStart: " + super.printBitset();
    }

    @Override
    public IBMContainer toCompressContainer(int sequenceLength) {
        if (ContainerSelecter.useBitset(sequenceLength, bitset.cardinality())){
            return this;
        }else
            return new ArrayBMContainerIStart(InteractiveToolkit.bits2Ints(bitset));
    }

    @Override
    public int getNextStartPoint(int pos, int startPos) {
        return bitset.nextSetBit(startPos);
    }

    @Override
    public int getContainerSupport() {
        return bitset.cardinality();
    }

    @Override
    public int getStartAfterPos(int afterPos, int pos) {
        return bitset.nextSetBit(afterPos+1);
    }

    @Override
    public int getPosAfterPos(int afterPos, int pos) {
        return 0;
    }

    @Override
    public int getPosBeforePos(int beforePos, int pos) {
        return 0;
    }

    @Override
    public int getStartBeforePos(int beforePos, int pos) {
        return bitset.previousSetBit(beforePos-1);
    }
}
