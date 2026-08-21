package space.nucleus;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import space.Entry;
import space.Lease;
import space.Transaction;
import space.remote.RemoteEventListener;
import space.remote.RemoteSpaceService;

public class Nucleus extends UnicastRemoteObject implements RemoteSpaceService {
  private static final boolean DEBUG = false;
  
  protected LinkedList tuples = new LinkedList();
  
  private NotifyHandler notifier = null;
  
  protected static NucleusListener listener = null;
  
  protected static long mask;
  
  private int threadsWaiting = 0;
  
  private static final SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
  
  private static final String DUMPER_CLASS_NAME = "space.util.DumperEntry";
  
  public Nucleus(String paramString) throws RemoteException {
    Logger.getInstance().write("Space nucleus " + Logger.getVersion("$Revision: 18 $") + "started at " + paramString);
    Landlord landlord = new Landlord(this.tuples);
    landlord.start();
    this.notifier = new NotifyHandler();
  }
  
  public Lease write(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException {
    if (paramEntry == null) {
      Logger.getInstance().write("WRITE got null entry not written.");
      return new Lease(0L);
    } 
    if (isdumper(paramEntry)) {
      Logger.getInstance().dump(this.tuples);
      return new Lease(0L);
    } 
    MetaEntry metaEntry = insertEntry(paramEntry, paramLong, paramInetAddress);
    return new Lease(metaEntry.getExpiration());
  }
  
  public Entry take(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException {
    if (paramEntry == null) {
      Logger.getInstance().write("TAKE got null template not searchable.");
      return null;
    } 
    MetaEntry metaEntry = readEntry(paramEntry, paramLong, true);
    if (metaEntry == null) {
      Logger.getInstance().write("TAKE tmpl " + paramEntry.getClass().getName() + " no match size " + this.tuples.size() + " threads " + this.threadsWaiting);
      if (listener != null && (mask & 0x4L) != 0L)
        listener.actionPerformed(4, null, paramEntry); 
      return null;
    } 
    metaEntry.setTaker(paramInetAddress);
    if (listener != null && (mask & 0x4L) != 0L)
      listener.actionPerformed(4, metaEntry, paramEntry); 
    Logger.getInstance().write("TAKE " + metaEntry.getEntry().getClass().getName() + " spid " + metaEntry.getId() + " size " + this.tuples.size() + " threads " + this.threadsWaiting);
    return metaEntry.getEntry();
  }
  
  public Entry takeIfExists(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException {
    if (paramEntry == null) {
      Logger.getInstance().write("TAKEIF got null template not searchable.");
      return null;
    } 
    MetaEntry metaEntry = readEntry(paramEntry, 0L, true);
    if (metaEntry == null) {
      Logger.getInstance().write("TAKEIF tmpl " + paramEntry.getClass().getName() + " no match size " + this.tuples.size() + " waiting " + this.threadsWaiting);
      if (listener != null && (mask & 0x4L) != 0L)
        listener.actionPerformed(4, null, paramEntry); 
      return null;
    } 
    metaEntry.setTaker(paramInetAddress);
    if (listener != null && (mask & 0x4L) != 0L)
      listener.actionPerformed(4, metaEntry, paramEntry); 
    Logger.getInstance().write("TAKEIF " + metaEntry.getEntry().getClass().getName() + " spid " + metaEntry.getId() + " size " + this.tuples.size() + " waiting " + this.threadsWaiting);
    return metaEntry.getEntry();
  }
  
  public Entry read(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException {
    if (paramEntry == null) {
      Logger.getInstance().write("READ got null template not searchable.");
      return null;
    } 
    MetaEntry metaEntry = readEntry(paramEntry, paramLong, false);
    if (metaEntry == null) {
      Logger.getInstance().write("READ tmpl " + paramEntry.getClass().getName() + " no match size " + this.tuples.size() + " waiting " + this.threadsWaiting + ")");
      if (listener != null && (mask & 0x2L) != 0L)
        listener.actionPerformed(2, null, paramEntry); 
      return null;
    } 
    if (listener != null && (mask & 0x2L) != 0L)
      listener.actionPerformed(2, metaEntry, paramEntry); 
    Logger.getInstance().write("READ " + metaEntry.getEntry().getClass().getName() + " spid " + metaEntry.getId() + " size " + this.tuples.size() + " waiting " + this.threadsWaiting);
    return metaEntry.getEntry();
  }
  
  public Entry readIfExists(Entry paramEntry, Transaction paramTransaction, long paramLong, InetAddress paramInetAddress) throws RemoteException {
    if (paramEntry == null) {
      Logger.getInstance().write("READIF got null template not searchable.");
      return null;
    } 
    MetaEntry metaEntry = readEntry(paramEntry, 0L, false);
    if (metaEntry == null) {
      Logger.getInstance().write("READIF tmpl " + paramEntry.getClass().getName() + " no match waiting " + this.threadsWaiting);
      if (listener != null && (mask & 0x2L) != 0L)
        listener.actionPerformed(2, null, paramEntry); 
      return null;
    } 
    if (listener != null && (mask & 0x2L) != 0L)
      listener.actionPerformed(2, metaEntry, paramEntry); 
    Logger.getInstance().write("READIF " + metaEntry.getEntry().getClass().getName() + " spid " + metaEntry.getId() + " size " + this.tuples.size() + " waiting " + this.threadsWaiting);
    return metaEntry.getEntry();
  }
  
  public void notify(Entry paramEntry, Transaction paramTransaction, RemoteEventListener paramRemoteEventListener, long paramLong, MarshalledObject paramMarshalledObject, InetAddress paramInetAddress) throws RemoteException {
    Logger.getInstance().write("NOTIFY ignored.");
  }
  
  public Entry snapshot(Entry paramEntry, InetAddress paramInetAddress) throws RemoteException {
    return paramEntry;
  }
  
  public Integer register(String paramString, RemoteEventListener paramRemoteEventListener) throws RemoteException {
    if (paramRemoteEventListener == null) {
      Logger.getInstance().write("REGISTER received null proxy.");
      return null;
    } 
    int i = this.notifier.register(paramString, paramRemoteEventListener);
    return (i == -1) ? null : new Integer(i);
  }
  
  public Integer pin(Integer paramInteger, Entry paramEntry, InetAddress paramInetAddress) throws RemoteException {
    if (paramInteger == null) {
      Logger.getInstance().write("PIN received null rid.");
      return null;
    } 
    if (paramEntry == null) {
      Logger.getInstance().write("PIN received null template for rid = " + paramInteger);
      return null;
    } 
    int i = this.notifier.pin(paramInteger, paramEntry);
    if (i == -1)
      return null; 
    if (listener != null && (mask & 0x8L) != 0L)
      listener.actionPerformed(8, null, paramEntry); 
    return new Integer(i);
  }
  
  public static NucleusListener getListener() {
    return listener;
  }
  
  public static long getMask() {
    return mask;
  }
  
  private MetaEntry insertEntry(Entry paramEntry, long paramLong, InetAddress paramInetAddress) {
    MetaEntry metaEntry = new MetaEntry(paramEntry, null, paramLong, paramInetAddress);
    synchronized (this.tuples) {
      String str;
      this.tuples.addLast(metaEntry);
      this.tuples.notifyAll();
      if (paramLong == Long.MAX_VALUE) {
        str = "forever";
      } else {
        str = formatter.format(new Date(metaEntry.getExpiration()));
      } 
      if (listener != null && (mask & 0x1L) != 0L)
        listener.actionPerformed(1, metaEntry, null); 
      Logger.getInstance().write("WRITE " + metaEntry.getEntry().getClass().getName() + " spid " + metaEntry.getId() + " size " + this.tuples.size() + " waiting " + this.threadsWaiting + " until " + str);
    } 
    this.notifier.testEntry(paramEntry);
    return metaEntry;
  }
  
  private boolean isdumper(Entry paramEntry) {
    return paramEntry.getClass().getName().equals("space.util.DumperEntry");
  }
  
  private MetaEntry readEntry(Entry paramEntry, long paramLong, boolean paramBoolean) {
    MetaEntry metaEntry = null;
    synchronized (this.tuples) {
      ListIterator<MetaEntry> listIterator = searchEntries(paramEntry);
      if (listIterator != null) {
        metaEntry = listIterator.previous();
        if (paramBoolean)
          listIterator.remove(); 
      } else if (paramLong > 0L) {
        metaEntry = waitEntry(paramEntry, paramLong, paramBoolean);
      } 
      this.tuples.notifyAll();
    } 
    return metaEntry;
  }
  
  private MetaEntry waitEntry(Entry paramEntry, long paramLong, boolean paramBoolean) {
    if (paramLong <= 0L)
      return null; 
    long l = System.currentTimeMillis() + paramLong;
    String str = "forever";
    if (paramLong != Long.MAX_VALUE)
      str = formatter.format(new Date(l)); 
    Logger.getInstance().write("WAIT template " + paramEntry.getClass().getName() + " threads " + (this.threadsWaiting + 1) + " until " + str);
    while (true) {
      try {
        this.threadsWaiting++;
        if (listener != null && (mask & 0x20L) != 0L)
          listener.actionPerformed(32, null, paramEntry); 
        this.tuples.wait(paramLong);
        this.threadsWaiting--;
        if (listener != null && (mask & 0x40L) != 0L)
          listener.actionPerformed(64, null, paramEntry); 
      } catch (InterruptedException interruptedException) {
        return null;
      } catch (Exception exception) {
        System.err.println(exception);
      } 
      ListIterator<MetaEntry> listIterator = searchEntries(paramEntry);
      if (listIterator != null) {
        MetaEntry metaEntry = listIterator.previous();
        if (paramBoolean)
          listIterator.remove(); 
        return metaEntry;
      } 
      paramLong = l - System.currentTimeMillis();
      if (paramLong <= 0L)
        return null; 
    } 
  }
  
  private ListIterator searchEntries(Entry paramEntry) {
    long l = System.currentTimeMillis();
    ListIterator<MetaEntry> listIterator = this.tuples.listIterator(0);
    while (listIterator.hasNext()) {
      MetaEntry metaEntry = listIterator.next();
      if (metaEntry.isevictable(l))
        continue; 
      Entry entry = metaEntry.getEntry();
      if (entriesMatch(entry, paramEntry))
        return listIterator; 
    } 
    return null;
  }
  
  public static boolean entriesMatch(Entry paramEntry1, Entry paramEntry2) {
    return classesMatch(paramEntry1, paramEntry2) ? entryFieldsMatch(paramEntry1, paramEntry2) : false;
  }
  
  private static boolean classesMatch(Entry paramEntry1, Entry paramEntry2) {
    Class<?> clazz1 = paramEntry1.getClass();
    Class<?> clazz2 = paramEntry2.getClass();
    return (clazz1.getName().equals(clazz2.getName()) || clazz2.isInstance(paramEntry1));
  }
  
  private static boolean entryFieldsMatch(Object paramObject1, Object paramObject2) {
    try {
      Field[] arrayOfField = paramObject2.getClass().getFields();
      for (byte b = 0; b < arrayOfField.length; b++) {
        String str = arrayOfField[b].getName();
        try {
          Object object = arrayOfField[b].get(paramObject2);
          if (object != null && !arrayOfField[b].getType().isPrimitive()) {
            Field field = paramObject1.getClass().getField(str);
            Object object1 = field.get(paramObject1);
            if (!object.equals(object1))
              return false; 
          } 
        } catch (IllegalAccessException illegalAccessException) {}
      } 
      return true;
    } catch (Exception exception) {
      System.err.println(exception);
      exception.printStackTrace();
      return false;
    } 
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\nucleus\Nucleus.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */