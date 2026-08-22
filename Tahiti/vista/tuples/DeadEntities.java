// Team: Suva
// Date: 2005/02/27
// Vers: 1.0
// File: DeadEntities.java

package vista.tuples;

import java.util.LinkedList;
import vista.object.Entity;
import vista.tuples.Entities;
import vista.tuples.Iterator;

/**
    This class contains the Entitys which wrap MetaEntrys which are no longer
    in the space.
    
    DeadEntities extends Entities abstract class.

    It contains a linked list of dead Entitys and a reference to a ClidTable,
    and allows for insertion, removal (sometimes), and lookups of dead Entitys.

    @author Team Suva
*/
public class DeadEntities extends Entities {
  /** Constructor.
      Since clidtab is static, we will set the reference in the superclass's
      constructor, but we don't know whether LiveEntities or DeadEntities
      constructor will be called first, so we will do it in both places, since
      our implementation will create both a LiveEntities object and
      DeadEntities object before the ClidTable object will be used.

      @see vista.tuples.Entities
   */
  public DeadEntities() {
    super();
  }

  /** The "spid" value to pass if you just don't care which Dead Entitys are to
      be deleted.
      @see #remove(long)
  */
  public final static long DONTCARE = -1;

  /** The timeout for dead Entitys to be kept on life support.
      @see #remove(long)
  */
  public final static long LIFESUPPORT = 10*60*1000;

  /** Remove a dead Entity from the tuples LinkedList.
      This may or may not actually remove the Entity with the given spid, but
      probably not. (see below)<p>

      <em>There is no reason to trim the linked list in DeadEntities, except
      when they have been in the list for too long (over LIFESUPPORT
      milliseconds old).</em>
      <p>
      This will probably be called from the GUI "occasionally," and, if called
      with a spid, will return an Entity ONLY if the Entity with that spid was
      removed during the pruning. However, you can call with a spid of
      DeadEntities.DONTCARE, indicating that you just want to prune. This will
      be the most common usage.
      @return The Entity which was removed, null if none with that spid was.
      @see #LIFESUPPORT
  */
  public Entity remove(long spid) {
    Entity entityToReturn = null;
   
    try {
      while (true) {
        Entity entity = (Entity)tuples.getFirst();
         
        if (entity.getDeath() > (System.currentTimeMillis() - LIFESUPPORT)) {
          break;
        }

        if (entity.getId() == spid) {
          entityToReturn = entity;
        }
        tuples.removeFirst();
         
         }
      
      }
    catch (java.util.NoSuchElementException e) {
    }
    return entityToReturn;
  }
}

