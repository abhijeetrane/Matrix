//Team: Tahiti
//Date: 2005/04/27
//Vers: 1.0
//File: UpdateListener.java

package vista.ui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import vista.object.Cell;
import vista.object.*;

/** This class listens to the update combo box. 
 @author Tahiti
*/
public class UpdateListener implements ActionListener {

private ItemListener mp;
/** Constructor.
 @param mp Map panel. */
public UpdateListener(MapPanel mp ) {
  mp.setListener(this);
}

public void actionPerformed(ActionEvent e) {

 if(e.getSource()== MapPanel.updatebox)
	if(MapPanel.updatebox.getSelectedItem()=="Frequency"){
	    MapPanel.BY_FRE_AGE_MRU=0;
	    
	}
	 else if (MapPanel.updatebox.getSelectedItem()=="Age"){
	    MapPanel.BY_FRE_AGE_MRU=1;
	    
	    
		}
	if(MapPanel.updatebox.getSelectedItem()=="MRU"){
	    MapPanel.BY_FRE_AGE_MRU=2;
	    
	
 }
 
}

}