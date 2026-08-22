//Team: Tahiti
//Date: 2005/03/26
//Vers: 1.0
//File: ClidsUpdate.java

package vista.test;

import java.util.Vector;
import vista.ui.ClidsPanel;
import vista.object.ClidData;
import vista.object.*;
import vista.tuples.*;
import vista.ui.*;

/** This class tests the clids table update.

 @author Tahiti.
*/
public class ClidsUpdate implements Runnable {
/** Reference to ClidsPanel */
private ClidsPanel cp;
private Entities entities;

/** Constructor.
   @param cp ClidsPanel. */
public ClidsUpdate(ClidsPanel cp,Entities entities) {
 this.cp = cp;
 this.entities=entities;
}

/** Invoked by Thread.start to start the thread */
public void run( ) {
 Vector vector = new Vector( );
 try{
    while (true){

     Iterator iter = entities.getIterator();
     int i=0;
     for( i= 0;i<entities.getSize();i++){
    	Entity tempentity = (Entity)iter.next();
    	vector.addElement(new ClidData(VistaUI.getClid(tempentity.getEntry()),tempentity.getEntry().getClass().getName()));

}
 // Send in new clids information
 cp.update(vector);
  Thread.sleep(6000);
 	 vector.removeAllElements();
 }
 	}catch(Exception e){
 		e.printStackTrace( );
    }
}
}