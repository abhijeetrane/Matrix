//Team: Tahiti
//Date: 2005/04/05
//Vers: 1.0
//File: InspectSpidData.java

package vista.object;
/** This class represents format of Inspect dialog data in Vista UI.

@author Tahiti.
*/

public class InspectSpidData {
/** attribute*/
private String attribute;
/**value*/
private String value;
/**Constructor
 *
 * @param attribute
 * @param value
 */
  public InspectSpidData(String attribute,String value) {
  this.attribute=attribute;
  this.value=value;
  }
/** Get the attribute.
  @return attribute. */
  public String getAttribute() {
    return attribute;
  }
/** Get the value.
  @return value. */
  public String getValue( ) {
    return value;
  }

}

