package interactive.metadata;

/**
 * Created by yizhouyan on 4/28/18.
 */
public interface IPattern {
    public String getPatternInString();
    public int getPatternLength();
    public boolean isFrequentPattern();
    public String printPattern(int bitSize);
}
