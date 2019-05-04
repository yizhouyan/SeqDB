package interactive.query;

import interactive.metadata.QueryResult;
import interactive.util.InteractiveToolkit;
import org.junit.Test;


import java.util.ArrayList;
import java.util.BitSet;
import static interactive.remote.RemoteStatementImpl.bytesToMegabytes;
import static org.junit.Assert.assertEquals;

/**
 * Created by yizhouyan on 4/28/18.
 */

public class BitSetTest {
    @Test
    public void testLongSize() {
        System.out.println((65535)>>16);
        System.out.println((65537) & 65535);
    }

    @Test
    public void testBitSetSize() {
        BitSet [] bitsetArray = new BitSet[10000000];
        for(int i = 0; i< 10000000; i++){
            bitsetArray[i] = new BitSet(6);
        }
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is bytes: " + memory);
        System.out.println("Used memory is megabytes: "
                + bytesToMegabytes(memory));
    }

    @Test
    public void replacePartBitSet() {
        BitSet bitset1 = new BitSet();
        BitSet bitset2 = new BitSet();
        bitset2.set(1,true);
        bitset2.set(3,true);
//        System.out.println(bitset2.toLongArray());
        bitset1.or(bitset2);
        System.out.println(bitset1);
        System.out.println(bitset2);
    }

    @Test
    public void BitSetTest() {
        BitSet bitset = new BitSet();
        bitset.set(1, true);
        bitset.set(2, true);
        bitset.set(4, true);
        bitset.set(5, true);
        bitset.set(7, true);
        bitset.set(9, true);
        bitset.set(10, true);
        bitset.set(11, true);
        bitset.set(13, true);
        BitSet bitsetSeg = bitset.get(1,5);
        System.out.println(bitsetSeg);
        System.out.println(bitsetSeg.length());
    }

    @Test
    public void BitSetLengthTest() {
        BitSet bitset = new BitSet();
        bitset.set(1, true);
        bitset.set(2, true);
        bitset.set(4, true);
        bitset.set(5, true);
        bitset.set(7, true);
        bitset.set(9, true);
        bitset.set(10, true);
        bitset.set(11, true);
        bitset.set(13, true);
        assertEquals(bitset.length(),14);
        assertEquals(bitset.nextSetBit(14),-1);
    }



    @Test
    public void bitsetToArrayList() {
        BitSet bitset = new BitSet(5);
        bitset.set(1,true);
        bitset.set(2,true);
        bitset.set(64,true);
//        System.out.println(bitset2.toLongArray());
        ArrayList<Integer> result = InteractiveToolkit.bits2Ints(bitset);
        for(long i: result)
            System.out.println(i);
    }

    @Test
    public void testBitSet(){
        long i = 8; //1000
        int j = 1;

        i ^= (1L << (64));
        System.out.println(i);
    }

    @Test
    public void testOldBitSet(){
        BitSet bitset1 = new BitSet(200);
        bitset1.flip(100);
        System.out.println(bitset1);
//        byte i = 8;
//        byte j = 1;
//        i ^= (j << 10);
//        System.out.println(i);
    }
}