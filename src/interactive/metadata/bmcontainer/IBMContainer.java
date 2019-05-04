package interactive.metadata.bmcontainer;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by yizhouyan on 8/7/18.
 */
public interface IBMContainer {
    public String printBitset();
    public IBMContainer toCompressContainer(int sequenceLength);
    public ArrayList<Integer> getAllSetPoints();
    public BitSet createBitMapContainer(int bitsetSize);
    public IBMContainer createBitSetContainer(int bitsetSize);
    public BitSet union(short[] freqPattern, short [] superPattern,
                              BitSet container, short [] originalSeq, long [] originalTS,
                              int itemGap, int seqGap, long itemGapTS, long seqGapTS);
//    public BitSet union(BitSet container);
    public int cardinality();
}
