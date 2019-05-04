package interactive.metadata.bmcontainer;

import interactive.exception.SeqLenOutofBoundException;
import interactive.metadata.Constants;
import interactive.query.WaitingList;
import interactive.query.WaitingListTool;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by yizhouyan on 8/7/18.
 */
public class ArrayBMContainer implements IBMContainer {
    protected short[] nonzerobit;

    public ArrayBMContainer(ArrayList<Integer> allNoneZeros){
        Collections.sort(allNoneZeros);
        int count = 0;
        this.nonzerobit = new short[allNoneZeros.size()];
        for (int i : allNoneZeros) {
            if (i > Constants.SHORT_MAX_LIMIT) {
                throw new SeqLenOutofBoundException();
            } else {
                short temp = (short) (i - Constants.SHORT_OFFSET);
                this.nonzerobit[count++] = temp;
            }
        }
    }

    @Override
    public ArrayList<Integer> getAllSetPoints() {
        ArrayList<Integer> setPoints = new ArrayList<>();
        for (short i : nonzerobit) {
            setPoints.add(i + Constants.SHORT_OFFSET);
        }
        return setPoints;
    }

    @Override
    public BitSet createBitMapContainer(int bitsetSize) {
        BitSet newBitSet = new BitSet(bitsetSize);
        for(short i: nonzerobit){
            newBitSet.set(i + Constants.SHORT_OFFSET, true);
        }
        return newBitSet;
    }

    @Override
    public IBMContainer createBitSetContainer(int bitsetSize) {
        return new BitsetContainer(getAllSetPoints(), bitsetSize);
    }

    @Override
    public BitSet union(short[] freqPattern, short [] superPattern,
                              BitSet container, short [] originalSeq, long [] originalTS,
                              int itemGap, int seqGap, long itemGapTS, long seqGapTS) {
        if(freqPattern[0] == superPattern[0]) {
            for (short i : nonzerobit) {
                container.set(i + Constants.SHORT_OFFSET, true);
            }
            return container;
        }else{
            for(int i = 0; i< nonzerobit.length; i++){
                int curIndex = nonzerobit[i] + Constants.SHORT_OFFSET;
                HashMap<Short, WaitingList> waitingElements = new HashMap<Short, WaitingList>();
                int startIndex = curIndex;
                int endIndex = (i == nonzerobit.length-1)? originalSeq.length: nonzerobit[i+1]+Constants.SHORT_OFFSET;
                endIndex = Math.min(endIndex, startIndex + seqGap + 1);
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
            }
            return container;
        }
    }

    @Override
    public int cardinality() {
        return nonzerobit.length;
    }

    @Override
    public String printBitset() {
        String str = "ArrayContainer: ";
        for (short i : nonzerobit) {
            str += (i + Constants.SHORT_OFFSET) + ",";
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    public IBMContainer toCompressContainer(int sequenceLength) {
        if (ContainerSelecter.useBitset(sequenceLength, nonzerobit.length)){
            return new BitsetContainer(nonzerobit, sequenceLength);
        }else
            return this;
    }

    public short[] getNonzerobit() {
        return nonzerobit;
    }

    public void setNonzerobit(short[] nonzerobit) {
        this.nonzerobit = nonzerobit;
    }
}
