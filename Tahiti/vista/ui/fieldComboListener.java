//Team: Tahiti
//Date: 2005/05/27
//Vers: 1.0
//File: fieldComboListener.java

package vista.ui;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import vista.object.Cell;
import vista.object.*;
import java.lang.reflect.Field;

/** This class listens to combo box and changes the field values accordingly.

@author Tahiti
*/
public class fieldComboListener implements ActionListener {

private InspectDialog id;
/** Constructor.
@param id InspectDialog. */
public fieldComboListener( InspectDialog id) {
    this.id = id;
}
public void actionPerformed(ActionEvent e) {

	if(e.getSource()== id.fieldCombo){
	 int indexfield=id.updateByStrings.indexOf(id.fieldCombo.getSelectedItem());
	 id.classlabel.setText(id.classvalue.elementAt(indexfield).toString());
	 id.changevalue.setText(id.fieldvalue.elementAt(indexfield).toString());
	}
	 
	}
}