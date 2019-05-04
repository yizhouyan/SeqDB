package interactive.metadata;

import interactive.util.InteractiveToolkit;

/**
 * Created by yizhouyan on 4/28/18.
 */
public class InfrequentPattern implements IPattern {
    protected short patternInString;
    protected int patternLength;

    public InfrequentPattern(short patternInString, int patternLength){
        this.patternLength = patternLength;
        this.patternInString = patternInString;
    }

    public short getPatternElement(){return patternInString; }

    @Override
    public String getPatternInString() {
        return "" + patternInString;
    }

    @Override
    public int getPatternLength() {
        return patternLength;
    }

    @Override
    public boolean isFrequentPattern() {
        return false;
    }

    @Override
    public String printPattern(int bitSize) {
        String str = patternInString + "," + patternLength + "\n";
        return str;
    }
}
