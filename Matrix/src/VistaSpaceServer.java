import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import space.Entry;
import space.nucleus.MetaEntry;
import space.nucleus.NucleusListener;
import space.remote.VistaSpace;

public class VistaSpaceServer implements NucleusListener {
  public VistaSpace vistaSpace = null;
  
  public static final Integer REGISTRY_PORT = new Integer(1099);
  
  public static final String SERVER_LOCATION = "rmi://localhost/space";
  
  private SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
  
  private int spaceWrites = 0;
  
  private int spaceReads = 0;
  
  private int spaceTakes = 0;
  
  private int spaceSleeping = 0;
  
  private int spaceRegisters = 0;
  
  private int spaceNotifies = 0;
  
  private int spaceExpires = 0;
  
  private int clidIndex = 0;
  
  private Hashtable clidtab = new Hashtable<Object, Object>();
  
  String[] actionStrings = new String[] { "BOGY", "WRITE", "READ", "TAKE", "REGISTER", "NOTIFY", "SLEEP", "WAKEUP", "EXPIRE" };
  
  public static void main(String[] paramArrayOfString) {
	System.setProperty("java.net.preferIPv4Stack" , "true");  
    String str;
    if (paramArrayOfString.length == 0) {
      str = "rmi://localhost/space";
    } else {
      str = paramArrayOfString[0];
    } 
    try {
      VistaSpace vistaSpace = new VistaSpace(str, REGISTRY_PORT);
      VistaSpaceServer vistaSpaceServer = new VistaSpaceServer(vistaSpace);
    } catch (Exception exception) {
      System.err.println(exception);
      System.exit(1);
    } 
  }
  
  public VistaSpaceServer(VistaSpace paramVistaSpace) {
    VistaSpace.setListener(this);
  }
  
  public void actionPerformed(int paramInt, MetaEntry paramMetaEntry, Entry paramEntry) {
    System.out.println("Action: " + this.actionStrings[getActionNumber(paramInt)]);
    switch (paramInt) {
      case 1:
        this.spaceWrites++;
        break;
      case 2:
        this.spaceReads++;
        break;
      case 4:
        this.spaceTakes++;
        break;
      case 32:
        this.spaceSleeping++;
        break;
      case 64:
        this.spaceSleeping--;
        break;
      case 8:
        this.spaceRegisters++;
        break;
      case 16:
        this.spaceNotifies++;
        break;
      case 128:
        this.spaceExpires++;
        break;
    } 
    print(paramMetaEntry, paramEntry);
  }
  
  private void print(MetaEntry paramMetaEntry, Entry paramEntry) {
    System.out.println("Number writes: " + this.spaceWrites);
    System.out.println("Number reads: " + this.spaceReads);
    System.out.println("Number takes: " + this.spaceTakes);
    System.out.println("Sleeping threads: " + this.spaceSleeping);
    System.out.println("Notify registers: " + this.spaceRegisters);
    System.out.println("Notifies: " + this.spaceNotifies);
    if (paramMetaEntry != null) {
      Entry entry = paramMetaEntry.getEntry();
      System.out.println("Spid: " + paramMetaEntry.getId());
      System.out.println("Class: " + entry.getClass().getName());
      System.out.println("Clid: " + getClid(entry.getClass().getName()));
      System.out.println("Owner: " + paramMetaEntry.getOwner().toString());
      long l = paramMetaEntry.getExpiration();
      System.out.print("Expiry: ");
      if (l == Long.MAX_VALUE) {
        System.out.println("never");
      } else {
        System.out.println(this.formatter.format(new Date(l)));
      } 
    } 
    if (paramEntry != null)
      System.out.println("Read/take template: " + paramEntry.getClass().getName()); 
    System.out.println("---");
  }
  
  private Integer getClid(String paramString) {
    Integer integer = (Integer)this.clidtab.get(paramString);
    if (integer == null) {
      this.clidIndex++;
      integer = new Integer(this.clidIndex);
      this.clidtab.put(paramString, integer);
    } 
    return integer;
  }
  
  private int getActionNumber(long paramLong) {
    for (byte b = 1; b <= 32; b++) {
      if ((paramLong & 0x1L) != 0L)
        return b; 
      paramLong >>= 1L;
    } 
    return 0;
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\VistaSpaceServer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */