package space.remote;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import space.Entry;
import space.Lease;
import space.Transaction;

public interface RemoteSpaceService extends Remote {
  Lease write(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException;
  
  Entry take(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException;
  
  Entry takeIfExists(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException;
  
  Entry read(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException;
  
  Entry readIfExists(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException;
  
  Integer register(String paramString, RemoteEventListener paramRemoteEventListener) throws RemoteException;
  
  Integer pin(Integer paramInteger, Entry paramEntry, InetAddress paramInetAddress) throws RemoteException;
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\RemoteSpaceService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */