//Team: Tahiti
//Date: 2005/05/27
//Vers: 1.0
//File: ViewListener.java

package vista.ui;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import vista.object.Cell;
import vista.object.*;

/** This class listens to the view combo box 
 @author Tahiti
*/

public class ViewListener implements ActionListener {

private ItemListener mp;
/** Constructor.
@param mp Map panel. */
public ViewListener(MapPanel mp ) {
    mp.setListener(this);


}


public void actionPerformed(ActionEvent e) {
	
  if(e.getSource()== MapPanel.viewbox)
	if(MapPanel.viewbox.getSelectedItem()=="Clids"){
	    MapPanel.BY_CLID_SPID=0;
	   
	}
	 else if (MapPanel.viewbox.getSelectedItem()=="Spids"){
	    MapPanel.BY_CLID_SPID=1;
	   
	    
		}
	
	 
}

}