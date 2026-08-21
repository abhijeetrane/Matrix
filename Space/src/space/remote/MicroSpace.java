package space.remote;

import java.net.InetAddress;
import java.rmi.MarshalledObject;
import space.Entry;
import space.Lease;
import space.SpaceService;
import space.Transaction;

public class MicroSpace implements SpaceService {
  private ProxyListener proxy;
  
  private Integer rid;
  
  private RemoteSpaceService spacesvc;
  
  private InetAddress address;
  
  public MicroSpace(RemoteSpaceService paramRemoteSpaceService) throws Exception, SpaceException {
    if (paramRemoteSpaceService == null)
      throw new SpaceException("Remote space service reference is null."); 
    this.spacesvc = paramRemoteSpaceService;
    this.address = InetAddress.getLocalHost();
  }
  
  public Lease write(Entry paramEntry, Transaction paramTransaction, long paramLong) throws Exception {
    return this.spacesvc.write(paramEntry, paramTransaction, paramLong, this.address);
  }
  
  public Entry take(Entry paramEntry, Transaction paramTransaction, long paramLong) throws Exception {
    return this.spacesvc.take(paramEntry, paramTransaction, paramLong, this.address);
  }
  
  public Entry takeIfExists(Entry paramEntry, Transaction paramTransaction, long paramLong) throws Exception {
    return this.spacesvc.takeIfExists(paramEntry, paramTransaction, paramLong, this.address);
  }
  
  public Entry read(Entry paramEntry, Transaction paramTransaction, long paramLong) throws Exception {
    return this.spacesvc.read(paramEntry, paramTransaction, paramLong, this.address);
  }
  
  public Entry readIfExists(Entry paramEntry, Transaction paramTransaction, long paramLong) throws Exception {
    return this.spacesvc.readIfExists(paramEntry, paramTransaction, paramLong, this.address);
  }
  
  public Entry snapshot(Entry paramEntry) throws Exception {
    return paramEntry;
  }
  
  public void notify(Entry paramEntry, Transaction paramTransaction, RemoteEventListener paramRemoteEventListener, long paramLong, MarshalledObject paramMarshalledObject) throws Exception {
    if (this.proxy == null) {
      this.proxy = new ProxyListener();
      this.rid = this.spacesvc.register(InetAddress.getLocalHost().getHostName(), this.proxy);
      if (this.rid == null)
        throw new SpaceException("Nucleus was unable register listener."); 
    } 
    Integer integer = this.spacesvc.pin(this.rid, paramEntry, this.address);
    if (integer == null)
      throw new SpaceException("Nucleus was unable to pin template."); 
    this.proxy.add(integer, paramRemoteEventListener, paramMarshalledObject);
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\MicroSpace.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */