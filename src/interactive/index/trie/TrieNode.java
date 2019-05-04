package interactive.index.trie;

import java.util.HashMap;

/**
 * Created by yizhouyan on 4/25/18.
 */
public class TrieNode <T> {
    private HashMap<Short, TrieNode> linksToChildren;
    private boolean isEnd;
    private T content;

    public TrieNode() {
        linksToChildren = new HashMap<>();
    }

    public boolean containsKey(Short ch) {
        return linksToChildren.containsKey(ch);
    }

    public TrieNode get(Short ch) {
        return linksToChildren.get(ch);
    }

    public void put(Short ch, TrieNode node) {
        linksToChildren.put(ch, node);
    }

    public void setEnd(T content) {
        isEnd = true;
        this.content = content;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public T getExistingContent(){
        return content;
    }
}
