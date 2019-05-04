package interactive.mining.index;

import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.SingleSeqWrapup;
import interactive.util.InteractiveToolkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yizhouyan on 4/24/18.
 */
public class MiningFreqOnPrevRes {
    private SingleSeqWrapup singleSeqWrapup;
    private int curLocalSupport;
    private int curItemGap;
    private int curSeqGap;
    private long curItemGapTS;
    private long curSeqGapTS;
    private int indexSupport;
    private HashMap<short[], Integer> freqSeqRes;

    public MiningFreqOnPrevRes(SingleSeqWrapup singleSeqWrapup, int curSupport, int indexSupport,
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
                        freqSeqRes.put(curPattern.getFreqPatternInString(), curPattern.getAllSupport());
                    }else
                        break;
                    indexOfFS++;
                }
            }
        }
        return this.freqSeqRes;
    }
}

