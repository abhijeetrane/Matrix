package space.nucleus;

import java.util.Vector;
import space.Entry;
import space.remote.RemoteEventListener;

public class NotifyHandler {
  private Vector<Pinnable> pintable = new Vector<Pinnable>();
  
  public NotifyHandler() {
    Logger.getInstance().write("Notify handler " + Logger.getVersion("$Revision: 10 $") + "here...");
  }
  
  public int register(String paramString, RemoteEventListener paramRemoteEventListener) {
    synchronized (this.pintable) {
      Pinnable pinnable = new Pinnable(paramString, paramRemoteEventListener);
      int i = pinnable.getRid();
      this.pintable.add(pinnable);
      Logger.getInstance().write("NOTIFY registered " + paramRemoteEventListener.getClass().getName() + " @ " + paramString + " rid = " + i);
      return i;
    } 
  }
  
  public int pin(Integer paramInteger, Entry paramEntry) {
    synchronized (this.pintable) {
      for (byte b = 0; b < this.pintable.size(); b++) {
        Pinnable pinnable = this.pintable.elementAt(b);
        if (pinnable.getRid() == paramInteger.intValue()) {
          int i = pinnable.pin(paramEntry);
          Logger.getInstance().write("NOTIFY pinned " + paramEntry.getClass().getName() + " to rid = " + paramInteger + " pinid = " + i);
          return i;
        } 
      } 
    } 
    Logger.getInstance().write("NOTIFY rid = " + paramInteger + " not found in pin table!");
    return -1;
  }
  
  public void testEntry(Entry paramEntry) {
    if (this.pintable.size() == 0)
      return; 
    Dispatcher dispatcher = new Dispatcher(this.pintable, paramEntry);
    dispatcher.start();
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\nucleus\NotifyHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */