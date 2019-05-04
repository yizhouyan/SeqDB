package interactive.mining.index;

import interactive.exception.IncorrectStartPointException;
import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.InvertedIndex;
import interactive.metadata.SingleSeqWrapup;
import interactive.util.InteractiveToolkit;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yizhouyan on 4/24/18.
 */
public class MiningMaxOnPrevRes {
    private SingleSeqWrapup singleSeqWrapup;
    private int curLocalSupport;
    private int curItemGap;
    private int curSeqGap;
    private long curItemGapTS;
    private long curSeqGapTS;
    private int indexSupport;
    private HashMap<short[], Integer> freqSeqRes;

    public MiningMaxOnPrevRes(SingleSeqWrapup singleSeqWrapup, int curSupport, int indexSupport,
                              int curItemGap, int curSeqGap, long curItemGapTS,
                              long curSeqGapTS){
        this.singleSeqWrapup = singleSeqWrapup;
        this.curItemGap = curItemGap;
        this.curLocalSupport = curSupport;
        this.curSeqGap = curSeqGap;
        this.curItemGapTS = curItemGapTS;
        this.curSeqGapTS = curSeqGapTS;
    }

    public HashMap<short[], Integer> FreqPatternMiningOnPrev() {
        this.freqSeqRes = new HashMap<short[], Integer>();
        if (this.singleSeqWrapup.getFreqPatternsMap().size() == 0)
            return null;
        else {
            ArrayList<Integer> freqLengths = this.singleSeqWrapup.getPatternLengths();
            for(int curLength : freqLengths){
                int indexOfFS = 0;
                ArrayList<FreqPatternWrapup> freqSeqFromPrev = this.singleSeqWrapup.getFreqPatternsMap().get(curLength);
                while (indexOfFS < freqSeqFromPrev.size()) {
                    FreqPatternWrapup curPattern = freqSeqFromPrev.get(indexOfFS);
                    if(curPattern.getAllSupport() >= this.curLocalSupport){
                        miningOnPrevFreqPattern(curPattern);
                    }else
                        break;
                    indexOfFS++;
                }
            }
        }
        return this.freqSeqRes;
    }

    public void miningOnPrevFreqPattern(FreqPatternWrapup prevFreqPattern){
        // first check its super pattern, if frequent pattern exists, then return
        for(FreqPatternWrapup superPattern: prevFreqPattern.getSuperPatterns()){
            if(freqSeqRes.containsKey(superPattern.getFreqPatternInString()))
                return;
        }
        // if the pattern is frequent, add to tempFreqSeqRes, if it is not independently frequent, add to tempFreqPatternForII
        // if support satisfy, check availability of each occurrences and save to tempResult
        //TODO if we change itemGap or seqGap, we need to traverse each pattern occurrence to get the current support number
        freqSeqRes.put(prevFreqPattern.getFreqPatternInString(), prevFreqPattern.getAllSupport());
    }

    public String printMiningResult(){
        String str = "";
        for(Map.Entry<short [], Integer> pattern: freqSeqRes.entrySet()){
            str += InteractiveToolkit.shortArrayToString(pattern.getKey()) + "\t Support: " + pattern.getValue() + "\n";
        }
        return str;
    }
}

