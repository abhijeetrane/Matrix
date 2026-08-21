package space.remote;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.EventObject;
import java.util.Hashtable;

public class ProxyListener implements RemoteEventListener {
  private Hashtable bindtab = new Hashtable<Object, Object>();
  
  public ProxyListener() throws RemoteException {
    UnicastRemoteObject.exportObject(this);
  }
  
  public void add(Integer paramInteger, RemoteEventListener paramRemoteEventListener, MarshalledObject paramMarshalledObject) {
    this.bindtab.put(paramInteger, new Binding(paramRemoteEventListener, paramMarshalledObject));
  }
  
  public void notify(EventObject paramEventObject) {
    try {
      Jumper jumper = new Jumper((RemoteEvent)paramEventObject, this.bindtab);
      jumper.start();
    } catch (Exception exception) {}
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\ProxyListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */