//Team: Tahiti
//Date: 2005/04/05
//Vers: 1.0
//File: ClidData.java

package vista.object;
/** This class represents format of Clid data in Vista UI.

@author Tahiti.
*/
public class ClidData {
/** Clid */
  private Integer clid;
/** Class name */
  private String className;
/**Constructor
 * 
 * @param clid
 * @param className
 */
  
  public ClidData(Integer clid,String className) {
    this.clid = clid;
    this.className = className;
  }
/**Constructor
 * 
 * @param clid
 * @param className
 */
  public ClidData(int clid,String className) {
    this.clid = new Integer(clid);
    this.className = className;
  }
/** Get the clid.
  @return clid. */
  public Integer getClid( ) {
    return clid;
  }
/** Get the classname.
  @return classname. */
  public String getClassName( ) {
    return className;
  }
}
