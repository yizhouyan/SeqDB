package interactive.metadata;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by yizhouyan on 8/12/18.
 */
class QueryResultTest {
    @Test
    public void generateQueryResultTest(){
        QueryResult qr = new QueryResult(2);
        QueryResult qr2 = new QueryResultDisplay(2,  new ArrayList<Integer>());
        assertTrue(qr2.getClass().getName().endsWith("QueryResultDisplay"));
    }

}