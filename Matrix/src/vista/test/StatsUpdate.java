//Team: Tahiti
//Date: 2005/03/27
//Vers: 1.0
//File: StatsUpdate.java

package vista.test;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;
import vista.ui.StatsPanel;
import vista.object.StatsData;
import vista.ui.*;
import vista.tuples.*;
import vista.object.*;
/** This class tests the stats table update.

 @author Tahiti.
*/
public class StatsUpdate implements Runnable {
/** Reference to StatsPanel */
private StatsPanel stp;
private Entities entities;
private SimpleDateFormat formatter = new SimpleDateFormat ("hh:mm:ss a");
/**Constructor
 *
 * @param stp
 */
public StatsUpdate(StatsPanel stp,Entities entities) {
 this.stp = stp;
 this.entities=entities;
 }

/** Invoked by Thread.start to start the thread */
public void run( ) {
 try{
 Vector vector = new Vector( );
 while(true){
 // Create some dummy data
 // (More interesting if in loop but...next time.)
 vector.addElement(new StatsData("Entries",new Integer(entities.getSize()).toString()));
 vector.addElement(new StatsData("Threads",new Integer(VistaUI.spaceSleeping).toString()));
 vector.addElement(new StatsData("Listeners",new Integer(VistaUI.spaceRegisters).toString()));
 vector.addElement(new StatsData("Writes",new Integer(VistaUI.spaceWrites).toString()));
 vector.addElement(new StatsData("Reads",new Integer(VistaUI.spaceReads).toString()));
 vector.addElement(new StatsData("Takes",new Integer(VistaUI.spaceTakes).toString()));
 vector.addElement(new StatsData("Notifies",new Integer(VistaUI.spaceNotifies).toString()));
 long uptimedate = System.currentTimeMillis()-VistaUI.spaceStart;
 LastUpdate uptime =new LastUpdate();
 uptime.calculate(uptimedate);
 
  vector.addElement(new StatsData("Uptime",uptime.hours+":"+uptime.minutes+":"+uptime.seconds));
 long tempdate =System.currentTimeMillis() - VistaUI.lastupdate;
 
 LastUpdate lastupdate =new LastUpdate();
 lastupdate.calculate(tempdate);
 if(VistaUI.lastupdate==0)
 	vector.addElement(new StatsData("Last update","0"+":"+"0"+":"+"0"));
 else
 vector.addElement(new StatsData("Last update","-"+lastupdate.hours+":"+lastupdate.minutes+":"+lastupdate.seconds));

 // Send in new spids information
 stp.update(vector);
 Thread.sleep(1000);
 vector.removeAllElements();
}
}catch(Exception e){
	e.printStackTrace( );
    }
}
}