package interactive.metadata.bmcontainer;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Created by yizhouyan on 8/22/18.
 */
public class ArrayBMContainerIStartTest {
    public ArrayBMContainerIStart array;
    void setup(){
        Integer [] numbers = {1,2,3,5,6,8,9,11,21,23,34};
        array = new ArrayBMContainerIStart(new ArrayList<Integer>(Arrays.asList(numbers)));
        System.out.println(array.printBitset());
    }
    @Test
    public void getStartAfterPos_1() {
        setup();
        assertEquals(3,array.getPosAfterPos(3,0));
    }

    @Test
    public void getStartAfterPos_2() {
        setup();
        assertEquals(0,array.getPosAfterPos(0,0));
    }

    @Test
    public void getStartAfterPos_3() {
        setup();
        assertEquals(10,array.getPosAfterPos(33,0));
    }

    @Test
    public void getStartAfterPos_4() {
        setup();
        assertEquals(-1,array.getPosAfterPos(37,0));
    }

    @Test
    public void getStartBeforePos_1() {
        setup();
        assertEquals(1,array.getPosBeforePos(3,0));
    }

    @Test
    public void getStartBeforePos_2() {
        setup();
        assertEquals(6,array.getPosBeforePos(10,0));
    }

    @Test
    public void getStartBeforePos_3() {
        setup();
        assertEquals(10,array.getPosBeforePos(38,0));
    }

    @Test
    public void getStartBeforePos_4() {
        setup();
        assertEquals(-1,array.getPosBeforePos(0,0));
    }

}