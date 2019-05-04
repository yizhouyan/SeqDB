package interactive.metadata.bmcontainer;

import org.junit.Test;

import java.util.ArrayList;



/**
 * Created by yizhouyan on 9/9/18.
 */
public class ContainerListTest {
    @Test
    public void getAllSetPoints() {
        ArrayList<Integer> noneZeroPos = new ArrayList<>();
        noneZeroPos.add(0);
        noneZeroPos.add(100);
        noneZeroPos.add(65535);
        noneZeroPos.add(65536);
        noneZeroPos.add(65580);
        noneZeroPos.add(65536*2);

        ContainerList cl = new ContainerList(noneZeroPos, 65536*2 + 1);
        ArrayList<Integer> outputResult = cl.getAllSetPoints();
        for(int i: outputResult)
            System.out.println(i);
    }

}