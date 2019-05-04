package interactive.index.singletrie;

import interactive.metadata.FreqPatternWrapup;

import java.util.*;

/**
 * Created by yizhouyan on 4/25/18.
 */
public class Trie{
    private TrieNode root;
    private BitSet devicesInTrie;

    public Trie() {
        root = new TrieNode();
        this.devicesInTrie = new BitSet();
    }

    public void markDeviceInTrie(int deviceId){
        devicesInTrie.set(deviceId, true);
    }

    // Inserts a pattern into the trie.
    public void insert(short [] pattern, FreqPatternWrapup content, int deviceId) {
        TrieNode node = root;
        for (int i = 0; i < pattern.length; i++) {
            short currentChar = pattern[i];
            if (!node.containsKey(currentChar)) {
                node.put(currentChar, new TrieNode());
            }
            node = node.get(currentChar);
        }
        node.setEnd(content, deviceId);
    }

    // search a prefix or whole key in trie and
    // returns the node where search ends
    private TrieNode searchPrefix(short [] pattern) {
        TrieNode node = root;
        for (int i = 0; i < pattern.length; i++) {
            short curLetter = pattern[i];
            if (node.containsKey(curLetter)) {
                node = node.get(curLetter);
            } else {
                return null;
            }
        }
        return node;
    }

    // Returns if the pattern is in the trie.
    public boolean search(short [] pattern) {
        TrieNode node = searchPrefix(pattern);
        return node != null && node.isEnd();
    }

    /**
     * Returns if there exists a pattern starts with prefix
     * @param prefix
     * @return
     */
    public boolean startsWith(short [] prefix) {
        TrieNode node = searchPrefix(prefix);
        return node != null;
    }

    /**
     * Returns all prefix matches to the pattern, sorted by length
     * @param pattern
     * @return
     */
    public ArrayList<TrieNodeContent> findAllPrefixes(short [] pattern){
        ArrayList<TrieNodeContent> results = new ArrayList<>();
        TrieNode node = root;
        for (int i = 0; i < pattern.length; i++) {
            short curLetter = pattern[i];
            if (node.containsKey(curLetter)) {
                node = node.get(curLetter);
                if(node.isEnd()){
                    results.add(0, node.getExistingContent());
                }
            } else {
                break;
            }
        }
        return results;
    }

    /**
     * Returns the longest prefix matches to the pattern
     */
    public TrieNodeContent findLongestPrefixes(short [] pattern){
        TrieNodeContent result = null;
        TrieNode node = root;
        for (int i = 0; i < pattern.length; i++) {
            short curLetter = pattern[i];
            if (node.containsKey(curLetter)) {
                node = node.get(curLetter);
                if(node.isEnd()){
                    result = node.getExistingContent();
                }
            } else {
                break;
            }
        }
        return result;
    }

    public HashMap<Integer, FreqPatternWrapup> findMatchFreqPatternForDevices(short [] pattern){
        HashMap<Integer, FreqPatternWrapup> freqPatternsForDevices = new HashMap<>();
        Stack<TrieNodeContent> trieNodeContentStack = new Stack<TrieNodeContent>();
        TrieNode node = root;
        for (int i = 0; i < pattern.length; i++) {
            short curLetter = pattern[i];
            if (node.containsKey(curLetter)) {
                node = node.get(curLetter);
                if(node.isEnd()) {
                    trieNodeContentStack.push(node.getExistingContent());
                }
            } else {
                break;
            }
        }
        if(trieNodeContentStack.empty())
            return freqPatternsForDevices;
        TrieNodeContent longestMatchPattern = trieNodeContentStack.pop();
        BitSet markedDevices = (BitSet) this.devicesInTrie.clone();
        markedDevices.andNot(longestMatchPattern.getDeviceMap());
        freqPatternsForDevices.putAll(longestMatchPattern.getPatternReferences());
        while(!trieNodeContentStack.empty()){
            if(markedDevices.cardinality() == 0)
                break;
            TrieNodeContent curMatchPattern = trieNodeContentStack.pop();
            BitSet curMarkedDevice = (BitSet) curMatchPattern.getDeviceMap().clone();
            HashMap<Integer, FreqPatternWrapup> curFreqPatternForDevices = curMatchPattern.getPatternReferences();
            curMarkedDevice.and(markedDevices);
            if(curMarkedDevice.cardinality() > 0) {
                int pos = 0;
                while(pos < curMarkedDevice.length()) {
                    int nextSet = curMarkedDevice.nextSetBit(pos);
                    freqPatternsForDevices.put(nextSet, curFreqPatternForDevices.get(nextSet));
                    pos = nextSet+1;
                }
            }
            markedDevices.andNot(curMarkedDevice);
        }
        return freqPatternsForDevices;
    }


    public HashMap<Integer, FreqPatternWrapup> findMatchFreqPatternForDevicesWithOpt(short [] pattern,
                                                                                     HashMap<Integer, Integer> bestMatchStartPoints){
        HashMap<Integer, FreqPatternWrapup> freqPatternsForDevices = new HashMap<>();

        short[] curQuery = pattern.clone();
        int curQueryLength = curQuery.length;
        BitSet globalDeviceInTrie = (BitSet) this.devicesInTrie.clone();
        for(int i = 0; i< curQueryLength-1; i++){
//            System.out.println("Current Query Sequence: " + Arrays.toString(curQuery));
            // find longest prefix in trie
            Stack<TrieNodeContent> trieNodeContentStack = new Stack<TrieNodeContent>();
            TrieNode node = root;
            int j = 0;
            for (j = 0; j < curQuery.length; j++) {
                short curLetter = curQuery[j];
                if (node.containsKey(curLetter)) {
                    node = node.get(curLetter);
                    if(node.isEnd())
                        trieNodeContentStack.push(node.getExistingContent());
                } else {
                    break;
                }
            }
            if(!trieNodeContentStack.empty()) {

                TrieNodeContent longestMatchPattern = trieNodeContentStack.pop();
                BitSet markedDevices = (BitSet) globalDeviceInTrie.clone();

                if (i == 0) {
                    markedDevices.andNot(longestMatchPattern.getDeviceMap());
                    freqPatternsForDevices.putAll(longestMatchPattern.getPatternReferences());

                } else {
                    BitSet curMarkedDevice = (BitSet) longestMatchPattern.getDeviceMap().clone();
                    HashMap<Integer, FreqPatternWrapup> curFreqPatternForDevices = longestMatchPattern.getPatternReferences();
                    curMarkedDevice.and(markedDevices);
                    if (curMarkedDevice.cardinality() > 0) {
                        int pos = 0;
                        while (pos < curMarkedDevice.length()) {
                            int nextSet = curMarkedDevice.nextSetBit(pos);
                            if (freqPatternsForDevices.containsKey(nextSet)) {
                                if (curFreqPatternForDevices.get(nextSet).getAllSupport() < freqPatternsForDevices.get(nextSet).getAllSupport()) {
                                    bestMatchStartPoints.put(nextSet, i);
                                    freqPatternsForDevices.put(nextSet, curFreqPatternForDevices.get(nextSet));
                                }
                            } else {
                                freqPatternsForDevices.put(nextSet, curFreqPatternForDevices.get(nextSet));
                                bestMatchStartPoints.put(nextSet, i);
                            }
                            pos = nextSet + 1;
                        }
                    }
                    markedDevices.andNot(curMarkedDevice);
                }
                if (j == curQuery.length) {
                    globalDeviceInTrie.andNot(longestMatchPattern.getDeviceMap());
                    if (globalDeviceInTrie.cardinality() == 0)
                        break;
                }

                while (!trieNodeContentStack.empty()) {
                    if (markedDevices.cardinality() == 0)
                        break;
                    TrieNodeContent curMatchPattern = trieNodeContentStack.pop();
                    BitSet curMarkedDevice = (BitSet) curMatchPattern.getDeviceMap().clone();
                    HashMap<Integer, FreqPatternWrapup> curFreqPatternForDevices = curMatchPattern.getPatternReferences();
                    curMarkedDevice.and(markedDevices);
                    if (curMarkedDevice.cardinality() > 0) {
                        int pos = 0;
                        while (pos < curMarkedDevice.length()) {
                            int nextSet = curMarkedDevice.nextSetBit(pos);
                            if (freqPatternsForDevices.containsKey(nextSet)) {
                                if (curFreqPatternForDevices.get(nextSet).getAllSupport() < freqPatternsForDevices.get(nextSet).getAllSupport()) {
                                    freqPatternsForDevices.put(nextSet, curFreqPatternForDevices.get(nextSet));
                                    bestMatchStartPoints.put(nextSet, i);
                                }
                            } else {
                                freqPatternsForDevices.put(nextSet, curFreqPatternForDevices.get(nextSet));
                                bestMatchStartPoints.put(nextSet, i);
                            }
                            pos = nextSet + 1;
                        }
                    }
                    markedDevices.andNot(curMarkedDevice);
                }
            }
            curQuery = Arrays.copyOfRange(curQuery, 1, curQuery.length);
        }
        return freqPatternsForDevices;
    }

    public TrieNode getRoot() {
        return root;
    }

    public void setRoot(TrieNode root) {
        this.root = root;
    }

    //    public HashMap<Integer, FreqPatternWrapup> findMatchFreqPatternForDevices(short [] pattern){
//        HashMap<Integer, FreqPatternWrapup> freqPatternsForDevices = new HashMap<>();
//        Stack<TrieNodeContent> trieNodeContentStack = new Stack<TrieNodeContent>();
//        TrieNode node = root;
//        for (int i = 0; i < pattern.length; i++) {
//            short curLetter = pattern[i];
//            if (node.containsKey(curLetter)) {
//                node = node.get(curLetter);
//                if(node.isEnd()){
//                    trieNodeContentStack.push(node.getExistingContent());
//                    if(node.getExistingContent().getDeviceMap().cardinality() == 0)
//                        break;
//                }
//            } else {
//                break;
//            }
//        }
//        if(trieNodeContentStack.empty())
//            return freqPatternsForDevices;
//        TrieNodeContent longestMatchPattern = trieNodeContentStack.pop();
////        BitSet markedDevices = (BitSet) this.devicesInTrie.clone();
//        BitSet markedDevices = (BitSet) longestMatchPattern.getDeviceMap().clone();
//        freqPatternsForDevices.putAll(longestMatchPattern.getPatternReferences());
//        while(!trieNodeContentStack.empty()){
//            TrieNodeContent curMatchPattern = trieNodeContentStack.pop();
//            BitSet curMarkedDevice = (BitSet) curMatchPattern.getDeviceMap().clone();
//            HashMap<Integer, FreqPatternWrapup> curFreqPatternForDevices = curMatchPattern.getPatternReferences();
//            curMarkedDevice.andNot(markedDevices);
//            if(curMarkedDevice.cardinality() > 0) {
//                int pos = 0;
//                while(pos < curMarkedDevice.length()) {
//                    int nextSet = curMarkedDevice.nextSetBit(pos);
//                    freqPatternsForDevices.put(nextSet, curFreqPatternForDevices.get(nextSet));
//                    pos = nextSet+1;
//                }
//            }
//            markedDevices.or(curMarkedDevice);
////            markedDevices = (BitSet) curMatchPattern.getDeviceMap().clone();
//        }
//        return freqPatternsForDevices;
//    }
}
