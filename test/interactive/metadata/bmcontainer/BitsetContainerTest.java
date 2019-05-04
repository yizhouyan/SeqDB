package interactive.metadata.bmcontainer;


import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by yizhouyan on 8/12/18.
 */
public class BitsetContainerTest {
    @Test
    public void generateBitSetContainer(){
        ArrayList<Integer> setbits = new ArrayList<>();
        setbits.add(0);
        setbits.add(65535);
        IBMContainer ac = new ArrayBMContainerIStart(setbits);
        IStartPointsContainer s = (IStartPointsContainer)ac;
        System.out.println(s.printBitset());
    }

}