package space;

import java.rmi.MarshalledObject;
import space.remote.RemoteEventListener;

public interface SpaceService {
  Lease write(Entry paramEntry, Transaction paramTransaction, long paramLong) throws Exception;
  
  Entry take(Entry paramEntry, Transaction paramTransaction, long paramLong) throws Exception;
  
  Entry takeIfExists(Entry paramEntry, Transaction paramTransaction, long paramLong) throws Exception;
  
  Entry read(Entry paramEntry, Transaction paramTransaction, long paramLong) throws Exception;
  
  Entry readIfExists(Entry paramEntry, Transaction paramTransaction, long paramLong) throws Exception;
  
  Entry snapshot(Entry paramEntry) throws Exception;
  
  void notify(Entry paramEntry, Transaction paramTransaction, RemoteEventListener paramRemoteEventListener, long paramLong, MarshalledObject paramMarshalledObject) throws Exception;
}