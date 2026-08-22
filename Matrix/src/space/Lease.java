package space;

import java.io.Serializable;
import java.rmi.RemoteException;

public class Lease implements Serializable {
  public static final long FOREVER = 9223372036854775807L;
  
  public static final long ANY = -1L;
  
  public static final int DURATION = 1;
  
  public static final int ABSOLUTE = 2;
  
  private long expiration;
  
  public Lease(long paramLong) {
    this.expiration = paramLong;
  }
  
  public long getExpiration() {
    return this.expiration;
  }
  
  public void cancel() {}
  
  public void renew(long paramLong) throws RemoteException {}
  
  void setSerialFormat(int paramInt) {}
  
  int getSerialFormat() {
    return 0;
  }
  
  boolean canBatch(Lease paramLease) {
    return false;
  }
}