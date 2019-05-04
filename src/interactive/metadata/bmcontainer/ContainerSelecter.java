package interactive.metadata.bmcontainer;

import interactive.metadata.Constants;

import java.util.ArrayList;

/**
 * Created by yizhouyan on 8/11/18.
 */
public class ContainerSelecter {
    public static IBMContainer generateBitSet(ArrayList<Integer> nonZeroPositions, int sequenceLength,
                                              int supportCount){
        if(sequenceLength > Constants.SHORT_MAX_LIMIT + 1)
            return new ContainerList(nonZeroPositions, sequenceLength);
        else {
//            System.out.println(sequenceLength + "," + supportCount);
            if(useBitset(sequenceLength, supportCount)) {
//                System.out.println("Use bitmap container");
                return new BitsetContainerIStart(nonZeroPositions, sequenceLength);
            }
            else {
                return new ArrayBMContainerIStart(nonZeroPositions);
            }
        }
    }

    public static boolean useBitset(int sequenceLen, int usedBit){
        int byteBitset = (int) Math.ceil(sequenceLen * 1.0/64) * 8;
        int byteArray = usedBit * 2;
//        System.out.println(byteBitset + "," + byteArray);
        if (byteBitset <= byteArray)
            return true;
        else
            return false;
    }
}
