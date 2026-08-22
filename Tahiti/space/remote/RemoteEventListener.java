package space.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.EventListener;
import java.util.EventObject;

public interface RemoteEventListener extends Remote, EventListener {
  void notify(EventObject paramEventObject) throws RemoteException;
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\RemoteEventListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */