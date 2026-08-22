// Team: Suva
// Date: 2005/02/27
// Vers: 1.0
// File: LiveEntities.java

package vista.tuples;

import java.util.LinkedList;
import vista.object.Entity;
import vista.tuples.Iterator;
import vista.tuples.Entities;

/**
    This class contains the Entitys which wrap MetaEntrys which are still in
    the space.
    
    LiveEntities extends Entities abstract class.

    It contains a linked list of live Entitys and a reference to a ClidTable,
    and allows for insertion, removal, and lookups of live Entitys.

    @author Team Suva
*/
public class LiveEntities extends Entities {
  /** Constructor.
      Since clidtab is static, we will set the reference in the superclass's
      constructor, but we don't know whether LiveEntities or DeadEntities
      constructor will be called first, so we will do it in both places, since
      our implementation will create both a LiveEntities object and
      DeadEntities object before the ClidTable object will be used.

      @see vista.tuples.Entities
   */
  public LiveEntities() {
    super();
  }

  /** Remove a live Entity from the tuples LinkedList.
      @return The Entity which was removed, null if nothing was removed.
  */
  public vista.object.Entity remove(long spid) {
  	synchronized(tuples){
    vista.tuples.Iterator iter = new vista.tuples.Iterator(tuples.listIterator(0));
    while (true) {
      Entity entity = iter.next();
      if (entity == null) {
        return null;
      }
      if (entity.getId() == spid) {
        if (tuples.remove(entity)) {
          return entity;
	} else {
	  return null;
	}
      }
    }
  }
}
}
