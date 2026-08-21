// Team: Suva
// Date: 2005/02/27
// Vers: 1.0
// File: ClidTableIterator.java

package vista.util;

import java.util.Enumeration;
import vista.util.Clid;

/**
    This class is the iterator for the hash table of class ids for VISTA.

    When getIterator() is called on vista.util.ClidTable, this is the object
    which is returned.<p>
    
    It is NOT synchronized with the ClidTable <b>(should it be?)</b>, so next()
    could miss new key/value pairs added after.<p>

    @author Team Suva
*/
public class ClidTableIterator {
  /** The Enumeration of the ClidTable's values (Clids). */
  private Enumeration enum1;

  /** Constructor.
      This constructor builds an iterator over the values in the ClidTable.
   */
  public ClidTableIterator(java.util.Enumeration enum1) {
    this.enum1 = enum1;
  }

  /** Returns the next Clid in the Enumeration of the ClidTable.
      Return the next Clid, or null if there are no more.
  */
  public vista.util.Clid next() {
    if (enum1.hasMoreElements()) {
      return (vista.util.Clid)enum1.nextElement();
    }
    return null;
  }
}

