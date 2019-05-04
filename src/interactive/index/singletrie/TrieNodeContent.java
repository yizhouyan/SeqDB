package interactive.index.singletrie;

import interactive.metadata.FreqPatternWrapup;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yizhouyan on 10/4/18.
 */
public class TrieNodeContent {
    private BitSet deviceMap;
    private HashMap<Integer, FreqPatternWrapup> patternReferences;

    public TrieNodeContent(){
        this.deviceMap = new BitSet();
        this.patternReferences = new HashMap<>();
    }

    public void addToTrieNodeContent(int deviceId, FreqPatternWrapup patternReference){
        this.deviceMap.set(deviceId, true);
        this.patternReferences.put(deviceId, patternReference);
    }

    public void printTrieNodeContent(){
        System.out.println(deviceMap);
        for(Map.Entry<Integer, FreqPatternWrapup> freqPattern: patternReferences.entrySet()){
            System.out.println(freqPattern.getKey() + "\t" + freqPattern.getValue().getPatternInString());
        }
    }

    public BitSet getDeviceMap() {
        return deviceMap;
    }

    public void setDeviceMap(BitSet deviceMap) {
        this.deviceMap = deviceMap;
    }

    public HashMap<Integer, FreqPatternWrapup> getPatternReferences() {
        return patternReferences;
    }

    public void setPatternReferences(HashMap<Integer, FreqPatternWrapup> patternReferences) {
        this.patternReferences = patternReferences;
    }
}
