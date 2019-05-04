package interactive.metadata.bmcontainer;

import java.util.ArrayList;

/**
 * Created by yizhouyan on 8/12/18.
 */
public interface IStartPointsContainer extends IBMContainer{
    public int getNextStartPoint(int pos, int startPos);
    public int getContainerSupport();
    public int getStartAfterPos(int afterPos, int pos);
    public int getPosAfterPos(int afterPos, int pos);
    public int getPosBeforePos(int beforePos, int pos);
    public int getStartBeforePos(int beforePos, int pos);
}
