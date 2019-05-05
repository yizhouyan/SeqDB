package interactive.remote;

import java.rmi.*;

/**
 * The RMI remote interface corresponding to Statement.
 * Statements include buildIndex, execute pattern mining and query on the index
 * Created by yizhouyan on 4/20/18.
 */
public interface RemoteStatement extends Remote {
    public void setupSequenceIndex(String inputFile, int localSupport, int itemGap, int seqGap, long itemGapTS, long seqGapTS) throws RemoteException;
    public void setupSequenceIndex(String inputFile, String metaFile, int localSupport, int itemGap, int seqGap,
                                   long itemGapTS, long seqGapTS) throws RemoteException;
    public void executeMining(int localSupport, int itemGap, int seqGap, long itemGapTS, long seqGapTS, String method) throws RemoteException;
    public void executeQuery(String qry) throws RemoteException;
    public void executeQuery(String globalFreqFileName, String outputFileName) throws RemoteException;
    public void getGlobalFreqPattern(int globalSupport, boolean outputToFile) throws RemoteException;
}
