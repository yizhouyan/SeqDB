package interactive.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by yizhouyan on 6/23/18.
 */
class InteractiveToolkitTest {
    @Test
    void computeRemovePositionsTest_1() {
        short [] s1 = {0,1,2,3,4};
        short [] s2 = {0,1,3};
        Integer[] results = InteractiveToolkit.computeRemovePositions(s1,s2);
        Integer [] expected = {2,4};
        assertEquals(results.length, expected.length);
        for(int i = 0; i< results.length; i++){
            assertEquals(results[i], expected[i]);
            System.out.println(results[i]);
        }
    }

    @Test
    void shortArrayToString(){
        short [] sArray = new short[]{1,2,3};
        System.out.println(Arrays.toString(sArray));
    }

    @Test
    void computeRemovePositionsTest_2() {
        short [] s1 = {1,2,3,4,5};
        short [] s2 = {2,4};
        Integer[] results = InteractiveToolkit.computeRemovePositions(s1,s2);
        Integer [] expected = {0,2,4};
        assertEquals(results.length, expected.length);
        for(int i = 0; i< results.length; i++){
            assertEquals(results[i], expected[i]);
            System.out.println(results[i]);
        }
    }

    @Test
    void computeRemovePositionsTest_3() {
        short [] s1 = {1,2,3,4,5};
        short [] s2 = {4};
        Integer[] results = InteractiveToolkit.computeRemovePositions(s1,s2);
        Integer [] expected = {0,1,2,4};
        assertEquals(results.length, expected.length);
        for(int i = 0; i< results.length; i++){
            assertEquals(results[i], expected[i]);
            System.out.println(results[i]);
        }
    }

    @Test
    void computeNewBitMap_1() {
        Integer [] removePoints = {1,2};
        BitSet newBitSet = new BitSet(4);
        newBitSet.set(0,true);
        newBitSet.set(1,true);
        newBitSet.set(2,true);
        newBitSet.set(3,true);

        Integer[] results = InteractiveToolkit.computeNewBitSet(newBitSet, removePoints);
        Integer [] expected = {0,3};
        assertEquals(results.length, expected.length);
        for(int i = 0; i< results.length; i++){
            assertEquals(results[i], expected[i]);
            System.out.println(results[i]);
        }
    }

    @Test
    void computeNewBitMap_2() {
        Integer [] removePoints = {3};
        BitSet newBitSet = new BitSet(6);
        newBitSet.set(0,true);
//        newBitSet.set(1,true);
        newBitSet.set(2,true);
        newBitSet.set(3,true);
        newBitSet.set(4,true);

        Integer[] results = InteractiveToolkit.computeNewBitSet(newBitSet, removePoints);
        Integer [] expected = {0,2,3};
        assertEquals(results.length, expected.length);
        for(int i = 0; i< results.length; i++){
            assertEquals(results[i], expected[i]);
            System.out.println(results[i]);
        }
    }

    @Test
    void getIntArrayTest_1(){
        BitSet newBitSet = new BitSet(4);
        newBitSet.set(0,true);
        newBitSet.set(1,true);
        newBitSet.set(2,true);
        newBitSet.set(3,true);
        int [] bitArray = InteractiveToolkit.bitSetToIntArray(newBitSet);
        int [] expected = {0,1,2,3};
        assertEquals(bitArray.length, expected.length);
        for(int i = 0; i< bitArray.length; i++){
            assertEquals(bitArray[i], expected[i]);
            System.out.println(bitArray[i]);
        }
    }

    @Test
    void getIntArrayTest_2(){
        BitSet newBitSet = new BitSet(4);
        newBitSet.set(0,true);
        newBitSet.set(1,true);
        newBitSet.set(3,true);
        int [] bitArray = InteractiveToolkit.bitSetToIntArray(newBitSet);
        int [] expected = {0,1,3};
        assertEquals(bitArray.length, expected.length);
        for(int i = 0; i< bitArray.length; i++){
            assertEquals(bitArray[i], expected[i]);
            System.out.println(bitArray[i]);
        }
    }

    @Test
    void getIntArrayTest_3(){
        BitSet newBitSet = new BitSet(8);
        int [] bitArray = InteractiveToolkit.bitSetToIntArray(newBitSet);
        int [] expected = {};
        assertEquals(bitArray.length, expected.length);
        for(int i = 0; i< bitArray.length; i++){
            assertEquals(bitArray[i], expected[i]);
            System.out.println(bitArray[i]);
        }
    }
}