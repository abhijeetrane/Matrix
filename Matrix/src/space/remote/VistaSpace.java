package space.remote;

import java.util.ListIterator;
import space.nucleus.Logger;
import space.nucleus.MetaEntry;
import space.nucleus.Nucleus;
import space.nucleus.NucleusListener;

public class VistaSpace extends RemoteSpace {
  public VistaSpace() throws Exception {}
  
  public VistaSpace(String paramString) throws Exception {
    super(paramString);
  }
  
  public VistaSpace(String paramString, Integer paramInteger) throws Exception {
    super(paramString, paramInteger, (Integer)null);
  }
  
  public VistaSpace(String paramString, Integer paramInteger1, Integer paramInteger2) throws Exception {
    super(paramString, paramInteger1, paramInteger2);
  }
  
  public void dispose(long paramLong) {
    synchronized (this.tuples) {
      long l = System.currentTimeMillis();
      ListIterator<MetaEntry> listIterator = this.tuples.listIterator(0);
      while (listIterator.hasNext()) {
        MetaEntry metaEntry = listIterator.next();
        if (!metaEntry.isevictable(l) && metaEntry.getId() == paramLong) {
          listIterator.remove();
          metaEntry.setDisposed();
          Logger.getInstance().write("DISPOSE " + metaEntry.getEntry().getClass().getName() + " spid " + metaEntry.getId() + " size " + this.tuples.size());
          return;
        } 
      } 
    } 
    Logger.getInstance().write("DISPOSE failed spid " + paramLong + " size " + this.tuples.size());
  }
  
  public static void setListener(NucleusListener paramNucleusListener, long paramLong) {
    Nucleus.listener = paramNucleusListener;
    Nucleus.mask = paramLong;
  }
  
  public static void setListener(NucleusListener paramNucleusListener) {
    setListener(paramNucleusListener, 255L);
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\remote\VistaSpace.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */