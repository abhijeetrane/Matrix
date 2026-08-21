//Team: Ron Coleman
//Date: 2005/03/09
//Vers: 1.0
//File: SpidData.java


package vista.object;
/** This class represents format of Spids data in Vista UI.

@author Tahiti.
*/

public class SpidData {
  private Long spid;
  private Integer clid;
  private String className;

  public SpidData(Long spid,Integer clid,String className) {
    this.spid = spid;
    this.clid = clid;
    this.className = className;
  }

  public SpidData(long spid,int clid,String className) {
    this.spid = new Long(spid);
    this.clid = new Integer(clid);
    this.className = className;
  }

  public Long getSpid() {
    return spid;
  }

  public Integer getClid( ) {
    return clid;
  }

  public String getClassName( ) {
    return className;
  }
}
