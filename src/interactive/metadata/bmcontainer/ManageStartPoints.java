package interactive.metadata.bmcontainer;

import interactive.metadata.Constants;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by yizhouyan on 9/19/18.
 */
public class ManageStartPoints{
    private int i;
    private int index;
    private ArrayList<IBMContainer> cl;
    private BitSet curContainer;
    private int nextStartPos;

    public ManageStartPoints(ArrayList<IBMContainer> cl){
        this.i = 0;
        this.index = 0;
        this.cl = cl;
        this.curContainer = ((BitsetContainer) cl.get(this.index)).getBitset();
        this.nextStartPos = 0;
    }

    public int getNextStartPosition(){
        int result = -1;

        if(i < curContainer.cardinality()){
            int setBit = curContainer.nextSetBit(nextStartPos);
            result = setBit + index * (Constants.SHORT_MAX_LIMIT + 1);
            i++;
            nextStartPos = setBit+1;
        }else{
            boolean newContainer = false;
            while(index + 1 < cl.size()){
                index ++;
                this.curContainer = ((BitsetContainer) cl.get(this.index)).getBitset();
                if(curContainer.cardinality() > 0) {
                    newContainer = true;
                    break;
                }
            }
            if(newContainer) {
                i = 0;
                this.nextStartPos = 0;
                int setBit = curContainer.nextSetBit(nextStartPos);
                result = setBit + index * (Constants.SHORT_MAX_LIMIT + 1);
                i++;
                nextStartPos = setBit + 1;
            }
        }
        return result;
    }
}
