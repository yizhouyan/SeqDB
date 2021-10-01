package interactive.index.fst;


import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertEquals;


/**
 * Created by yizhouyan on 9/27/18.
 */
public class LoudsSparseTest {
    @Test
    public void testSelect(){
        BitSet bitset = new BitSet();
        bitset.set(0);
        bitset.set(1);
        bitset.set(2);
        bitset.set(4);
        bitset.set(6);
        bitset.set(100);
        assertEquals(LoudsSparse.select1(bitset, 0),0);
        assertEquals(LoudsSparse.select1(bitset,1), 1);
        assertEquals(LoudsSparse.select1(bitset,2), 2);
        assertEquals(LoudsSparse.select1(bitset,3), 4);
        assertEquals(LoudsSparse.select1(bitset,4), 6);
        assertEquals(LoudsSparse.select1(bitset,5), 100);
        assertEquals(LoudsSparse.select1(bitset,6), -1);
    }

    @Test
    public void testRank(){
        BitSet bitset = new BitSet();
        bitset.set(0);
        bitset.set(1);
        bitset.set(2);
        bitset.set(4);
        bitset.set(6);
        bitset.set(100);
        assertEquals(LoudsSparse.rank1(bitset, 1),1);
        assertEquals(LoudsSparse.rank1(bitset,2), 2);
        assertEquals(LoudsSparse.rank1(bitset,3), 3);
        assertEquals(LoudsSparse.rank1(bitset,4), 3);
        assertEquals(LoudsSparse.rank1(bitset,5), 4);
        assertEquals(LoudsSparse.rank1(bitset,6), 4);
        assertEquals(LoudsSparse.rank1(bitset,100), 5);
        assertEquals(LoudsSparse.rank1(bitset, 101), 6);
        assertEquals(LoudsSparse.rank1(bitset,102), 6);
    }
}