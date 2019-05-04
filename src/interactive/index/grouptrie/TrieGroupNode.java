package interactive.index.grouptrie;

import interactive.metadata.FreqPatternWrapup;

import java.util.*;

/**
 * Created by yizhouyan on 4/25/18.
 */
public class TrieGroupNode {
    private TrieNode root;

    public TrieGroupNode() {
        root = new TrieNode();
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


    public HashMap<Integer, FreqPatternWrapup> findMatchFreqPatternForDevices(short [] pattern,
                                                                              HashMap<Integer, FreqPatternWrapup> freqPatternsForDevices){
        TrieNode node = root;
        TrieNodeContent bestTrieNodeContent = null;
        for (int i = 0; i < pattern.length; i++) {
            short curLetter = pattern[i];
            if (node.containsKey(curLetter)) {
                node = node.get(curLetter);
                if(node.isEnd()){
                    bestTrieNodeContent = node.getExistingContent();
                    if(node.getExistingContent().getPatternReferences().size() == 0)
                        break;
                }
            } else {
                break;
            }
        }
        if(bestTrieNodeContent == null)
            return freqPatternsForDevices;
        freqPatternsForDevices.putAll(bestTrieNodeContent.getPatternReferences());
        return freqPatternsForDevices;
    }
}
