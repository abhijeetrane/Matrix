package space.remote;

import java.rmi.MarshalledObject;

class Binding {
  private RemoteEventListener client;
  
  private MarshalledObject handback;
  
  public Binding(RemoteEventListener paramRemoteEventListener, MarshalledObject paramMarshalledObject) {
    this.client = paramRemoteEventListener;
    this.handback = paramMarshalledObject;
  }
  
  RemoteEventListener getListener() {
    return this.client;
  }
  
  MarshalledObject getHandback() {
    return this.handback;
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\Binding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */