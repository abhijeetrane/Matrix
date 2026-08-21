// Team: Suva
// Date: 2005/02/27
// Vers: 1.0
// File: Clid.java

package vista.util;

import space.Entry;

/**
    This class is the value stored in vista.util.ClidTable.

    It holds a space.Entry and an Integer.<p>

    @author Team Suva
*/
public class Clid {
  /** the entry, from which the class name can be determined. */
  private space.Entry entry;

  /** the unique class id assigned to this class name. */
  private  Integer id;

  /** Constructor.
      This constructor builds a Clid to be put() into the clid hash table.
   */
  public Clid(space.Entry entry, Integer id) {
    this.entry = entry;
    this.id = id;
  }

  /** Retrieves the class name from the Clid.
      @return A String containing the class name.
  */
  public String getName() {
    return entry.getClass().getName();
  }

  /** Retrieves the class id from the Clid.
      @return An Integer containing the class id.
  */
  public Integer getId() {
    return id;
  }
}

