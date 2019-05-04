package interactive.metadata.bmcontainer;

import interactive.metadata.Constants;
import interactive.query.WaitingList;
import interactive.query.WaitingListTool;
import interactive.util.InteractiveToolkit;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

/**
 * Created by yizhouyan on 8/7/18.
 */
public class BitsetContainer implements IBMContainer {
    protected BitSet bitset;

    public BitsetContainer(ArrayList<Integer> noneZeroBits, int bitsetSize) {
        this.bitset = new BitSet(bitsetSize);
        this.bitset.set(0, bitsetSize, false);
        for (Integer indexForLetter : noneZeroBits) {
            this.bitset.set(indexForLetter, true);
        }
    }

    public BitsetContainer(short[] noneZeroBits, int bitsetSize) {
        this.bitset = new BitSet(bitsetSize);
        this.bitset.set(0, bitsetSize, false);
        for (short indexForLetter : noneZeroBits) {
            int setIndex = indexForLetter + Constants.SHORT_OFFSET;
            this.bitset.set(setIndex, true);
        }
    }


    @Override
    public ArrayList<Integer> getAllSetPoints() {
        return InteractiveToolkit.bits2Ints(bitset);
    }

    @Override
    public BitSet createBitMapContainer(int bitsetSize) {
        return (BitSet) bitset.clone();
    }

    @Override
    public IBMContainer createBitSetContainer(int bitsetSize) {
        return new BitsetContainer((BitSet) bitset.clone());
    }

    @Override
    public BitSet union(short[] freqPattern, short [] superPattern,
                              BitSet container, short [] originalSeq, long [] originalTS,
                              int itemGap, int seqGap, long itemGapTS, long seqGapTS) {
        if(freqPattern[0] == superPattern[0]) {
            container.or(this.bitset);
            return container;
        }else{
            int pos = 0;
            while(pos < bitset.length()) {
                int nextSet = bitset.nextSetBit(pos);
                int endIndex = bitset.nextSetBit(nextSet +1);
                endIndex = (endIndex == -1)? originalSeq.length: endIndex;
                endIndex = Math.max(endIndex, nextSet + 1 + seqGap);
                HashMap<Short, WaitingList> waitingElements = new HashMap<Short, WaitingList>();
                int startIndex = nextSet;
                while(startIndex < endIndex) {
                    short curEle = originalSeq[startIndex];
                    int newStart = WaitingListTool.checkWaitingPatternLists(waitingElements, freqPattern, originalSeq, originalTS, curEle,
                            startIndex, itemGap, seqGap, itemGapTS, seqGapTS);
                    if(newStart >= 0){
                        container.set(newStart,true);
                        break;
                    }
                    startIndex++;
                }
                pos = nextSet+1;
            }
            return container;
        }
    }

    @Override
    public int cardinality() {
        return bitset.cardinality();
    }

    public BitsetContainer(BitSet bitSet){
        this.bitset = bitSet;
    }


    @Override
    public String printBitset() {
        return "BitSetContainer: " + bitset.toString();
    }

    @Override
    public IBMContainer toCompressContainer(int sequenceLength) {
        if (ContainerSelecter.useBitset(sequenceLength, bitset.cardinality())){
            return this;
        }else
            return new ArrayBMContainer(InteractiveToolkit.bits2Ints(bitset));
    }

    public BitSet getBitset() {
        return bitset;
    }

    public void setBitset(BitSet bitset) {
        this.bitset = bitset;
    }
}
