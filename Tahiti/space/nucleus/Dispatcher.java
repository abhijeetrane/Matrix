package space.nucleus;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.util.EventObject;
import java.util.Vector;
import space.Entry;
import space.remote.Passback;
import space.remote.RemoteEvent;
import space.remote.RemoteEventListener;

class Dispatcher extends Thread {
  private Vector pintable = null;
  
  private Entry entry;
  
  public Dispatcher(Vector paramVector, Entry paramEntry) {
    this.pintable = paramVector;
    this.entry = paramEntry;
  }
  
  public void run() {
    Vector<Pinnable> vector = new Vector();
    synchronized (this.pintable) {
      for (byte b1 = 0; b1 < this.pintable.size(); b1++)
        vector.add((Pinnable) this.pintable.elementAt(b1)); 
    } 
    Vector<Integer> vector1 = new Vector();
    for (byte b = 0; b < vector.size(); b++) {
      Pinnable pinnable = vector.elementAt(b);
      pinnable.reset();
      int i;
      while ((i = pinnable.nextPin()) != -1) {
        Entry entry = pinnable.getTemplate(i);
        if (Nucleus.entriesMatch(this.entry, entry)) {
          RemoteEventListener remoteEventListener = pinnable.getListener();
          int j = pinnable.getRid();
          String str = pinnable.getHost();
          try {
            MarshalledObject marshalledObject = new MarshalledObject(new Passback(j, i));
            remoteEventListener.notify((EventObject)new RemoteEvent("notifyhandler", marshalledObject));
            if (Nucleus.getListener() != null && (Nucleus.getMask() & 0x10L) != 0L)
              Nucleus.getListener().actionPerformed(16, null, entry); 
            Logger.getInstance().write("NOTIFY dispatched " + entry.getClass().getName() + " rid = " + j + " pinid = " + i + " host = " + str);
          } catch (RemoteException remoteException) {
            vector1.add(new Integer(j));
          } catch (Exception exception) {
            System.err.println(exception);
            exception.printStackTrace();
          } 
        } 
      } 
    } 
    deregister(vector1);
  }
  
  private void deregister(Vector<Integer> paramVector) {
    if (paramVector.size() == 0)
      return; 
    synchronized (this.pintable) {
      for (byte b = 0; b < paramVector.size(); b++) {
        Integer integer = paramVector.elementAt(b);
        for (byte b1 = 0; b1 < this.pintable.size(); b1++) {
          if (((Pinnable)this.pintable.elementAt(b1)).getRid() == integer.intValue()) {
            this.pintable.remove(b1);
            Logger.getInstance().write("NOTIFY deregistered rid = " + integer + " unreachable.");
            break;
          } 
        } 
      } 
    } 
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\nucleus\Dispatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */