package interactive.index.grouptrie;

import interactive.metadata.FreqPatternWrapup;

import java.util.HashMap;

/**
 * Created by yizhouyan on 4/25/18.
 */
public class TrieNode {
    private HashMap<Short, TrieNode> linksToChildren;
    private boolean isEnd;
    private TrieNodeContent content;

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

    public void setEnd(FreqPatternWrapup freqPattern, int deviceId) {
        if(this.content == null) {
            isEnd = true;
            this.content = new TrieNodeContent();
        }
        this.content.addToTrieNodeContent(deviceId, freqPattern);
    }

    public boolean isEnd() {
        return isEnd;
    }

    public TrieNodeContent getExistingContent(){
        return content;
    }
}
