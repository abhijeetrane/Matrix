// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: MruMethod.java

package vista.tuples;

import java.util.Vector;
import vista.object.EntityLocator;
import vista.object.Heat;
import vista.object.Cell;

/** This class implement the MRU update method.

    @author Ron Coleman, Ph.D.
*/
public class MruMethod implements UpdateMethod {
  /** Set to false to suppress debug activity. */
  private final static boolean DEBUG = true;

  /** Reference to entities. */
  private Entities entities;

  /** Constructor.
      @param entities Entities to calculate space map the locators. */
  public MruMethod(Entities entities ) {
    this.entities = entities;
  }

  /** Get the locators based as a function of entity MRU.
      @return Vector of locators.
      @see vista.object.EntityLocator */
  public Vector getLocators() {

    Vector locators = new Vector( );

    return locators;
  }

}