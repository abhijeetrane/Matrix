// Team: Suva
// Vers: 1.1 Fixed isexpired method. 2005/03/10. RC
// Date: 2005/02/27
// Vers: 1.0
// File: Entity.java

package vista.object;

import space.Entry;
import space.util.DeepCopy;
import space.nucleus.MetaEntry;
import java.net.InetAddress;

/**
    This class contains the meta information needed by VISTA for maintaining a
    view of all Entrys in the space.

    It wraps a MetaEntry, and contains other details needed to properly track
    and represent Entrys in VISTA.

    @author Team Suva
*/
public class Entity {
  /** MetaEntry wrapped by an Entity. */
  private space.nucleus.MetaEntry mentry;

  /** creation time for the Entry wrapped by an Entity. */
  private long birth;

  /** expiration time for the Entry wrapped by an Entity. */
  private long death;

  /** count of reads for the Entry wrapped by an Entity. */
  private long reads = 0;

  /** time of most recent access for the Entry wrapped by an Entity. */
  private long mru;

  /** Constructor.
      @param mentry MetaEntry to be wrapped
   */
  public Entity(space.nucleus.MetaEntry mentry) {
    this.mentry = mentry;
    this.birth = System.currentTimeMillis();
    this.death = mentry.getExpiration();
    this.mru = System.currentTimeMillis();
  }

  /** Determines if the wrapped MetaEntry was disposed vs taken or expired.
      <p>
      An entry was taken if taker != null, expired if taker==null && !disposed.
      @return True if disposed.
  */
  public boolean isdisposed() {
    return mentry.isdisposed();
    
  }

  /** Determines if the wrapped MetaEntry was taken vs disposed or expired.
      <p>
      An entry was taken if taker != null, expired if taker==null && !disposed.
      @return True if taken.
  */
  public boolean isremoved() {
    return getTaker() != null;
  }

  /** Determines if the wrapped MetaEntry was expired vs taken or disposed.
      <p>
      An entry was taken if taker != null, expired if taker==null && !disposed and...
      expiration time has passed
      @return True if expired.
  */
  public boolean isexpired() {
    //return ((getTaker() == null) && !isdisposed());
    long now = System.currentTimeMillis();
    return ((getTaker() == null) && !isdisposed() && mentry.getExpiration() < now );
    //return (now > mentry.getExpiration());
  }

  /** Gets a reference to the Entry this Entity is tracking.
      @return An Entry reference.
  */
  public space.Entry getEntry() {
    return mentry.getEntry();
  }

  /** Gets a deep copy of the Entry this Entity is tracking.
      @return A deep copy of the Entry.
  */
  public space.Entry getEntryCopy() {
  	
    return (space.Entry)space.util.DeepCopy.copy(mentry.getEntry());
  }

  /** Gets the space id of the Entry this Entity is tracking.
      @return Space id of this entry.
  */
  public long getId() {
    return mentry.getId();
    
  }

  /** Get the address of owner.
      @return The address.
  */
  public java.net.InetAddress getOwner() {
    return mentry.getOwner();
  }

  /** Get the address of taker.
      @return The address.
  */
  public java.net.InetAddress getTaker() {
    return mentry.getTaker();
  }

  /** Get the MRU attribute.
      @return The MRU value.
  */
  public long getMRU() {
    return mru;
  }

  /** Get the time of death for the Entry.
      @return The death value.
  */
  public long getDeath() {
    return death;
  }

  /** Get the time of birth for the Entry.
      @return The birth value.
  */
  public long getBirth() {
    return birth;
  }

  /** Get the age of the Entry wrapped by this Entity.
      @return The age.
  */
  public long getAge() {
    return System.currentTimeMillis() - birth;
  }

  /** Return the String-ified form of this Entity.
      @return String of this Entity.<p>
      <b>Question: Is this REALLY needed, or was it a typo in the class diagram?</b>
   */
  public String toString() {
    return "classname = " + getEntry().getClass().getName() + " spid = " +
           getId() + " birth = " + birth + " death = " + death;
  }

  /** Set a new Entry for the wrapped MetaEntry.
      @param newEntry The new Entry to be inserted in the wrapped MetaEntry.
   */
  public void setEntry(space.Entry newEntry) {
    mentry.setEntry(newEntry);
  }

  /** Set the MRU for this Entity.
      @param mru The updated MRU value.
   */
  public void setMRU(long mru) {
    this.mru = mru;
  }

  /** Increment the reads count. */
  public void incrementReads() {
    reads++;
  }

  /** Set the disposed flag on the wrapped MetaEntry. */
  public void setDisposed() {
    mentry.setDisposed();
  }
  public long getReads(){
  	return this.reads;
  }
  public void setDeath(long now){
    this.death=now;
  }
}

