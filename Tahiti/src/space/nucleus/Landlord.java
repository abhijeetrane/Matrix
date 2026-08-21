package space.nucleus;

import java.util.LinkedList;
import java.util.ListIterator;

public class Landlord extends Thread {
  private static final int PERIOD = 5000;
  
  private LinkedList tuples = null;
  
  public Landlord(LinkedList paramLinkedList) {
    this.tuples = paramLinkedList;
  }
  
  public void run() {
    Logger.getInstance().write("Landlord " + Logger.getVersion("$Revision: 8 $") + "here...");
    try {
      while (true) {
        Thread.sleep(5000L);
        evict(this.tuples);
      } 
    } catch (Exception exception) {
      System.err.println("Landload thread: " + exception);
      return;
    } 
  }
  
  private void evict(LinkedList paramLinkedList) {
    synchronized (paramLinkedList) {
      int bool = 0;
      ListIterator<MetaEntry> listIterator = paramLinkedList.listIterator(bool);
      while (listIterator.hasNext()) {
        MetaEntry metaEntry = listIterator.next();
        long l = System.currentTimeMillis();
        if (metaEntry.isevictable(l)) {
          listIterator.remove();
          if (Nucleus.getListener() != null && (Nucleus.getMask() & 0x80L) != 0L)
            Nucleus.getListener().actionPerformed(128, metaEntry, null); 
          Logger.getInstance().write("EXPIRE " + metaEntry.getEntry().getClass().getName() + " spid " + metaEntry.getId() + " size " + paramLinkedList.size());
        } 
      } 
    } 
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\nucleus\Landlord.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */