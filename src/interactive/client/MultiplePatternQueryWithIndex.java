package interactive.client;

import interactive.remote.RemoteStatement;
import interactive.util.InteractiveToolkit;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * Created by yizhouyan on 4/23/18.
 */
public class MultiplePatternQueryWithIndex {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            RemoteStatement rs = (RemoteStatement) registry.lookup("InteractivePatternExploration");
            rs.executeQuery();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
