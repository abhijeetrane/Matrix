// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: AgeMethod.java

package vista.tuples;

import java.util.Vector;
import vista.object.EntityLocator;
import vista.object.Heat;
import vista.object.Cell;

/** This class implement the Age update method.

    @author Ron Coleman, Ph.D.
*/
public class AgeMethod implements UpdateMethod {
  /** Set to false to suppress debug activity. */
  private final static boolean DEBUG = true;

  /** Reference to entities. */
  private Entities entities;

  /** Constructor.
      @param entities Entities to calculate space map the locators. */
  public AgeMethod(Entities entities ) {
    this.entities = entities;
  }

  /** Get the locators based as a function of entity age.
      @return Vector of locators.
      @see vista.object.EntityLocator */
  public Vector getLocators() {

    Vector locators = new Vector( );

    return locators;
  }

}