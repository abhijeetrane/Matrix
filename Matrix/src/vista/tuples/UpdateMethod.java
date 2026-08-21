// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: UpdateMethod.java

package vista.tuples;

import java.util.Vector;

/** This interface is used to create locators unique to a method.

    @author Ron Coleman, Ph.D.
*/
public interface UpdateMethod {

  /** Get the locators.
      @return Vector of locators.
      @see vista.object.EntityLocator */
  public Vector getLocators( );
}