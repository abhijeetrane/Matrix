package space.remote;

import java.io.Serializable;

public class Passback implements Serializable {
  private int rid;
  
  private int pid;
  
  public Passback(int paramInt1, int paramInt2) {
    this.rid = paramInt1;
    this.pid = paramInt2;
  }
  
  public Integer getRid() {
    return new Integer(this.rid);
  }
  
  public Integer getPid() {
    return new Integer(this.pid);
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\Passback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */