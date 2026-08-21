package space.nucleus;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import space.Entry;

public class Logger {
  private static Logger logger;
  
  private FileWriter fw = null;
  
  private static String LOGFILE = "space.log";
  
  private static SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
  
  public static Logger getInstance() {
    return getInstance(LOGFILE);
  }
  
  public static Logger getInstance(String paramString) {
    if (logger != null)
      return logger; 
    logger = new Logger(paramString);
    return logger;
  }
  
  private Logger(String paramString) {
    try {
      this.fw = new FileWriter(LOGFILE, true);
      this.fw.write("\n---\n");
    } catch (IOException iOException) {
      System.err.println("Logger: unable to open " + paramString + " for append: " + iOException);
    } 
  }
  
  public void write(String paramString) {
    if (this.fw == null)
      return; 
    Date date = new Date();
    String str = formatter.format(date);
    try {
      this.fw.write(str + " " + paramString + "\n");
      this.fw.flush();
    } catch (IOException iOException) {}
  }
  
  public void dump(LinkedList paramLinkedList) {
    if (this.fw == null)
      return; 
    synchronized (paramLinkedList) {
      Date date = new Date();
      String str = formatter.format(date);
      try {
        this.fw.write(str + " Space dump:" + "\n");
        int i = paramLinkedList.size();
        this.fw.write("Space sz = " + i + "\n");
        ListIterator<MetaEntry> listIterator = paramLinkedList.listIterator(0);
        for (byte b = 0; listIterator.hasNext(); b++) {
          String str1;
          MetaEntry metaEntry = listIterator.next();
          if (metaEntry.getExpiration() == Long.MAX_VALUE) {
            str1 = "never";
          } else {
            str1 = formatter.format(new Date(metaEntry.getExpiration()));
          } 
          Entry entry = metaEntry.getEntry();
          this.fw.write("[" + b + "] " + entry.getClass().getName() + " spid = " + metaEntry.getId() + " expires " + str1 + "\n");
          Field[] arrayOfField = entry.getClass().getFields();
          for (byte b1 = 0; b1 < arrayOfField.length; b1++) {
            String str2 = arrayOfField[b1].getName();
            Object object = arrayOfField[b1].get(entry);
            this.fw.write("  " + str2 + " = " + object);
            if (arrayOfField[b1].getType().isPrimitive())
              this.fw.write(" (primitive)"); 
            this.fw.write("\n");
          } 
        } 
        this.fw.flush();
      } catch (IOException iOException) {
      
      } catch (Exception exception) {
        System.err.println("Logger.dump: " + exception);
      } 
    } 
  }
  
  public static String getVersion(String paramString) {
    int i = paramString.indexOf(" ");
    int j = paramString.lastIndexOf("$");
    if (i == -1 || j == 1)
      return ""; 
    try {
      String str = paramString.substring(i + 1, j - 1);
      int k = Integer.parseInt(str);
      return "v1." + k + " ";
    } catch (Exception exception) {
      return "";
    } 
  }
}


/* Location:              E:\masters_projects\Tahiti\src\space.jar!\space\nucleus\Logger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */