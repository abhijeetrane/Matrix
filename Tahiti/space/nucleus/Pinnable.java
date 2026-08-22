package space.nucleus;

import java.util.Enumeration;
import java.util.Hashtable;
import space.Entry;
import space.remote.RemoteEventListener;

class Pinnable {
  private static int registryid = 0;
  
  private Hashtable templates = new Hashtable<Object, Object>();
  
  private RemoteEventListener listener;
  
  private String host;
  
  private int rid;
  
  private int pinid = -1;
  
  private Enumeration pins = null;
  
  public Pinnable(String paramString, RemoteEventListener paramRemoteEventListener) {
    this.host = paramString;
    this.listener = paramRemoteEventListener;
    this.rid = registryid++;
  }
  
  public RemoteEventListener getListener() {
    return this.listener;
  }
  
  public int pin(Entry paramEntry) {
    this.pinid++;
    this.templates.put(new Integer(this.pinid), paramEntry);
    return this.pinid;
  }
  
  public int getRid() {
    return this.rid;
  }
  
  public int nextPin() {
    if (this.pins == null)
      this.pins = this.templates.keys(); 
    return !this.pins.hasMoreElements() ? -1 : ((Integer)this.pins.nextElement()).intValue();
  }
  
  public Entry getTemplate(int paramInt) {
    return (Entry)this.templates.get(new Integer(paramInt));
  }
  
  public String getHost() {
    return this.host;
  }
  
  public void reset() {
    this.pins = null;
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\nucleus\Pinnable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */