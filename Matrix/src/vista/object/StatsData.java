//Team: Tahiti
//Date: 2005/04/05
//Vers: 1.0
//File: StatsData.java

package vista.object;
/** This class represents format of Stats data in Vista UI.

@author Tahiti.
*/
public class StatsData {
/** Param */  
private String param;
/** Value */
private String value;
/**Constructor
 * 
 * @param param
 * @param value
 */
  public StatsData(String param,String value) {
  this.param=param;
  this.value=value;
  }
/** Get the Param.
  @return param. */
  public String getParam() {
    return param;
  }
/** Get the value.
  @return value. */
  public String getValue( ) {
    return value;
  }

}

