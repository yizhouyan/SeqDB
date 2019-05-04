package interactive.client;

import interactive.remote.RemoteStatement;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by yizhouyan on 4/23/18.
 */
public class PatternMiningWithIndex {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            RemoteStatement rs = (RemoteStatement) registry.lookup("InteractivePatternExploration");
            rs.executeMining(Integer.parseInt(args[0]),0,10, 5000, 60000, args[1]);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
