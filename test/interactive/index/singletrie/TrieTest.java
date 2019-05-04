package interactive.index.singletrie;

/**
 * Created by yizhouyan on 4/25/18.
 */
public class TrieTest {
    short [] getStringInShort(String str){
        short [] inputs = new short[str.split(",").length];
        String [] splits = str.split(",");
        for(int i = 0; i< splits.length; i++)
            inputs[i] = Short.parseShort(splits[i]);
        return inputs;
    }
}