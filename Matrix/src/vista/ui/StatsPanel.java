//Team: Tahiti 
//Date: 2005/03/27
//Vers: 1.0
//File: StatsPanel.java

package vista.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import vista.object.StatsData;

/** This class contains a scrollable table of parameters and value.

 @author Tahiti.
*/
public class StatsPanel extends GriddedPanel {
/** Set to false to suppress debug activity. */
private final static boolean DEBUG = true;

/** Table model that guides display of table data.  */
private StatsTableModel model;

/** Panel width */
private final static int WIDTH = 375;

/** Panel height */
private final static int HEIGHT = 125;

/** Constructor  */
public StatsPanel() {
 // Set layout to position table at top of panel
 setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

 // Create the table model and add to table container
 model = new StatsTableModel();

 JTable table = new JTable();

 table.setAutoCreateColumnsFromModel(false);
 table.setModel(model);

 // Creates each TableColumn with specified alignment/width
 for (int k = 0; k < StatsTableModel.columns.length; k++) {
   DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

   renderer.setHorizontalAlignment(StatsTableModel.columns[k].alignment);

   TableColumn column = new TableColumn(k,StatsTableModel.columns[k].width,renderer, null);
   table.addColumn(column);
 }

 addComponent(table,0,0);

 // Finally, set the size (determined by trial-error)
 setPreferredSize(new Dimension(WIDTH,HEIGHT));
}

/** Updates the table data.
   @param statsData */
public void update(Vector statsData) {
 model.update(statsData);
}
}

/** This class is a table model which JTable to manage the table. */
class StatsTableModel extends AbstractTableModel
{
static final public ColumnData columns[] = {
 new ColumnData( "Param", 5, JLabel.RIGHT ),
 new ColumnData( "Value",25, JLabel.CENTER ),
};

/** Holds the table contents, per row.  */
private Vector vector  = new Vector();

/** Constructor. */
public StatsTableModel() {

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

 StatsData row = (StatsData) vector.elementAt(rowIndex);
 
 switch (colIndex) {
   case 0: return row.getParam();
   case 1: return row.getValue();
   }
 return "";
}
}