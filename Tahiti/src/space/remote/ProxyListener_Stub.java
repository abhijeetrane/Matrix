package space.remote;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import java.util.EventObject;

public final class ProxyListener_Stub extends RemoteStub implements RemoteEventListener {
  private static final long serialVersionUID = 2L;
  
  private static Method $method_notify_0;
  
  static {
    try {
      $method_notify_0 = RemoteEventListener.class.getMethod("notify", new Class[] { EventObject.class });
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new NoSuchMethodError("stub class initialization failed");
    } 
  }
  
  public ProxyListener_Stub(RemoteRef paramRemoteRef) {
    super(paramRemoteRef);
  }
  
  public void notify(EventObject paramEventObject) throws RemoteException {
    try {
      this.ref.invoke(this, $method_notify_0, new Object[] { paramEventObject }, 3615383539805677769L);
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (RemoteException remoteException) {
      throw remoteException;
    } catch (Exception exception) {
      throw new UnexpectedException("undeclared checked exception", exception);
    } 
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\ProxyListener_Stub.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */