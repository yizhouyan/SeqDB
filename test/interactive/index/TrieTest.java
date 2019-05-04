package interactive.index;

import interactive.index.trie.Trie;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by yizhouyan on 4/25/18.
 */
class TrieTest {
    short [] getStringInShort(String str){
        short [] inputs = new short[str.split(",").length];
        String [] splits = str.split(",");
        for(int i = 0; i< splits.length; i++)
            inputs[i] = Short.parseShort(splits[i]);
        return inputs;
    }

    @org.junit.jupiter.api.Test
    void singlePatternInsertTest() {
        Trie<String> testTrie = new Trie<String>();
        testTrie.insert(getStringInShort("1,2,3"), "123");
        assertTrue(testTrie.search(getStringInShort("1,2,3")));
        assertFalse(testTrie.search(getStringInShort("1,2")));
        assertFalse(testTrie.search(getStringInShort("2,3")));
    }

    @org.junit.jupiter.api.Test
    void severalPatternInsertTest() {
        Trie<String> testTrie = new Trie<String>();
        testTrie.insert(getStringInShort("1,2,3"), "123");
        testTrie.insert(getStringInShort("1,2"), "12");
        testTrie.insert(getStringInShort("2,3"), "23");
        assertTrue(testTrie.search(getStringInShort("1,2,3")));
        assertTrue(testTrie.search(getStringInShort("1,2")));
        assertTrue(testTrie.search(getStringInShort("2,3")));
    }

    @org.junit.jupiter.api.Test
    void existingPatternQueryTest() {
        Trie<String> testTrie = new Trie<String>();
        testTrie.insert(getStringInShort("1,2,3,4"), "abcd-content");
        testTrie.insert(getStringInShort("1,2,3"), "abc-content");
        testTrie.insert(getStringInShort("1,2"), "ab-content");
        testTrie.insert(getStringInShort("2,3"), "bc-content");
        ArrayList<String> expectedResults = new ArrayList<>();
        expectedResults.add("abcd-content");
        expectedResults.add("abc-content");
        expectedResults.add("ab-content");
        assertEquals(testTrie.findAllPrefixes(getStringInShort("1,2,3,4")), expectedResults);
    }

    @org.junit.jupiter.api.Test
    void prefixPatternQueryTest() {
        Trie<String> testTrie = new Trie<String>();
        testTrie.insert(getStringInShort("1,2,3,4"), "abcd-content");
        testTrie.insert(getStringInShort("1,2,3"), "abc-content");
        testTrie.insert(getStringInShort("1,2"), "ab-content");
        testTrie.insert(getStringInShort("2,3"), "bc-content");
        ArrayList<String> expectedResults = new ArrayList<>();
        expectedResults.add("abcd-content");
        expectedResults.add("abc-content");
        expectedResults.add("ab-content");
        assertEquals(testTrie.findAllPrefixes(getStringInShort("1,2,3,4,5")), expectedResults);
    }

    @org.junit.jupiter.api.Test
    void nonexistPatternQueryTest() {
        Trie<String> testTrie = new Trie<String>();
        testTrie.insert(getStringInShort("1,2,3,4"), "abcd-content");
        testTrie.insert(getStringInShort("1,2,3"), "abc-content");
        testTrie.insert(getStringInShort("1,2"), "ab-content");
        testTrie.insert(getStringInShort("2,3"), "bc-content");
        ArrayList<String> expectedResults = new ArrayList<>();
        assertEquals(testTrie.findAllPrefixes(getStringInShort("3,4,5")), expectedResults);
    }

    @org.junit.jupiter.api.Test
    void longestPrefixPatternQueryTest() {
        Trie<String> testTrie = new Trie<String>();
        testTrie.insert(getStringInShort("1,2,3,4"), "abcd-content");
        testTrie.insert(getStringInShort("1,2,3"), "abc-content");
        testTrie.insert(getStringInShort("1,2"), "ab-content");
        testTrie.insert(getStringInShort("2,3"), "bc-content");
        ArrayList<String> expectedResults = new ArrayList<>();
        assertEquals(testTrie.findLongestPrefixes(getStringInShort("3,4,5")), null);
    }

    @org.junit.jupiter.api.Test
    void longestPrefixPatternQueryTest2() {
        Trie<String> testTrie = new Trie<String>();
        testTrie.insert(getStringInShort("1,2,3,4"), "abcd-content");
        testTrie.insert(getStringInShort("1,2,3"), "abc-content");
        testTrie.insert(getStringInShort("1,2"), "ab-content");
        testTrie.insert(getStringInShort("2,3"), "bc-content");
        ArrayList<String> expectedResults = new ArrayList<>();
        assertEquals(testTrie.findLongestPrefixes(getStringInShort("2,3,4")), "bc-content");
    }


    @org.junit.jupiter.api.Test
    void longestPrefixPatternQueryTest3() {
        Trie<String> testTrie = new Trie<String>();
        testTrie.insert(getStringInShort("1,2,3,4"), "abcd-content");
        testTrie.insert(getStringInShort("1,2,3"), "abc-content");
        testTrie.insert(getStringInShort("1,2"), "ab-content");
        testTrie.insert(getStringInShort("2,3"), "bc-content");
        ArrayList<String> expectedResults = new ArrayList<>();
        assertEquals(testTrie.findLongestPrefixes(getStringInShort("1,2,3,4,5")), "abcd-content");
    }
}