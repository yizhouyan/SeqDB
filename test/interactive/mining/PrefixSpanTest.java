package interactive.mining;

import inputdata.InputData;
import inputdata.LocalParameter;
import interactive.metadata.*;
import interactive.mining.prefixspan.PrefixSpanTool;
import org.junit.Test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yizhouyan on 4/27/18.
 */
public class PrefixSpanTest {
    @Test
    public void miningLocalFreqPatternsOnPrev() {
        LocalParameter localParameter = new LocalParameter(2, 0, 3,
                5, 100);

        short[] oneSeq = {0, 1, 2,4, 0, 1, 2,4, 0, 1, 2, 0, 1, 3, 0, 1, 3, 1, 2, 4, 1, 2, 4, 0, 1, 0, 1, 5};
        long[] timeStamps = {0, 0, 0,0, 100, 100, 100,100, 200, 200, 200, 300, 300, 300, 400, 400, 400, 500, 500, 500, 600, 600, 600, 700, 700, 800, 800, 800};

        PrefixSpanTool tool = new PrefixSpanTool(oneSeq, timeStamps, localParameter);
        tool.prefixSpanCalculate();
        tool.printTotalFreqSeqsSimple();

        SingleSeqWrapup freqSeqTree = tool.generateBitMapTree();
        HashMap<Short,String> dictionary = new HashMap<>();
        dictionary.put(Short.parseShort("0"),"S");
        dictionary.put(Short.parseShort("1"),"L");
        dictionary.put(Short.parseShort("2"),"M");
        dictionary.put(Short.parseShort("3"),"A");
        dictionary.put(Short.parseShort("4"),"X");
        dictionary.put(Short.parseShort("5"),"R");
        freqSeqTree.printFreqSeqSet(0, dictionary);
    }

    @Test
    public void testBitSetEqual() {
        // create 2 bitsets
        BitSet bitset1 = new BitSet(8);
        BitSet bitset2 = new BitSet(8);

        // assign values to bitset1
        bitset1.set(0);
        bitset1.set(1);
        bitset1.set(2);
        bitset1.set(3);

        // assign values to bitset2
        bitset2.set(0);
        bitset2.set(1);
        bitset2.set(2);
        bitset2.set(3);

        // print the sets
        System.out.println("Bitset1:" + bitset1);
        System.out.println("Bitset2:" + bitset2);

        // check equality for bitset1 and bitset 2
        System.out.println("" + bitset1.equals(bitset2));
    }

}