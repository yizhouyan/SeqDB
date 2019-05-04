package interactive.index.fst;

import interactive.metadata.FreqPatternWrapup;

import java.util.ArrayList;

/**
 * Created by yizhouyan on 9/24/18.
 */
public class FST {
    private LoudsDense louds_dense;
    private LoudsSparse louds_sparse;


    public FST(ArrayList<FreqPatternWrapup> freqPatterns){
        this.create(freqPatterns, Config.kIncludeDense, Config.kSparseDenseRatio);
    }

    public void create(ArrayList<FreqPatternWrapup> freqPatterns, boolean includeDense, double sparseDenseRatio){
        FSTBuilder builder = new FSTBuilder(includeDense, sparseDenseRatio);
        builder.build(freqPatterns);
//        this.louds_dense = new LoudsDense(builder);
        this.louds_sparse = new LoudsSparse(builder);
    }

    public FreqPatternWrapup lookupKey(short [] key){
        int connectNodeNum = 0;
        return louds_sparse.lookupKey(key, connectNodeNum);
    }
}
