package interactive.client;

import interactive.metadata.BasicStatistics;
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
//            rs.setupSequenceIndex("../data/realdata/real10000data_timestamp_less5w.csv",5,0,
//                    10,5*1000, 60*1000);
            //"../data/realdata/CT_data_ts.csv"
//            rs.setupSequenceIndex(args[0],Integer.parseInt(args[1]),0,
//                    Integer.parseInt(args[2]),5*1000, 60*1000);
            rs.setupSequenceIndex("../data/realdata/CT_data_ts.csv",2, 0,
                    10,5*1000, 60*1000);
//            System.out.println("Input File: " + args[0]);
//            rs.setupSequenceIndex("../data/realdata/oneSequence.csv",2,0,10,2);
//            rs.setupSequenceIndex("../data/extractedClean_less10000_withId.tsv", 2, 0, 10);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
