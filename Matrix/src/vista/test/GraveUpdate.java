//Team: Tahiti
//Date: 2005/04/05
//Vers: 1.0
//File: GraveUpdate.java

package vista.test;

import java.util.Vector;
import vista.ui.GravePanel;
import vista.object.GraveData;
import vista.object.*;
import vista.tuples.*;
import vista.ui.*;
import java.util.Date;
import java.text.SimpleDateFormat;
/** This class tests the graveyard table update.

 @author Tahiti.
*/
public class GraveUpdate implements Runnable {
/** Reference to GravePanel */
private GravePanel gp;
private Entities entities;
private SimpleDateFormat formatter = new SimpleDateFormat ("hh:mm:ss a");
/** Constructor.
   @param gp GravePanel. */
public GraveUpdate(GravePanel gp,Entities entities) {
 this.gp = gp;
 this.entities=entities;
}

/** Invoked by Thread.start to start the thread */
public void run( ) {
 
  
 
 try{
 	Vector vector = new Vector( );
    while (true){
    	if(entities == null)
    		System.out.println(" I am null in grave update");
    	
     Iterator iter = entities.getIterator();
     if(iter == null)
     	System.out.println("Iterator is null");
     int i=0;
     
     
     for( i= 0;i<entities.getSize();i++){
     
 		Entity tempentity = (Entity)iter.next();
 		
 			
 		try{
 		
 			if(tempentity.isdisposed())
 			vector.addElement(new GraveData(new Long(tempentity.getId()),VistaUI.getClid(tempentity.getEntry()),tempentity.getEntry().getClass().getName(),formatter.format(new Date(tempentity.getDeath())).toString(),"Deleted"));
 			else if(tempentity.isremoved())
 				vector.addElement(new GraveData(new Long(tempentity.getId()),VistaUI.getClid(tempentity.getEntry()),tempentity.getEntry().getClass().getName(),formatter.format(new Date(tempentity.getDeath())).toString(),(tempentity.getTaker()).toString()));	
 			else if(tempentity.isexpired())
 				vector.addElement(new GraveData(new Long(tempentity.getId()),VistaUI.getClid(tempentity.getEntry()),tempentity.getEntry().getClass().getName(),formatter.format(new Date(tempentity.getDeath())).toString(),"Expired"));
 		}catch(Exception  e){
 			e.printStackTrace();
 			//System.err.println(e);
 			System.out.println("Error in vector of grave update");
 		}
 	
 	
}
    	    
 // Send in new dead spids information
 gp.update(vector);
 Thread.sleep(6000);
 	 vector.removeAllElements();
 }
 	}catch(Exception e){
 		System.out.println("In grave update");
 		e.printStackTrace( );
    }
}
}