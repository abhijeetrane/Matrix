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


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\SpaceService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */