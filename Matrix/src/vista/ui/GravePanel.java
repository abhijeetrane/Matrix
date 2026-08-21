//Team: Tahiti
//Date: 2005/03/26
//Vers: 1.0
//File: GravePanel.java

package vista.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import vista.object.Cell;
import vista.object.GraveData;
import vista.tuples.*;
import vista.object.*;
/** This class contains a scrollable table of spids, clids,entry class names,time and taken by.

@author Tahiti.
*/
public class GravePanel extends GriddedPanel {
/** Set to false to suppress debug activity. */
private final static boolean DEBUG = true;

/** Table model that guides display of table data.  */
private GraveTableModel model;

/** Panel width */
private final static int WIDTH = 375;

/** Panel height */
private final static int HEIGHT = 125;

/** Constructor  */
public GravePanel() {
GriddedPanel mainPanel =new GriddedPanel();

// Set layout to position table at top of panel
setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

// Create the table model and add to table container
model = new GraveTableModel();

JTable table = new JTable();

table.setAutoCreateColumnsFromModel(false);
table.setModel(model);

// Creates each TableColumn with specified alignment/width
for (int k = 0; k < GraveTableModel.columns.length; k++) {
 DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

 renderer.setHorizontalAlignment(GraveTableModel.columns[k].alignment);

 TableColumn column = new TableColumn(k,GraveTableModel.columns[k].width,renderer, null);

 table.addColumn(column);
}

// Putting table on JScrollPane makes table scrollable; skipping this
// step means the table will be cut-off if it's too big.

JScrollPane scroll =new JScrollPane(table);
scroll.setPreferredSize(new Dimension(300,300));

mainPanel.addComponent(scroll,2,0);

addAnchoredComponent(mainPanel,0,0,GriddedPanel.C_WEST);

GriddedPanel deadIdPanel = new GriddedPanel( );

deadIdPanel.addComponent(new JLabel("Dead Spid"),0,0);

JTextField deadspid = new JTextField("",5);

deadIdPanel.addComponent(deadspid,0,2);

JButton inspectButton = new JButton("Inspect");

inspectButton.addActionListener(new InspectButtonListener(this,deadspid));


deadIdPanel.addComponent(inspectButton,0,4);

addAnchoredComponent(deadIdPanel,1,0,GriddedPanel.C_WEST);

// Finally, set the size (determined by trial-error)

setPreferredSize(new Dimension(WIDTH,HEIGHT));
}

/** Updates the table data.
 @param graveData Table data. */
public void update(Vector graveData) {
 model.update(graveData);
}
}

/** This class is a table model which JTable to manage the table. */
class GraveTableModel extends AbstractTableModel
{
static final public ColumnData columns[] = {
new ColumnData( "Spid", 5, JLabel.RIGHT ),
new ColumnData( "Clid",5, JLabel.CENTER ),
new ColumnData( "Class name",25, JLabel.CENTER ),
new ColumnData( "Time",50, JLabel.CENTER ),
new ColumnData( "Taken by",25, JLabel.CENTER ),
};

/** Holds the table contents, per row.  */
private Vector vector  = new Vector();

/** Constructor. */
public GraveTableModel() {

}

/** Updates table and causes table to repaint contents.
 @param vector Rows of column data. */
public void update(Vector vector) {
this.vector = vector;
fireTableDataChanged();
}

/** Get row count (invoked by JTable).
 @return Number of rows in table. */
public int getRowCount() {
return vector == null ? 0 : vector.size();
}

/** Get column count (invoked by JTable).
 @return Table column count. */
public int getColumnCount() {
return columns.length;
}

/** Get column name (invoked by JTable).
 @param column Column number.
 @return Column name. */
public String getColumnName(int colIndex) {
return columns[colIndex].title;
}

/** Test if cell editable (invoked by JTable).
 @param rowIndex Row index of cell.
 @param colIndex Column index of cell.
 @return true if cell is editable, false otherwise. */
public boolean isCellEditable(int rowIndex, int colIndex) {
return false;
}

/** Get table object at row, column (used by JTable).
 @param rowIndex Row index of cell.
 @param colIndex Column index of cell.
 @return Object at that row, column */
public Object getValueAt(int rowIndex, int colIndex) {
if (rowIndex < 0 || rowIndex >= getRowCount())
 return "";

GraveData row = (GraveData) vector.elementAt(rowIndex);
switch (colIndex) {
 case 0: return row.getSpid();
 case 1: return row.getClid();
 case 2: return row.getClassName();
 case 3: return row.getTime();
 case 4: return row.getTakenby();
}
return "";
}
}
/** This class handles  clicks on the inspect button. */
class InspectButtonListener implements ActionListener {
  private JTextField deadspid;
  private GriddedPanel gp;
/**Constructor
*
* @param deadspid
*/
public InspectButtonListener(GriddedPanel gp,JTextField deadspid) {
  this.deadspid=deadspid;
  this.gp=gp;
}
/** Invoked on button click.
 @param e Action event. */
public void actionPerformed(ActionEvent e) {
	//System.out.println("The string is "+deadspid.getText());
	try{
	if(deadspid.getText().equals(""))
		JOptionPane.showMessageDialog(gp, "You have not entered a spid");
	else 
	{
	Entity localentity = VistaUI.getDeadEntities().lookup(new Long(deadspid.getText()).longValue());
    if(localentity == null)
	  	JOptionPane.showMessageDialog(gp, "You have entered an invalid spid");
	   else
			new InspectDialog(new JFrame(),localentity,null,true);
	}
	}catch(NumberFormatException ne){
		JOptionPane.showMessageDialog(gp, "You have entered an invalid spid");
	}
}
}