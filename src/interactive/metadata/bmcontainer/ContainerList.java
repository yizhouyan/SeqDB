package interactive.metadata.bmcontainer;

import interactive.metadata.Constants;
import interactive.query.WaitingList;
import interactive.query.WaitingListTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;

/**
 * Created by yizhouyan on 8/8/18.
 */
public class ContainerList implements IBMContainer {
    private ArrayList<IBMContainer> bitsetContainerList;

    public ArrayList<ArrayList<Integer>> createContainers(int bitsetSize) {
        int numContainers = ((bitsetSize - 1) >> Constants.SHORT_NUM_BITS) + 1;
        ArrayList<ArrayList<Integer>> containers = new ArrayList<>();
        for (int i = 0; i < numContainers; i++) {
            ArrayList<Integer> oneContainer = new ArrayList<>();
            containers.add(oneContainer);
        }
        return containers;
    }

    public ContainerList(int bitsetSize){
        this.bitsetContainerList = new ArrayList<>();
    }

    public ContainerList(ArrayList<IBMContainer> containers){this.bitsetContainerList = containers;}

    public void addToContainerList(IBMContainer container){
        this.bitsetContainerList.add(container);
    }

    public ContainerList(ArrayList<Integer> nonZeroPositions, int bitsetSize) {
        ArrayList<ArrayList<Integer>> containers = createContainers(bitsetSize);
        for (int index : nonZeroPositions) {
            int containerId = index >> Constants.SHORT_NUM_BITS;
            int newNum = index & Constants.SHORT_MAX_LIMIT;
            containers.get(containerId).add(newNum);
        }
        generateContainerList(containers, bitsetSize);
    }

    @Override
    public ArrayList<Integer> getAllSetPoints() {
        ArrayList<Integer> setPoints = new ArrayList<>();
        for(int i = 0; i< bitsetContainerList.size(); i++){
            ArrayList<Integer> tempSetPoints = bitsetContainerList.get(i).getAllSetPoints();
            for(int setPoint: tempSetPoints){
                setPoints.add(setPoint + i * (Constants.SHORT_MAX_LIMIT+1));
            }
        }
        return setPoints;
    }

    @Override
    public BitSet createBitMapContainer(int bitsetSize) {
        ManageStartPoints startPointManager = new ManageStartPoints(((ContainerList)this.createBitSetContainer(bitsetSize)).getBitsetContainerList());
        BitSet container = new BitSet(bitsetSize);
        int curSetBit;
        while((curSetBit = startPointManager.getNextStartPosition()) != -1){
            container.set(curSetBit, true);
        }
        return container;
    }

    @Override
    public IBMContainer createBitSetContainer(int bitsetSize) {
        ArrayList<IBMContainer> newContainerList = new ArrayList<>();
        for(int i = 0; i< this.bitsetContainerList.size()-1; i++){
            IBMContainer curContainer = this.bitsetContainerList.get(i).createBitSetContainer(Constants.SHORT_MAX_LIMIT+1);
            newContainerList.add(curContainer);
        }
        int lastSeqLen = bitsetSize % (Constants.SHORT_MAX_LIMIT+1);
        IBMContainer curBitSetContainer = this.bitsetContainerList.get(this.bitsetContainerList.size()-1).createBitSetContainer(lastSeqLen);
        newContainerList.add(curBitSetContainer);
        return new ContainerList(newContainerList);
    }

    @Override
    public BitSet union(short[] freqPattern, short [] superPattern,
                              BitSet container, short [] originalSeq, long [] originalTS,
                              int itemGap, int seqGap, long itemGapTS, long seqGapTS) {
        ManageStartPoints startPointManager = new ManageStartPoints(((ContainerList)this.createBitSetContainer(originalSeq.length)).getBitsetContainerList());
        int curStartPoint = startPointManager.getNextStartPosition();
        int nextStartPoint = startPointManager.getNextStartPosition();
        while (curStartPoint != -1) {
            int curEndPoint = (nextStartPoint == -1) ? originalSeq.length : nextStartPoint;
            curEndPoint = Math.min(curEndPoint, curStartPoint + seqGap + 1);
            HashMap<Short, WaitingList> waitingElements = new HashMap<Short, WaitingList>();
            int startIndex = curStartPoint;
            while(startIndex < curEndPoint) {
                short curEle = originalSeq[startIndex];
                int newStart = WaitingListTool.checkWaitingPatternLists(waitingElements, freqPattern, originalSeq, originalTS, curEle,
                        startIndex, itemGap, seqGap, itemGapTS, seqGapTS);
                if(newStart >= 0){
                    container.set(newStart,true);
                    break;
                }
                startIndex++;
            }
            curStartPoint = nextStartPoint;
            nextStartPoint = startPointManager.getNextStartPosition();
        }
        return container;
    }

    @Override
    public int cardinality() {
        int supportSum = 0;
        for(IBMContainer container: bitsetContainerList)
            supportSum += container.cardinality();
        return supportSum;
    }

    public void generateContainerList(ArrayList<ArrayList<Integer>> containers, int seqSize) {
        this.bitsetContainerList = new ArrayList<>();
        for(int i = 0; i< containers.size()-1; i++){
            ArrayList<Integer> curContainer = containers.get(i);
            if(useBitset(Constants.SHORT_MAX_LIMIT+1, curContainer.size())){
                IBMContainer curBitSetContainer;
                curBitSetContainer = new BitsetContainerIStart(curContainer, Constants.SHORT_MAX_LIMIT+1);
                this.bitsetContainerList.add(curBitSetContainer);
            }else{
                IBMContainer curArrayContainer;
                curArrayContainer = new ArrayBMContainerIStart(curContainer);
                this.bitsetContainerList.add(curArrayContainer);
            }
        }
        // for the last one, first compute its sequence length
        int lastSeqLen = seqSize % (Constants.SHORT_MAX_LIMIT+1);
        ArrayList<Integer> curContainer = containers.get(containers.size()-1);
        if(useBitset(lastSeqLen, curContainer.size())){
            IBMContainer curBitSetContainer;
            curBitSetContainer= new BitsetContainerIStart(curContainer, lastSeqLen);
            this.bitsetContainerList.add(curBitSetContainer);
        }else{
            IBMContainer curArrayContainer;
            curArrayContainer = new ArrayBMContainerIStart(curContainer);
            this.bitsetContainerList.add(curArrayContainer);
        }
    }

    public boolean useBitset(int sequenceLen, int usedBits) {
        // compute how many bytes memory for bitset (len-64 -> 8 byte)
        int byteBitset = (int) Math.ceil(sequenceLen/64) * 8;
        int byteArray =  usedBits * 2;
        if (byteBitset <= byteArray)
            return true;
        else
            return false;
    }

    @Override
    public String printBitset() {
        String str = "From Container List: \n";
        for(IBMContainer container: this.bitsetContainerList){
            str += container.printBitset() + "\t";
        }
        return  str;
    }

    @Override
    public IBMContainer toCompressContainer(int sequenceLength) {
        for(int i = 0; i< bitsetContainerList.size()-1; i++){
            bitsetContainerList.set(i, bitsetContainerList.get(i).toCompressContainer(Constants.SHORT_MAX_LIMIT+1));
        }
        int lastSeqLen = sequenceLength % (Constants.SHORT_MAX_LIMIT+1);
        bitsetContainerList.set(bitsetContainerList.size()-1,
                bitsetContainerList.get(bitsetContainerList.size()-1).toCompressContainer(lastSeqLen));
        return this;
    }



    public int numContainers(){
        return this.bitsetContainerList.size();
    }

    public ArrayList<IBMContainer> getBitsetContainerList() {
        return bitsetContainerList;
    }

    public void setBitsetContainerList(ArrayList<IBMContainer> bitsetContainerList) {
        this.bitsetContainerList = bitsetContainerList;
    }
}
