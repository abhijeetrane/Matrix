package space.remote;

import java.util.Hashtable;

class Jumper extends Thread {
  private RemoteEvent rev;
  
  private Hashtable bindtab;
  
  public Jumper(RemoteEvent paramRemoteEvent, Hashtable paramHashtable) {
    this.rev = paramRemoteEvent;
    this.bindtab = paramHashtable;
  }
  
  public void run() {
    try {
      Passback passback = (Passback)this.rev.getRegistrationObject().get();
      Integer integer1 = passback.getPid();
      Integer integer2 = passback.getRid();
      Binding binding = (Binding)this.bindtab.get(integer1);
      if (binding == null) {
        System.err.println("Internal error passback pinid = " + integer1 + " for rid = " + integer2 + " not found!");
        return;
      } 
      RemoteEventListener remoteEventListener = binding.getListener();
      remoteEventListener.notify(new RemoteEvent("proxylistener", binding.getHandback()));
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\Jumper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */