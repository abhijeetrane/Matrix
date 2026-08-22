// Team: Suva
// Date: 2005/02/27
// Vers: 1.0
// File: Iterator.java

package vista.tuples;

import vista.object.Entity;

/**
    This class is an iterator for walking either LiveEntities or DeadEntities.

    It can either walk all Entities, if the iterator was created without
    specifying a class id, or all Entities of a given class if created with a
    class id.

    @author Team Suva
*/
public class Iterator {
  /** class id (if iterator was created to walk for a specific class id). */
  private Integer clid;

  /** class id used when the iterator is not walking for a specific class id. */
  private static final int NOCLID = -1;

  /** ListIterator which does the walking of the LinkedList under the covers.
      This is really just a reference to this;
   */
  private java.util.ListIterator iter;

  /** Clid table reference (if iterator was created to walk for a specific
      class id).
   */
  private vista.util.ClidTable clidtab = null;

  /** Constructor.
      This constructor builds an iterator which will walk all Entities.
      @param iter ListIterator used under the covers to walk the Entities
   */
  public Iterator(java.util.ListIterator iter) {
    this.iter = iter;
    this.clid = new Integer(NOCLID);
  }

  /** Constructor.
      This constructor builds an iterator which will walk all Entities of a
      given class.
      @param iter ListIterator used under the covers to walk the Entities
      @param clid Class id that Entities returned by this iterator will be
      @param clidtab Clid table
   */
  public Iterator(java.util.ListIterator iter, Integer clid, vista.util.ClidTable clidtab) {
    this.iter = iter;
    this.clid = clid;
    this.clidtab = clidtab;
  }

  /** Returns the next Entity in the Entities, or null if no more are to be found.
      @return the next Entity, or null if there are no more left
  */
  public vista.object.Entity next() {
  	  
    if (clid.intValue() == NOCLID) {
      if (iter.hasNext()) {
        return (vista.object.Entity) iter.next();
      }
    } else {
      while (iter.hasNext()) {
        vista.object.Entity entity =  (vista.object.Entity)iter.next();
        
        if (clidtab.get(entity.getEntry()).getId().compareTo(clid) == 0)
              return entity;
      }
    }
    return null;
  }
}

