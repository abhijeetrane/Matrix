//Team: Tahiti
//Date: 2005/05/27
//Vers: 1.0
//File: FileSave.java
package vista.util;

import java.lang.reflect.*;
import java.io.*;
import java.text.*;
import java.util.*;
import space.*;
import space.nucleus.*;


/** Writes log to a file.

    @author Tahiti.
*/
public class FileSave {
  private static FileSave filesave;

  // File writer reference
  private FileWriter fw = null;

  private static String LOGFILE = "vista.txt";

  // Generic date formatter
  static private SimpleDateFormat formatter = new SimpleDateFormat ("hh:mm:ss a");

  public static FileSave getInstance() {
    return getInstance(LOGFILE);
  }

  public static FileSave getInstance(String file) {
    if(filesave != null)
      return filesave;

    filesave = new FileSave(file);

    return filesave;
  }

  /** Constructor.
     @param file Name of file to write logs.
  */
  private FileSave(String file) {
    try {
      fw = new FileWriter(file,true);
      fw.write("\n---\n");
    }
    catch(java.io.IOException ioe) {
      System.err.println("FileSave: unable to open "+file+" for append: "+ioe);
    }
  }

  /** Write message to the log and time stamp it.
      @param msg Message to write
  */
  public void write(String msg) {
    if(fw == null)
      return;

    Date now = new Date();

    String datestr = formatter.format(now);

    try {
      fw.write(datestr+" "+msg+"\n");
      fw.flush();
    }
    catch(java.io.IOException ioe) {
      // File write must have failed...but don't complain
      // as it is just the logger
    }

  }

  
  
}