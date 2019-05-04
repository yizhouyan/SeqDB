package interactive.metadata;

/**
 * Created by yizhouyan on 8/8/18.
 */
public class BasicStatistics {
    public static int totalNumPatterns = 0;
    public static int totalSizePattern = 0;
    public static int seqWithPatterns = 0;
    public static int totalNumInputEvents = 0;
    public static int totalNumOcc = 0;

    public static void printBasicStatistics(){
        System.out.println("Total number of input events: " + totalNumInputEvents +
                "\nTotal number of sequences with patterns: " + seqWithPatterns +
                "\nNumber of Patterns: " + totalNumPatterns +
                "\nAverage Pattern Length: " + totalSizePattern * 1.0/totalNumPatterns +
                "\nNumber of Occurrences: " + totalNumOcc);
    }
}
