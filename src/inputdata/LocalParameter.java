package inputdata;

/**
 * Created by yizhouyan on 4/23/18.
 */
public class LocalParameter {
    private int localSupport;
    private int itemGap;
    private int seqGap;
    private long itemGapTS;
    private long seqGapTS;
//    private int supportDiffThreshold;

    public LocalParameter(int localSupport, int itemGap, int seqGap, long itemGapTS, long seqGapTS){ //int supportDiffThreshold
        this.localSupport = localSupport;
        this.itemGap = itemGap;
        this.seqGap = seqGap;
        this.itemGapTS = itemGapTS;
        this.seqGapTS = seqGapTS;
//        this.supportDiffThreshold = supportDiffThreshold;
    }

    public LocalParameter clone(){
        return new LocalParameter(this.localSupport, this.itemGap, this.seqGap, itemGapTS, seqGapTS); //, this.supportDiffThreshold
    }

    public int getLocalSupport() {
        return localSupport;
    }

    public void setLocalSupport(int localSupport) {
        this.localSupport = localSupport;
    }

    public int getItemGap() {
        return itemGap;
    }

    public void setItemGap(int itemGap) {
        this.itemGap = itemGap;
    }

    public int getSeqGap() {
        return seqGap;
    }

    public void setSeqGap(int seqGap) {
        this.seqGap = seqGap;
    }

//    public int getSupportDiffThreshold() {
//        return supportDiffThreshold;
//    }
//
//    public void setSupportDiffThreshold(int supportDiffThreshold) {
//        this.supportDiffThreshold = supportDiffThreshold;
//    }

    public long getItemGapTS() {
        return itemGapTS;
    }

    public void setItemGapTS(long itemGapTS) {
        this.itemGapTS = itemGapTS;
    }

    public long getSeqGapTS() {
        return seqGapTS;
    }

    public void setSeqGapTS(long seqGapTS) {
        this.seqGapTS = seqGapTS;
    }
}
