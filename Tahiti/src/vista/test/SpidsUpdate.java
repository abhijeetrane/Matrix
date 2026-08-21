// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: SpidsUpdate.java

package vista.test;

import java.util.Vector;
import vista.ui.SpidsPanel;
import vista.object.SpidData;
import vista.object.*;
import vista.tuples.*;
import vista.ui.*;

/** This class tests the spids table update.

    @author Ron Coleman, Ph.D.
*/

public class SpidsUpdate implements Runnable {
  /** Reference to SpidsPanel */
  private SpidsPanel sp;
  private Entities entities;
  /** Constructor.
      @param sp SpidsPanel. */
  public SpidsUpdate(SpidsPanel sp,Entities entities) {
    this.sp = sp;
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
	vector.addElement(new SpidData(new Long(tempentity.getId()),VistaUI.getClid(tempentity.getEntry()),tempentity.getEntry().getClass().getName()));

}

    // Send in new spids information
    sp.update(vector);
    Thread.sleep(6000);
	 vector.removeAllElements();
}
	}catch(Exception e){
		e.printStackTrace( );
    }
  }
}