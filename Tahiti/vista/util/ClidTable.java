// Team: Suva
// Date: 2005/02/27
// Vers: 1.0
// File: ClidTable.java

package vista.util;

import java.util.Enumeration;
import java.util.Hashtable;
import space.Entry;
import vista.util.Clid;
import vista.util.ClidTableIterator;

/**
    This class is the hash table of class ids for VISTA.

    It holds a hash table of unique class name/Clid pairs, using class name as
    the key, and Clid as the value.<p>
    
    However, the get() and put() methods will actually take a space.Entry
    parameter instead for ease of use.

    @author Team Suva
*/
public class ClidTable extends java.util.Hashtable {
  /** the next unique class id to be assigned. */
  private static Integer id = new Integer(0);

  /** Constructor.
      This constructor builds an empty clid hash table.
   */
  public ClidTable() {
    super();
  }

  /** Attempts to insert a new unique class name/id pair into the hash table.
      class name is retrieved from the passed space.Entry, and we check to see
      if there is already an existing hash table entry using this class name
      before we insert.<p>

      We do the check for this key first, because Hashtable (our superclass)
      will just replace the previous value with a new value if the key already
      exists, and this is not the behavior we're looking for.
  */
  public void put(space.Entry entry) throws NullPointerException {
    if (get(entry) == null) {
      super.put(entry.getClass().getName(), new Clid(entry, id));
      id = new Integer(id.intValue()+1);
    }
  }

  /** Attempts to retrieve the unique id for a given class name from the hash
      table.
      class name is retrieved from the passed space.Entry, and we attempt to
      retrieve the value using this as the key.

      @param entry The space.Entry whose clid you would like.

      @return A vista.util.Clid, or null if this key is not in the hash table.
  */
  public vista.util.Clid get(space.Entry entry) {
    return (vista.util.Clid)super.get(entry.getClass().getName());
  }

  /** Returns the iterator for the clid table.
      <b>Not quite sure whether this is correct.</b><p>

      @return A vista.util.ClidTableIterator.
  */
  public vista.util.ClidTableIterator getIterator() {
    return new vista.util.ClidTableIterator(super.elements());
  }

  /** Returns the number of keys in the clid table.

      @return An int.
  */
  public int size() {
    return super.size();
  }
}

