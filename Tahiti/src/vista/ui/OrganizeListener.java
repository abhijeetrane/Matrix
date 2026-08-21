//Team: Tahiti
//Date: 2005/04/27
//Vers: 1.0
//File: OrganizeListener.java
package vista.ui;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import vista.object.Cell;
import vista.object.*;

/** This class organizes the map according to intensity.

 @author Tahiti
*/
public class OrganizeListener implements ItemListener {

private ItemListener mp;
/** Constructor.
 @param mp Map panel. */
public OrganizeListener(MapPanel mp ) { 
  mp.setListener(this);

}


public void itemStateChanged(ItemEvent e) {

 if(e.getSource()== MapPanel.organizebox){
	if(e.getStateChange()==ItemEvent.SELECTED){
	    MapPanel.BY_INTENSITY =1;
	    
	}
	if(e.getStateChange()==ItemEvent.DESELECTED){
		MapPanel.BY_INTENSITY =0;
		
			
	}
 }
}

}