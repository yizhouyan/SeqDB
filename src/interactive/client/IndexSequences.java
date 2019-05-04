package interactive.client;

//import interactive.metadata.BasicStatistics;
import interactive.remote.RemoteStatement;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by yizhouyan on 4/23/18.
 */
public class IndexSequences {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            RemoteStatement rs = (RemoteStatement) registry.lookup("InteractivePatternExploration");
//            rs.setupSequenceIndex("../data/testOnLog.txt", 20,0,10);
//            rs.setupSequenceIndex("../data/realdata/logstream.csv",2,0,
//                    10,5*1000, 60*1000);
            rs.setupSequenceIndex("../data/realdata/CT_data_ts.csv",4,0,
                     10,5*1000, 60*1000);
//            rs.setupSequenceIndex("../data/realdata/oneSequence.csv",2,0,10,
//                    5*1000, 60*1000);
//            rs.setupSequenceIndex("../data/extractedClean_less10000_withId.tsv", 2, 0, 10);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
