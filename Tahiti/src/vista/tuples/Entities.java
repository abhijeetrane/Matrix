// Team: Suva
// Vers: 1.1 Fixed Entities constructor. 2005/03/10. RC
// Date: 2005/02/27
// Vers: 1.0
// File: Entities.java

package vista.tuples;

import java.util.LinkedList;
import vista.util.ClidTable;
import vista.object.Entity;
import vista.tuples.Iterator;

/**
    This abstract class is the superclass for the LiveEntities and DeadEntities
    classes.
    <p>
    It contains a linked list of Entitys and a reference to a ClidTable, and
    allows for insertion, removal (in the subclasses), and lookups of Entitys.
    <p>
    @see vista.object.Entity
    @author Team Suva
*/
public abstract class Entities {
  /** LinkedList of tuples. */
  protected java.util.LinkedList tuples;

  /** clid table for this set of Entitys.
      There is only one per running program, which LiveEntities and
      DeadEntities both reference.
  */
  //private static vista.util.ClidTable clidtab;
  public static vista.util.ClidTable clidtab = new vista.util.ClidTable( );

  /** Constructor.
      Since clidtab is static, we will replace the reference in the second
      call to this constructor, but we don't know whether LiveEntities or
      DeadEntities constructor will be called first.
   */
  public Entities() {
    this.tuples = new java.util.LinkedList();
    //this.clidtab = new vista.util.ClidTable();
  }

  /** Inserts a new Entity into the Entities linked list.
      @param entity the new Entity to be inserted.
  */
  public void insert(vista.object.Entity entity) {
  	synchronized(tuples){
    tuples.add(entity);
    tuples.notify();
  	}
  }

  /** Looks up an Entity in the Entities linked list with a given spid.
      @param spid the spid of the Entity to be located.
      @return A vista.object.Entity if the lookup succeeded, or null otherwise.
  */
  public vista.object.Entity lookup(long spid) {
  	synchronized(tuples){
    vista.tuples.Iterator iter = getIterator();
    while (true) {
      Entity entity = iter.next();
      if (entity == null) {
        return null;
      }
      if (entity.getId() == spid) {
        return entity;
      }
    }
  	}
  }

  /** Returns the number of Entitys in the tuples LinkedList.
      @return the number of Entitys in the tuples LinkedList.
  */
  public  int getSize() {
    return tuples.size();
  }

  /** Returns an interator over all Entitys in the tuples LinkedList.
      @return a vista.tuples.Iterator.
  */
  public vista.tuples.Iterator getIterator() {
    return new vista.tuples.Iterator(tuples.listIterator(0));
  }

  /** Returns an interator over all Entitys of a given class id in the tuples
      LinkedList.
      @return a vista.tuples.Iterator.
  */
  public vista.tuples.Iterator getIterator(Integer clid) {
    return new vista.tuples.Iterator(tuples.listIterator(0), clid, clidtab);
  }

  /** Remove an Entity from the tuples LinkedList.
      @return The Entity which was removed, null if nothing was removed.
  */
  public abstract vista.object.Entity remove(long spid);
  
  public LinkedList getTuples(){
  	return this.tuples;
  }
}

