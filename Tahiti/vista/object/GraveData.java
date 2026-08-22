//Team: Tahiti
//Date: 2005/04/05
//Vers: 1.0
//File: GraveData.java

package vista.object;
/** This class represents format of Grave yard data in Vista UI.

@author Tahiti.
*/

public class GraveData {
  /** spid*/
  private Long spid;
  /** clid*/
  private Integer clid;
  /** classname*/
  private String className;
  /** time*/
  private String time;
  /** takenby*/
  private String takenby;

  /** Constructor
   *
   * @param spid
   * @param clid
   * @param className
   * @param time
   * @param takenby
   */
  public GraveData(Long spid,Integer clid,String className,String time,String takenby) {
    this.spid = spid;
    this.clid = clid;
    this.className = className;
    this.time=time;
    this.takenby=takenby;

  }
 /** Constructor
  *
  * @param spid
  * @param clid
  * @param className
  * @param time
  * @param takenby
  */
  public GraveData(long spid,int  clid,String className,String time,String takenby) {
  	this.spid = new Long(spid);
    this.clid = new Integer(clid);
    this.className = className;
    this.time=time;
    this.takenby=takenby;

  }
/** get the spid
 *  @return spid
 */
  public Long getSpid() {
    return spid;
  }
/** get the clid
 *  @return clid
 */
  public Integer getClid( ) {
    return clid;
  }
/** get the classname
 *  @return classname
 */
  public String getClassName( ) {
    return className;
  }
  /** get the time
   * @return time
   */

  public String getTime() {
    return time;
  }
  /**get the takenby
   * @return takenby
   */
  public String getTakenby() {
    return takenby;
  }

}
