package interactive.index.grouptrie;

import interactive.metadata.FreqPatternWrapup;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yizhouyan on 10/4/18.
 */
public class TrieNodeContent {
    private HashMap<Integer, FreqPatternWrapup> patternReferences;

    public TrieNodeContent(){
        this.patternReferences = new HashMap<>();
    }

    public void addToTrieNodeContent(int deviceId, FreqPatternWrapup patternReference){
        this.patternReferences.put(deviceId, patternReference);
    }

    public void printTrieNodeContent(){
        for(Map.Entry<Integer, FreqPatternWrapup> freqPattern: patternReferences.entrySet()){
            System.out.println(freqPattern.getKey() + "\t" + freqPattern.getValue().getPatternInString());
        }
    }

    public HashMap<Integer, FreqPatternWrapup> getPatternReferences() {
        return patternReferences;
    }

    public void setPatternReferences(HashMap<Integer, FreqPatternWrapup> patternReferences) {
        this.patternReferences = patternReferences;
    }
}
