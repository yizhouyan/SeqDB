package interactive.mining.index;

import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.InvertedIndex;
import interactive.metadata.SingleSeqWrapup;
import interactive.util.InteractiveToolkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yizhouyan on 4/24/18.
 */
public class MiningCloseOnPrevRes {
    private SingleSeqWrapup singleSeqWrapup;
    private int curLocalSupport;
    private int curItemGap;
    private int curSeqGap;
    private long curItemGapTS;
    private long curSeqGapTS;
    private int indexSupport;
    private HashMap<short[], Integer> freqSeqRes;

    public MiningCloseOnPrevRes(SingleSeqWrapup singleSeqWrapup, int curSupport, int indexSupport,
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
        InvertedIndex longerPatternList = new InvertedIndex(100, 100);

        if (this.singleSeqWrapup.getFreqPatternList().size() == 0)
            return null;
        else {
            ArrayList<FreqPatternWrapup> freqSeqFromPrev = this.singleSeqWrapup.getFreqPatternList();
            int indexOfFS = 0;
            int curLength = freqSeqFromPrev.get(0).getPatternLength();

            while (indexOfFS < freqSeqFromPrev.size() && curLength >= 2) {
                // save temporary frequent pattern of current length, to save search space
                HashMap<short[], Integer> tempFreqSeqRes = new HashMap<>();
                ArrayList<FreqPatternWrapup> tempFreqPatternForII = new ArrayList<>();

                while (indexOfFS < freqSeqFromPrev.size()
                        && freqSeqFromPrev.get(indexOfFS).getPatternLength() == curLength) {
                    if (freqSeqFromPrev.get(indexOfFS).getAllSupport() < this.curLocalSupport) {
                        indexOfFS++;
                        continue;
                    }
                    miningOnPrevFreqPattern(freqSeqFromPrev.get(indexOfFS), tempFreqPatternForII, longerPatternList,
                            tempFreqSeqRes);
                    indexOfFS++;
                }
                // update global bit set
                freqSeqRes.putAll(tempFreqSeqRes);
                if (tempFreqPatternForII.size() > 0) {
                    longerPatternList.addFreqSeqToList(tempFreqPatternForII);
                }
                curLength--;
            }
        }
        return this.freqSeqRes;
    }

    public void miningOnPrevFreqPattern(FreqPatternWrapup prevFreqPattern,
                                        ArrayList<FreqPatternWrapup> tempFreqPatternForII,
                                        InvertedIndex longerPatternList,
                                        HashMap<short[], Integer> tempFreqSeqRes){
        // first check its super pattern, if frequent pattern exists, then return
        int currentSupport = prevFreqPattern.getAllSupport();

        // then check inverted index, if the pattern has a frequent super pattern, return
        ArrayList<FreqPatternWrapup> superPatterns = longerPatternList.getSequencesWithAllElements(prevFreqPattern.getFreqPatternInString());
        for(FreqPatternWrapup prevPattern: superPatterns) {
            if (shortArrayContains(prevPattern.getFreqPatternInString(), prevFreqPattern.getFreqPatternInString())) {
                if((freqSeqRes.containsKey(prevPattern.getFreqPatternInString()))
                        && (freqSeqRes.get(prevPattern.getFreqPatternInString()) == currentSupport))
                    return;
            }
        }
        tempFreqSeqRes.put(prevFreqPattern.getFreqPatternInString(), prevFreqPattern.getAllSupport());
        tempFreqPatternForII.add(prevFreqPattern);
    }

    private boolean shortArrayContains(short [] strList1, short [] strList2) {
        boolean isContained = false;
        for (int i = 0; i < strList1.length - strList2.length + 1; i++) {
            int k = i;
            int j = 0;
            int lastIndex = k;
            while (k < strList1.length && j < strList2.length) {
                if(k - lastIndex - 1 > curItemGap)
                    break;
                if (strList1[k] == strList2[j]) {
                    lastIndex = k;
                    k++;
                    j++;
                } else {
                    k++;
                }
            }
            if (j == strList2.length) {
                isContained = true;
                break;
            }
        }
        return isContained;
    }

    public String printMiningResult(){
        String str = "";
        for(Map.Entry<short [], Integer> pattern: freqSeqRes.entrySet()){
            str += InteractiveToolkit.shortArrayToString(pattern.getKey()) + "\t Support: " + pattern.getValue() + "\n";
        }
        return str;
    }
}

