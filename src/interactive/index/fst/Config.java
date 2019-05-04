package interactive.index.fst;

import java.math.BigInteger;

/**
 * Created by yizhouyan on 9/24/18.
 */
public class Config {
    public static boolean kIncludeDense = true;
    public static double kSparseDenseRatio = 1/100;
    public static int kWordSize = 64;
    public static BigInteger kMSbMask = new BigInteger("8000000000000000", 16);
    public static short kTerminator = Short.MAX_VALUE;
    public static int kFanout = 256;
    public static int kRankBasicBlockSize = 512;
}
