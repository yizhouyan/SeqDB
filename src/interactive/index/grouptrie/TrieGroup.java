package interactive.index.grouptrie;

import interactive.metadata.FreqPatternWrapup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by yizhouyan on 10/6/18.
 */
public class TrieGroup {
    private ArrayList<TrieGroupNode> trieGroupNodes;
    private double similarThreshold = 1;

    public TrieGroup(){
        this.trieGroupNodes = new ArrayList<>();
    }

    public void insertDeviceToTrie(int deviceId, ArrayList<FreqPatternWrapup> freqPatternsInDevice,
                                  ArrayList<HashSet<String>> featureGroups,  ArrayList<Integer> numDevicesPerGroup){
        HashSet<String> newFeatures = extractFeatures(freqPatternsInDevice);
        TrieGroupNode matchedGroup = findInsertGroup(newFeatures, featureGroups, numDevicesPerGroup);
        for(FreqPatternWrapup freqPattern: freqPatternsInDevice){
            matchedGroup.insert(freqPattern.getFreqPatternInString(), freqPattern, deviceId);
        }
    }

    public HashSet<String> extractFeatures(ArrayList<FreqPatternWrapup> freqPatternWrapups){
        HashSet<String> features = new HashSet<>();
        for(FreqPatternWrapup freqPatternWrapup: freqPatternWrapups){
            features.add(freqPatternWrapup.getPatternInString());
        }
        return features;
    }

    public TrieGroupNode findInsertGroup(HashSet<String> curFeatures, ArrayList<HashSet<String>> featureGroups,
                                         ArrayList<Integer> numDevicesPerGroup){
        int bestMatchGroup = -1;
        double bestSimilarity = 0.0;
        for(int i = 0; i< trieGroupNodes.size(); i++){
            TrieGroupNode curTrieGroupNode = trieGroupNodes.get(i);
            HashSet<String> curFeatureGroup = featureGroups.get(i);
            HashSet<String> unionSet = new HashSet<>(curFeatures);
            unionSet.addAll(curFeatureGroup);
            int intersectionSize = curFeatureGroup.size() + curFeatures.size() - unionSet.size();
//            double curSimilarity = intersectionSize * 1.0 / curFeatures.size();
            double curSimilarity =  1.0 * intersectionSize / unionSet.size();
            if(curSimilarity > bestSimilarity){
                bestMatchGroup = i;
                bestSimilarity = curSimilarity;
            }
        }
        if(bestSimilarity < similarThreshold){
            TrieGroupNode curTrieGroupNode = new TrieGroupNode();
            trieGroupNodes.add(curTrieGroupNode);
            featureGroups.add(curFeatures);
            numDevicesPerGroup.add(1);
            return curTrieGroupNode;
        }else {
            featureGroups.get(bestMatchGroup).addAll(curFeatures);
            numDevicesPerGroup.set(bestMatchGroup, numDevicesPerGroup.get(bestMatchGroup) + 1);
            return trieGroupNodes.get(bestMatchGroup);
        }
    }

    public HashMap<Integer, FreqPatternWrapup> findMatchFreqPatternForDevices(short [] pattern) {
        HashMap<Integer, FreqPatternWrapup> freqPatternsForDevices = new HashMap<>();
        for(TrieGroupNode singleTrie: trieGroupNodes){
            freqPatternsForDevices = singleTrie.findMatchFreqPatternForDevices(pattern, freqPatternsForDevices);
        }
        return freqPatternsForDevices;
    }


    public int getNumOfTrie(){
       return trieGroupNodes.size();
    }

}
