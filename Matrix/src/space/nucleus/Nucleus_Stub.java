package space.nucleus;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import space.Entry;
import space.Lease;
import space.Transaction;
import space.remote.RemoteEventListener;
import space.remote.RemoteSpaceService;

public final class Nucleus_Stub extends RemoteStub implements RemoteSpaceService, Remote {
  private static final long serialVersionUID = 2L;
  
  private static Method $method_pin_0;
  
  private static Method $method_read_1;
  
  private static Method $method_readIfExists_2;
  
  private static Method $method_register_3;
  
  private static Method $method_take_4;
  
  private static Method $method_takeIfExists_5;
  
  private static Method $method_write_6;
  
  static {
    try {
      $method_pin_0 = RemoteSpaceService.class.getMethod("pin", new Class[] { Integer.class, Entry.class, InetAddress.class });
      $method_read_1 = RemoteSpaceService.class.getMethod("read", new Class[] { Entry.class, Transaction.class, long.class, InetAddress.class });
      $method_readIfExists_2 = RemoteSpaceService.class.getMethod("readIfExists", new Class[] { Entry.class, Transaction.class, long.class, InetAddress.class });
      $method_register_3 = RemoteSpaceService.class.getMethod("register", new Class[] { String.class, RemoteEventListener.class });
      $method_take_4 = RemoteSpaceService.class.getMethod("take", new Class[] { Entry.class, Transaction.class, long.class, InetAddress.class });
      $method_takeIfExists_5 = RemoteSpaceService.class.getMethod("takeIfExists", new Class[] { Entry.class, Transaction.class, long.class, InetAddress.class });
      $method_write_6 = RemoteSpaceService.class.getMethod("write", new Class[] { Entry.class, Transaction.class, long.class, InetAddress.class });
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new NoSuchMethodError("stub class initialization failed");
    } 
  }
  
  public Nucleus_Stub(RemoteRef paramRemoteRef) {
    super(paramRemoteRef);
  }
  
  public Integer pin(Integer paramInteger, Entry paramEntry, InetAddress paramInetAddress) throws RemoteException {
    try {
      Object object = this.ref.invoke(this, $method_pin_0, new Object[] { paramInteger, paramEntry, paramInetAddress }, -6534766974522884718L);
      return (Integer)object;
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (RemoteException remoteException) {
      throw remoteException;
    } catch (Exception exception) {
      throw new UnexpectedException("undeclared checked exception", exception);
    } 
  }
  
  public Entry read(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException {
    try {
      Object object = this.ref.invoke(this, $method_read_1, new Object[] { paramEntry, paramTransaction, new Long(paramLong), paramInetAddress }, -4412170560440751007L);
      return (Entry)object;
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (RemoteException remoteException) {
      throw remoteException;
    } catch (Exception exception) {
      throw new UnexpectedException("undeclared checked exception", exception);
    } 
  }
  
  public Entry readIfExists(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException {
    try {
      Object object = this.ref.invoke(this, $method_readIfExists_2, new Object[] { paramEntry, paramTransaction, new Long(paramLong), paramInetAddress }, 6183293191368292146L);
      return (Entry)object;
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (RemoteException remoteException) {
      throw remoteException;
    } catch (Exception exception) {
      throw new UnexpectedException("undeclared checked exception", exception);
    } 
  }
  
  public Integer register(String paramString, RemoteEventListener paramRemoteEventListener) throws RemoteException {
    try {
      Object object = this.ref.invoke(this, $method_register_3, new Object[] { paramString, paramRemoteEventListener }, 8302828689066225128L);
      return (Integer)object;
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (RemoteException remoteException) {
      throw remoteException;
    } catch (Exception exception) {
      throw new UnexpectedException("undeclared checked exception", exception);
    } 
  }
  
  public Entry take(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException {
    try {
      Object object = this.ref.invoke(this, $method_take_4, new Object[] { paramEntry, paramTransaction, new Long(paramLong), paramInetAddress }, -2530833330653332266L);
      return (Entry)object;
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (RemoteException remoteException) {
      throw remoteException;
    } catch (Exception exception) {
      throw new UnexpectedException("undeclared checked exception", exception);
    } 
  }
  
  public Entry takeIfExists(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException {
    try {
      Object object = this.ref.invoke(this, $method_takeIfExists_5, new Object[] { paramEntry, paramTransaction, new Long(paramLong), paramInetAddress }, -4224175275905265133L);
      return (Entry)object;
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (RemoteException remoteException) {
      throw remoteException;
    } catch (Exception exception) {
      throw new UnexpectedException("undeclared checked exception", exception);
    } 
  }
  
  public Lease write(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException {
    try {
      Object object = this.ref.invoke(this, $method_write_6, new Object[] { paramEntry, paramTransaction, new Long(paramLong), paramInetAddress }, -4789889552524711651L);
      return (Lease)object;
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (RemoteException remoteException) {
      throw remoteException;
    } catch (Exception exception) {
      throw new UnexpectedException("undeclared checked exception", exception);
    } 
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\nucleus\Nucleus_Stub.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */