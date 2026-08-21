package space.remote;

import java.rmi.MarshalledObject;
import java.util.EventObject;

public class RemoteEvent extends EventObject {
  private Object src;
  
  private MarshalledObject mobj;
  
  public RemoteEvent(Object paramObject, MarshalledObject paramMarshalledObject) {
    super(paramObject);
    this.src = paramObject;
    this.mobj = paramMarshalledObject;
  }
  
  public Object getSource() {
    return this.src;
  }
  
  public long getID() {
    return 0L;
  }
  
  public long getSequenceNumber() {
    return 0L;
  }
  
  public MarshalledObject getRegistrationObject() {
    return this.mobj;
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\RemoteEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */