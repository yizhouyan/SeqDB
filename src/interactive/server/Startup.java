package interactive.server;

import interactive.remote.RemoteStatementImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by yizhouyan on 4/20/18.
 */
public class Startup {
    public static void main(String [] args){
        try {
            RemoteStatementImpl rsi=new RemoteStatementImpl();
            LocateRegistry.createRegistry(1099);
            Registry registry=LocateRegistry.getRegistry();
            registry.bind("InteractivePatternExploration", rsi);
            System.out.println("Server connected !");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
