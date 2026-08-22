//Team: Ron Coleman
//Date: 2005/03/09
//Vers: 1.0
//File: MapPanel.java

package vista.ui;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.awt.Dimension;
import vista.object.EntityLocator;
import vista.object.Cell;
import vista.object.Heat;
import vista.tuples.UpdateMethod;
import vista.tuples.Entities;
import vista.test.*;
/** Contains a matrix object to render entries in space.

@author Ron Coleman, Ph.D.
*/
public class MapPanel extends GriddedPanel  implements ItemListener{
/** Set to false to suppress debug activity. */
private final static boolean DEBUG = true;

private static String[] updateByStrings = { "Frequency", "Age", "MRU" };
private static String[] updateByStrings1 = { "Clids", "Spids" };

public static JCheckBox freezebox = new JCheckBox();
public static JCheckBox organizebox = new JCheckBox();
public static JComboBox updatebox ;

public static JComboBox viewbox ;


public static boolean freezeflag = false;

public static int BY_FRE_AGE_MRU=0;


public static int BY_CLID_SPID=0;


public static int BY_INTENSITY=0;



/** Create matrix of cells */
private EntityMatrixPanel emp = new EntityMatrixPanel( );

/** Trans-listener called first if user clicks cell */
private MapListener transListener = null;
private ItemListener itemslistener =null;
private UpdateListener updatelistener=null;
private ViewListener viewlistener=null;

/** Update-by combo strings */



/** Update-by combo strings */
private String[] updateByIds = { "Clids", "Spids" };

private static final String GriddedPane = null;

/** Constructor. */
public MapPanel( ) {
super( );

// Add the legend
addAnchoredComponent(new LegendPanel( ),0,0,GriddedPanel.C_WEST);

// Add the space map
addAnchoredComponent(emp,1,0,GriddedPanel.C_WEST);

// Add the "Freeze map" checkbox
// NOTE: You'll also add the "Organize map by ..." to this panel, too.
GriddedPanel freezeOrganizePanel = new GriddedPanel( );

freezebox.setSelected(false);


freezeOrganizePanel.addComponent(freezebox,0,0);

freezebox.addItemListener(new ItemChangeListener(this) );

freezeOrganizePanel.addComponent(new JLabel("Freeze map"),0,1);
organizebox.setSelected(false);

freezeOrganizePanel.addComponent(organizebox,0,3);
organizebox.addItemListener(new OrganizeListener(this));


freezeOrganizePanel.addComponent(new JLabel("Organize map by intensity"),0,4);


addAnchoredComponent(freezeOrganizePanel,2,0,GriddedPanel.C_WEST);


// Add the "Update-by" combo box
// NOTE: You'll also the "View by" combo box to this panel, too.
GriddedPanel updateViewPanel = new GriddedPanel( );

updateViewPanel.addComponent(new JLabel("Update by"),0,0);

updatebox = new JComboBox(updateByStrings);

updateViewPanel.addComponent(updatebox,0,1);

updatebox.addActionListener(new UpdateListener(this) );

updateViewPanel.addComponent(new JLabel("View by"),0,3);

viewbox=new JComboBox(updateByIds);
updateViewPanel.addComponent(viewbox,0,4);
viewbox.addActionListener(new ViewListener(this) );

addAnchoredComponent(updateViewPanel,3,0,GriddedPanel.C_WEST);


}

/** Updates the entity cell matrix with new information.
 @param method Update method. */
public void update(UpdateMethod method) {
	System.out.println("MapPanel update Method Begin");
    emp.update(method);
    System.out.println("MapPanel update Method End");
}

/** Sets map listener to be invoked if user clicks a cell.
 @param listener Invoked if user clicks cell. */
public void setListener(MapListener listener) {
emp.setListener(listener);
}
public void setListener(ItemListener listener) {
	this.itemslistener=listener;
	}

public void setListener(UpdateListener listener) {
	this.updatelistener=listener;
	}

public void setListener(ViewListener listener) {
	this.viewlistener=listener;
	}
/* (non-Javadoc)
 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
 */
public void itemStateChanged(ItemEvent e) {
	if(itemslistener != null) {
		itemslistener.itemStateChanged(e);
}

	// TODO Auto-generated method stub
	
}

}

