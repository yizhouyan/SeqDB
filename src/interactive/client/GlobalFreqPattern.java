package interactive.client;

import interactive.remote.RemoteStatement;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by yizhouyan on 4/23/18.
 */
public class GlobalFreqPattern {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            RemoteStatement rs = (RemoteStatement) registry.lookup("InteractivePatternExploration");
            rs.getGlobalFreqPattern(1, true);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
