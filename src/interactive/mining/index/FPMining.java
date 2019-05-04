package interactive.mining.index;

import interactive.exception.InvalidParameterException;
import interactive.index.SequenceStorage;
import interactive.util.InteractiveToolkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yizhouyan on 4/24/18.
 */
public class FPMining {
    private SequenceStorage sequenceStorage;
    private int localSupport;
    private int itemGap;
    private int seqGap;
    private long itemGapTS;
    private long seqGapTS;

    public FPMining(SequenceStorage sequenceStorage, int localSupport, int itemGap, int seqGap, long itemGapTS, long seqGapTS){
        this.sequenceStorage = sequenceStorage;
        this.localSupport = localSupport;
        this.itemGap = itemGap;
        this.seqGap = seqGap;
        this.itemGapTS = itemGapTS;
        this.seqGapTS = seqGapTS;
        this.parameterChecking();
    }

    public void parameterChecking(){
        if(localSupport < sequenceStorage.getLocalParameterForStorage().getLocalSupport())
            throw new InvalidParameterException("Invalid local support --- too small");
        if (itemGap > sequenceStorage.getLocalParameterForStorage().getItemGap())
            throw new InvalidParameterException("Invalid event gap --- too large");
        if(seqGap > sequenceStorage.getLocalParameterForStorage().getSeqGap())
            throw new InvalidParameterException("Invalid sequence gap --- too large");
        if (itemGapTS > sequenceStorage.getLocalParameterForStorage().getItemGapTS())
            throw new InvalidParameterException("Invalid event time gap --- too large");
        if(seqGapTS > sequenceStorage.getLocalParameterForStorage().getSeqGapTS())
            throw new InvalidParameterException("Invalid sequence time gap --- too large");
    }

    public long miningContextualLocalFreqPatternsOnPrev(){
        long startTime = System.currentTimeMillis();
        HashMap<Integer, HashMap<short[], Integer>> localFreqSeqs = new HashMap<Integer, HashMap<short[], Integer>>();
        for (int i = 0; i < this.sequenceStorage.getInputData().getInputStringArray().size(); i++) {
            if (this.sequenceStorage.getLocalFreqPatternWrapUps().containsKey(i)) {
                MiningCFPOnPrevRes moreComputation = new MiningCFPOnPrevRes(this.sequenceStorage.getLocalFreqPatternWrapUps().get(i),
                        this.localSupport, this.itemGap, this.seqGap, this.itemGapTS, this.seqGapTS);
                HashMap<short[], Integer> curLocalFS = moreComputation.FreqPatternMiningOnPrev();
                if (curLocalFS != null)
                    localFreqSeqs.put(i, curLocalFS);
            }
        }
//        printPatternMiningResults(localFreqSeqs);
        System.out.println("Contextual pattern mining takes " + (System.currentTimeMillis()-startTime) + " milliseconds");
        return System.currentTimeMillis()-startTime;
    }

    public long miningFreqLocalFreqPatternsOnPrev(){
        long startTime = System.currentTimeMillis();
        HashMap<Integer, HashMap<short[], Integer>> localFreqSeqs = new HashMap<Integer, HashMap<short[], Integer>>();
        for (int i = 0; i < this.sequenceStorage.getInputData().getInputStringArray().size(); i++) {
            if (this.sequenceStorage.getLocalFreqPatternWrapUps().containsKey(i)) {
                MiningFreqOnPrevRes moreComputation = new MiningFreqOnPrevRes(this.sequenceStorage.getLocalFreqPatternWrapUps().get(i),
                        this.sequenceStorage.getLocalParameterForStorage().getLocalSupport(),
                        this.localSupport, this.itemGap, this.seqGap, this.itemGapTS, this.seqGapTS);
                HashMap<short[], Integer> curLocalFS = moreComputation.FreqPatternMiningOnPrev();
                if (curLocalFS != null)
                    localFreqSeqs.put(i, curLocalFS);
            }
        }
//        printPatternMiningResults(localFreqSeqs);
        System.out.println("Frequent pattern mining takes " + (System.currentTimeMillis()-startTime) + " milliseconds");
        return System.currentTimeMillis()-startTime;
    }

    public long miningMaxLocalFreqPatternsOnPrev(){
        long startTime = System.currentTimeMillis();
        HashMap<Integer, HashMap<short[], Integer>> localFreqSeqs = new HashMap<Integer, HashMap<short[], Integer>>();
        for (int i = 0; i < this.sequenceStorage.getInputData().getInputStringArray().size(); i++) {
            if (this.sequenceStorage.getLocalFreqPatternWrapUps().containsKey(i)) {
                MiningMaxOnPrevRes moreComputation = new MiningMaxOnPrevRes(this.sequenceStorage.getLocalFreqPatternWrapUps().get(i),
                        this.sequenceStorage.getLocalParameterForStorage().getLocalSupport(),
                        this.localSupport, this.itemGap, this.seqGap, this.itemGapTS, this.seqGapTS);
                HashMap<short[], Integer> curLocalFS = moreComputation.FreqPatternMiningOnPrev();
                if (curLocalFS != null && curLocalFS.size() > 0)
                    localFreqSeqs.put(i, curLocalFS);
            }
        }
//        printPatternMiningResults(localFreqSeqs);
        System.out.println("Maximum pattern mining takes " + (System.currentTimeMillis()-startTime) + " milliseconds");
        System.out.println("Number of Sequences that contains frequent patterns: " + localFreqSeqs.size());
        return System.currentTimeMillis()-startTime;
    }

    public long miningCloseLocalFreqPatternsOnPrev(){
        long startTime = System.currentTimeMillis();
        HashMap<Integer, HashMap<short[], Integer>> localFreqSeqs = new HashMap<Integer, HashMap<short[], Integer>>();
        for (int i = 0; i < this.sequenceStorage.getInputData().getInputStringArray().size(); i++) {
            if (this.sequenceStorage.getLocalFreqPatternWrapUps().containsKey(i)) {
                MiningCloseOnPrevRes moreComputation = new MiningCloseOnPrevRes(this.sequenceStorage.getLocalFreqPatternWrapUps().get(i),
                        this.sequenceStorage.getLocalParameterForStorage().getLocalSupport(),
                        this.localSupport, this.itemGap, this.seqGap, this.itemGapTS, this.seqGapTS);
                HashMap<short[], Integer> curLocalFS = moreComputation.FreqPatternMiningOnPrev();
                if (curLocalFS != null && curLocalFS.size() > 0)
                    localFreqSeqs.put(i, curLocalFS);
            }
        }
//        printPatternMiningResults(localFreqSeqs);
        System.out.println("Closed pattern mining takes " + (System.currentTimeMillis()-startTime) + " milliseconds");
        System.out.println("Number of Sequences that contains frequent patterns: " + localFreqSeqs.size());
        return System.currentTimeMillis()-startTime;
    }

    public void printPatternMiningResults(HashMap<Integer, HashMap<short[], Integer>> localFreqSeqs){
        System.out.println();
        System.out.println("-------------------Start Printing Pattern Mining Results-------------------");
        for(Map.Entry<Integer, HashMap<short[], Integer>> patterns: localFreqSeqs.entrySet()){
            System.out.println("Device ID: " + patterns.getKey());
            for(Map.Entry<short[], Integer> freqPattern: patterns.getValue().entrySet()){
                System.out.println("Pattern: " + InteractiveToolkit.shortArrayToString(freqPattern.getKey()) + "   , Frequency: " + freqPattern.getValue());
            }
        }
        System.out.println("-------------------End Printing Pattern Mining Results-------------------");
        System.out.println();
    }

    //TODO write pattern mining results to a file
    public void outputPatternMiningResults(){

    }
}
