package interactive.metadata;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * bitmap representation for one single pattern
 * Created by yizhouyan on 4/23/18.
 */
public class QueryResult {
    protected int supportCount = 0;

    public QueryResult(int supportCount){
        this.supportCount = supportCount;
    }

    public String pirntSingleFS(int bitsetSize) {
        String str = "Support # :" + this.supportCount;
        return str;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public ArrayList<Integer> getAllStartPoints(){
        return new ArrayList<>();
    }
}
