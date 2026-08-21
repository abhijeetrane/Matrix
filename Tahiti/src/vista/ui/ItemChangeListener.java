//Team: Tahiti
//Date: 2005/04/27
//Vers: 1.0
//File: ItemChangeListener.java

package vista.ui;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import vista.object.Cell;
import vista.object.*;

/** This class listens to checkbox to freeze or unfreeze the check box.

 @author Tahiti.
*/
public class ItemChangeListener implements ItemListener {

private ItemListener mp;
/** Constructor.
   @param mp Map panel. */
public ItemChangeListener(MapPanel mp ) {
   mp.setListener(this);

	


}


public void itemStateChanged(ItemEvent e) {

   if(e.getSource()== MapPanel.freezebox){
	if(e.getStateChange()==ItemEvent.SELECTED){
	    MapPanel.freezeflag =true;
	
	}
	if(e.getStateChange()==ItemEvent.DESELECTED){
		MapPanel.freezeflag =false;
			
	}
   }
}

}