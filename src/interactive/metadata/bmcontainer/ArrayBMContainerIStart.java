package interactive.metadata.bmcontainer;

import interactive.metadata.Constants;

import java.util.ArrayList;

/**
 * Created by yizhouyan on 8/7/18.
 */
public class ArrayBMContainerIStart extends ArrayBMContainer implements IStartPointsContainer {
    public ArrayBMContainerIStart(ArrayList<Integer> allNoneZeros){
        super(allNoneZeros);
    }

    @Override
    public String printBitset() {
        String str = "ArrayContainerForStartPoints: " + super.printBitset();
        return str;
    }

    @Override
    public IBMContainer toCompressContainer(int sequenceLength) {
        if (ContainerSelecter.useBitset(sequenceLength, nonzerobit.length)){
            return new BitsetContainerIStart(nonzerobit, sequenceLength);
        }else
            return this;
    }

    public int getStartPos(int pos){
        return nonzerobit[pos] + Constants.SHORT_OFFSET;
    }

    @Override
    public int getNextStartPoint(int pos, int startPos) {
        if(pos >= nonzerobit.length)
            return -1;
        return nonzerobit[pos] + Constants.SHORT_OFFSET;
    }

    @Override
    public int getContainerSupport() {
        return nonzerobit.length;
    }

    @Override
    public int getStartAfterPos(int afterPos, int pos) {
        if(pos != -1)
            return nonzerobit[pos] + Constants.SHORT_OFFSET;
        else
            return -1;
    }

    @Override
    public int getPosAfterPos(int afterPos, int pos) {
        if(nonzerobit.length == 0)
            return -1;
        if(nonzerobit[nonzerobit.length-1]+Constants.SHORT_OFFSET > afterPos)
            return binarySearch(afterPos-Constants.SHORT_OFFSET, pos);
        else
            return -1;
    }

    @Override
    public int getPosBeforePos(int beforePos, int pos) {
        if(nonzerobit.length == 0)
            return -1;
        if(nonzerobit[0]+Constants.SHORT_OFFSET < beforePos)
            return binarySearch2(beforePos-Constants.SHORT_OFFSET);
        else
            return -1;
    }

    @Override
    public int getStartBeforePos(int beforePos, int pos) {
        if(pos != -1)
            return nonzerobit[pos] + Constants.SHORT_OFFSET;
        else
            return -1;
    }

    private int binarySearch(int target, int low)
    {
        int high = nonzerobit.length; // numElems is the size of the array i.e arr.size()
        while (low != high) {
            int mid = low + (high-low)/2; // Or a fancy way to avoid int overflow
            if (nonzerobit[mid] <= target) {
                low = mid + 1;
            }
            else {
                high = mid;
            }
        }
        return low;
    }

    private int binarySearch2(int target)
    {
        int high = nonzerobit.length; // numElems is the size of the array i.e arr.size()
        int low = 0;
        while (low != high) {
            int mid = low + (high-low)/2; // Or a fancy way to avoid int overflow
            if (nonzerobit[mid] >= target) {
                high = mid - 1;
            }
            else if (mid+1 < nonzerobit.length && nonzerobit[mid+1] < target){
                low = mid + 1;
            }else{
                high = mid;
            }
        }
        return low;
    }

}
