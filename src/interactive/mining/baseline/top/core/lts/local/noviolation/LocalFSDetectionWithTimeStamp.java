package interactive.mining.baseline.top.core.lts.local.noviolation;

import interactive.mining.baseline.top.core.datastructure.ItemPair;
import interactive.mining.baseline.top.inputs.InputSequence;
import interactive.mining.baseline.top.inputs.InputSequenceWithTS;
import interactive.mining.baseline.top.parameterspace.LocalParameterSpace;
import interactive.mining.baseline.top.parameterspace.LocalParameterSpaceWithTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class LocalFSDetectionWithTimeStamp extends LocalFSDetection{
    public LocalFSDetectionWithTimeStamp(LocalParameterSpace localParameterSpace, InputSequence inputSequence) {
        super(localParameterSpace, inputSequence);
    }

    protected ArrayList<ItemPair> cleanedItemPairs(ArrayList<ItemPair> previousSeq) {
        ArrayList<ItemPair> newSeq = new ArrayList<ItemPair>();
        long startTS = ((InputSequenceWithTS)inputSequence).getOriginalTimeStamps().get(previousSeq.get(0).getIndex());
        long previousTS = startTS;
        int prevIndex = inputSequence.getOriginalIndexes().get(previousSeq.get(0).getIndex());
        newSeq.add(previousSeq.get(0));
        for (int i = 1; i < previousSeq.size(); i++) {
            long currentTS = ((InputSequenceWithTS)inputSequence).getOriginalTimeStamps().get(previousSeq.get(i).getIndex());
            int curIndex = inputSequence.getOriginalIndexes().get(previousSeq.get(i).getIndex());
            LocalParameterSpaceWithTS ts = (LocalParameterSpaceWithTS) localParameterSpace;
            if (currentTS - startTS > ts.getSequenceTimeInterval() + 1
                    || currentTS - previousTS > ts.getItemTimeInterval() + 1
                    || curIndex - prevIndex > ts.getItemGap() + 1)
                break;
            else {
                newSeq.add(previousSeq.get(i));
                prevIndex = curIndex;
                previousTS = currentTS;
            }
        }
        return newSeq;
    }

    public static void main(String[] args) {
        // String s = "A,B,C,A,B,C,A,C,B,A,C,D";
        // String s = "a,b,b,b,c,z,z,z,a,b,c";
        String s = "A|0,B|0,C|0,A|0," +
                "B|0,C|0,A|0,C|0," +
                "B|0,A|0,C|0,D|0";
        int localSupport = 2;
        int itemGap = 0;
        int seqGap = 10;
        long itemInterval = 10000;
        long sequenceInterval = 60000;
        String[] globalFE = { "A", "B", "C", "D" };
        HashSet<String> globalFrequentElements = new HashSet(Arrays.asList(globalFE));
        LocalParameterSpace localParameterSpace = new LocalParameterSpaceWithTS(localSupport, itemGap, seqGap, itemInterval, sequenceInterval);
        InputSequence inputSequence = new InputSequenceWithTS(s);
        LocalFSDetectionWithTimeStamp localFS = new LocalFSDetectionWithTimeStamp(localParameterSpace, inputSequence);
        localFS.Initialization(globalFrequentElements);
        HashMap<String, Integer> freqSeqRes = localFS.LocalFrequentSequenceMining(0);
        System.out.println("Frequent Sequence: ");
        for (Entry<String, Integer> entry : freqSeqRes.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
        // for (String str : cleanInputString.get(0)) {
        // System.out.print(str + ",");
        // }
    }
}
