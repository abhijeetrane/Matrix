package space.nucleus;

import java.net.InetAddress;
import space.Entry;
import space.Transaction;

public class MetaEntry {
  private static int spaceid = 0;
  
  private Entry entry;
  
  private long expiration;
  
  private Transaction txn;
  
  private long id;
  
  private InetAddress owner;
  
  private InetAddress taker;
  
  private boolean disposed = false;
  
  private long attribute;
  
  public MetaEntry(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) {
    this.entry = paramEntry;
    this.txn = paramTransaction;
    this.owner = paramInetAddress;
    if (paramLong == Long.MAX_VALUE) {
      this.expiration = Long.MAX_VALUE;
    } else {
      this.expiration = paramLong + System.currentTimeMillis();
    } 
    this.id = spaceid++;
  }
  
  public Entry getEntry() {
    return this.entry;
  }
  
  public boolean isevictable(long paramLong) {
    return (paramLong > this.expiration);
  }
  
  public boolean isdisposed() {
    return this.disposed;
  }
  
  public long getId() {
    return this.id;
  }
  
  public long getExpiration() {
    return this.expiration;
  }
  
  public InetAddress getOwner() {
    return this.owner;
  }
  
  public InetAddress getTaker() {
    return this.taker;
  }
  
  public long getAttribute() {
    return this.attribute;
  }
  
  public void setAttribute(long paramLong) {
    this.attribute = paramLong;
  }
  
  public void setEntry(Entry paramEntry) {
    this.entry = paramEntry;
  }
  
  public void setTaker(InetAddress paramInetAddress) {
    this.taker = paramInetAddress;
  }
  
  public void setDisposed() {
    this.disposed = true;
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\nucleus\MetaEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */