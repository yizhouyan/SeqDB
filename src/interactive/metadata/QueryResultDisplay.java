package interactive.metadata;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * bitmap representation for one single pattern
 * Created by yizhouyan on 4/23/18.
 */
public class QueryResultDisplay extends QueryResult{
    protected ArrayList<Integer> startPoints;

    public QueryResultDisplay(int support,  ArrayList<Integer> startPoints) {
        super(support);
        this.startPoints = startPoints;
    }

    public QueryResultDisplay clone() {
        QueryResultDisplay newWrapUp = new QueryResultDisplay(this.supportCount, this.startPoints);
        return newWrapUp;
    }

    public String pirntSingleFS(int bitsetSize) {
        String str = "Start Index: ";
        str += startPoints.toString();
        str += "\n";
        str += "Support # :" + this.supportCount;
        return str;
    }

    public ArrayList<Integer> getAllStartPoints(){
        return startPoints;
    }
}
