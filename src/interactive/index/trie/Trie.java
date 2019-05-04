package interactive.index.trie;

import java.util.ArrayList;

/**
 * Created by yizhouyan on 4/25/18.
 */
public class Trie<T>{
    private TrieNode root;

    public Trie() {
        root = new TrieNode<T>();
    }

    // Inserts a pattern into the trie.
    public void insert(short [] pattern, T content) {
        TrieNode node = root;
        for (int i = 0; i < pattern.length; i++) {
            short currentChar = pattern[i];
            if (!node.containsKey(currentChar)) {
                node.put(currentChar, new TrieNode<T>());
            }
            node = node.get(currentChar);
        }
        node.setEnd(content);
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
    public ArrayList<T> findAllPrefixes(short [] pattern){
        ArrayList<T> results = new ArrayList<T>();
        TrieNode node = root;
        for (int i = 0; i < pattern.length; i++) {
            short curLetter = pattern[i];
            if (node.containsKey(curLetter)) {
                node = node.get(curLetter);
                if(node.isEnd()){
                    results.add(0, (T) node.getExistingContent());
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
    public T findLongestPrefixes(short [] pattern){
        T result = null;
        TrieNode node = root;
        for (int i = 0; i < pattern.length; i++) {
            short curLetter = pattern[i];
            if (node.containsKey(curLetter)) {
                node = node.get(curLetter);
                if(node.isEnd()){
                    result = (T) node.getExistingContent();
                }
            } else {
                break;
            }
        }
        return result;
    }
}
