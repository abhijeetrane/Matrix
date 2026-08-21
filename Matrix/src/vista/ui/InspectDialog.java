//Team: Tahiti
//Date: 2005/04/01
//Vers: 1.0
//File: InspectDialog.java

package vista.ui;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import vista.object.Entity;
import vista.object.Cell;
import vista.object.InspectSpidData;
import vista.test.*;
import vista.tuples.Entities;
import java.lang.reflect.Field;
import space.*;
import space.util.*;

//import vista.test.star.*;

/** This class implements a pop-up dialog to do inspect or modify of entry.
@author Tahiti
*/
public class InspectDialog extends JDialog {

/** Table model that guides display of table data.  */
	private InspectTableModel model;
/** submit button */
	private JButton submitbutton;
/** value field */
	public JTextField changevalue;
	
	public JComboBox fieldCombo;
	public JLabel classlabel;
/** combo box field*/
	public  Vector updateByStrings = new Vector();

	public  Vector fieldvalue = new Vector();
	
	public Vector  classvalue = new Vector();
	
	public   int count =0;
    
	public Entity entity;
	public Entry copyentry;

/** Constructor.
   @param owner Owner JFrame.
   @param entity Entity to inspect or modify.  
*/

public InspectDialog(JFrame owner,Entity entity,Cell cell,boolean stopeditable) {
 super(owner,"Vista - Inspect / Modify",true);  // true == modal
 this.entity=entity;
 count=0;
 GriddedPanel topPanel = new GriddedPanel();

 topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

 GriddedPanel mainPanel = new GriddedPanel();

 // Create the table model and add to table container
 model = new InspectTableModel();

 JTable table = new JTable();

 table.setAutoCreateColumnsFromModel(false);
 table.setModel(model);

 // Creates each TableColumn with specified alignment/width
 for (int k = 0; k < InspectTableModel.columns.length; k++) {
   DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

   renderer.setHorizontalAlignment(InspectTableModel.columns[k].alignment);

   TableColumn column = new TableColumn(k,InspectTableModel.columns[k].width,renderer, null);
   table.addColumn(column);
 }

 // Putting table on JScrollPane makes table scrollable; skipping this
 // step means the table will be cut-off if it's too big.
 JScrollPane scroll = new JScrollPane(table);

 scroll.setPreferredSize(new Dimension(300,130));

 mainPanel.addComponent(scroll,0,0);

 topPanel.add(mainPanel);

 GriddedPanel lowerPanel = new GriddedPanel();

 lowerPanel.addComponent(new JLabel("Field"),0,0);
  inspect(entity.getEntry());
 
  fieldCombo =new JComboBox(updateByStrings);

 lowerPanel.addComponent(fieldCombo,0,1);
 
 fieldCombo.addActionListener(new fieldComboListener(this));
 
 
 lowerPanel.addComponent(new JLabel("Class"),1,0);

 classlabel =new JLabel(classvalue.elementAt(0).toString());
 //classlabel =new JLabel("a");
 lowerPanel.addComponent(classlabel,1,1);

 lowerPanel.addComponent(new JLabel("Value"),2,0);
 
	
 
 changevalue=new JTextField(fieldvalue.elementAt(0).toString(),20);
 //changevalue=new JTextField("A",20);
            
            
            
            
            
 lowerPanel.addComponent(changevalue,2,1);

 //mainPanel.addComponent(new JLabel(cell.toString( )+" clicked"),1,0);

 JButton okButton = new JButton("OK");

 okButton.addActionListener(new InpectOkButtonListener(this ));

 lowerPanel.addComponent(okButton,3,0);

 //mainPanel.addComponent(new JLabel(cell.toString( )+" clicked"),1,0);

  submitbutton = new JButton("Submit");

  submitbutton.addActionListener(new SubmitModifyListener(this ));

 lowerPanel.addComponent(submitbutton,3,1);

  topPanel.add(lowerPanel);

 getContentPane().add(topPanel);
 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 setSize(400,525);
 
 Vector vector = new Vector();
  SimpleDateFormat formatter = new SimpleDateFormat ("d MMM yy hh:mm:ss a");
 vector.addElement(new InspectSpidData("Class",entity.getEntry().getClass().getName()));
 vector.addElement(new InspectSpidData("Clid",(Entities.clidtab.get(entity.getEntry())).getId().toString()));
 vector.addElement(new InspectSpidData("Spid",new Long (entity.getId()).toString()));
 vector.addElement(new InspectSpidData("Reads",new Long(entity.getReads()).toString()));
 vector.addElement(new InspectSpidData("Owner",entity.getOwner().toString()));
 vector.addElement(new InspectSpidData("Written",formatter.format(new Date(entity.getBirth())).toString()));
 vector.addElement(new InspectSpidData("Expires",formatter.format(new Date(entity.getDeath())).toString()));
 model.update(vector);

if(stopeditable){
this.stopEditable();
}

 setVisible(true);
}
public void update(Vector InspectSpidData) {
    model.update(InspectSpidData);
  }
public void stopEditable(){
	submitbutton.setVisible(false);
    changevalue.setEditable(false);
}
//This is the start of inspect
/** Inspect an object.
@param object The object to inspect.
 */
public  void inspect(Object object) {
// Nothing to do if object is null
if(object == null)
return;

// Print the names and values of all class fields
printFields(object);


}
public  void inspectchange(Object object,String member,Object newValue) {
//	 Nothing to do if object is null
	if(object == null)
	return;
     
	  change(object, member,newValue);
//	 Print the names and values of all class fields
	//printFields(object);

//	 Print the names and values of all class fields
//	 printDeclaredFields(object,nest);

//	 Print the names and values of all class fields
	  //printFieldTypes(object);
	}
/** Print out the member fields and their values.
@param object The object to print.
 */
public  void printFields(Object object) {
//System.out.println(" Fields <<"+object.getClass().getName()+">>");
if(object ==null)
	System.out.println("Iam null object*********");
try{
Field[] fields = object.getClass().getFields();

try {
for(  int i=0;i < fields.length; i++) {
	if(object!=null){
  if(deepInspect(fields[i].get(object)))
  	  inspect(fields[i].get(object));
  	  else{
	updateByStrings.addElement(fields[i].getName());
	if(fields[i].get(object)== null)
  	fieldvalue.addElement("");
	else
	fieldvalue.addElement(fields[i].get(object));	
	
  	classvalue.addElement(fields[i].getType().getName());
	
  	  }
	   
}
}

  
  
}
catch(IllegalAccessException e) {
  System.out.println(  " not accessible");
  //continue;
}
}
catch(Exception e)
{
	System.out.println("I am null here2343434324");
}
}




/** Deep inspect any non-wrapper types.
@param object The object to print.
 */
public static boolean deepInspect(Object object) {
if(object == null)
return false;

if(object instanceof Integer ||
 object instanceof Double ||
 object instanceof Boolean ||
 object instanceof String)
   return false ;

//inspect(object);
return true;
}
//This is the end of inspect
/** Changes a field of an object.
@param object Object to change.
@param member Member name.
@param newValue New value for the member. */
public  void change(Object object,String member,Object newValue) {
Field[] fields = object.getClass().getFields();
for(  int i=0;i < fields.length; i++) {
	try{
  //if(deepInspect(fields[i].get(object)))
  	//  inspectchange(fields[i].get(object),member,newValue);
  	  //else{
 	
  	  String name = fields[i].getName();
  	  //System.out.println("This is name "+name);
  	if(name.equals(member)) {
  	  
  	    Object value = fields[i].get(object);

  	  //  System.out.println(member+" old value = "+value);

  	    fields[i].set(object,newValue);

  	    value = fields[i].get(object);

  	    //System.out.println(member+" new value = "+value);

  	    return;
  	  }else
  	  	if(deepInspect(fields[i].get(object)))
  	  	inspectchange(fields[i].get(object),member,newValue);
  	  	//else
  	      //System.out.println("couldn't find member "+member);	  
  	  }
	
  	  catch(IllegalAccessException e) {
  	  System.out.println(" not accessible");
  	  }
  	  
  	
  	  }
 }
 


	



}//end of InspectDialog
/** This class is a table model which JTable to manage the table. */
class InspectTableModel extends AbstractTableModel
{
  static final public ColumnData columns[] = {
    new ColumnData( "Attribute", 5, JLabel.RIGHT ),
    new ColumnData( "Value",25, JLabel.CENTER ),
   };

  /** Holds the table contents, per row.  */
  private Vector vector  = new Vector();

  /** Constructor. */
  public InspectTableModel() {

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

    InspectSpidData row = (InspectSpidData) vector.elementAt(rowIndex);
    switch (colIndex) {
      case 0: return row.getAttribute();
      case 1: return row.getValue();
   }
    return "";
  }
}
/** This class handles OK clicks on the inspect dialog. */
class InpectOkButtonListener implements ActionListener {
/** Dialog pop-up (we'll dispose) */
private JDialog dialog;

/** Constructor.
   @param dialog Dialog pop-up. */
public InpectOkButtonListener(JDialog dialog) {
 this.dialog = dialog;
}

/** Invoked on button click.
   @param e Action event. */
public void actionPerformed(ActionEvent e) {
	SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          dialog.dispose();
        }
    });
}
}
class SubmitModifyListener implements  ActionListener{
	/** Dialog pop-up (we'll dispose) */
	private InspectDialog dialog;

	/** Constructor.
	   @param dialog Dialog pop-up. */
	public SubmitModifyListener(InspectDialog dialog) {
	 this.dialog = dialog;
	}

	/** Invoked on button click.
	   @param e Action event. */
	public void actionPerformed(ActionEvent e) {
		//dialog.copyentry= (space.Entry)space.util.DeepCopy.copy(dialog.entity.getEntry());
		//dialog.copyentry=dialog.entity.getEntryCopy();
		 dialog.copyentry=dialog.entity.getEntry();
		//System.out.println(dialog.copyentry);
		int indexfield=dialog.updateByStrings.indexOf(dialog.fieldCombo.getSelectedItem());
		 
		 try{
			 	Object C = dialog.classvalue.elementAt(indexfield);
		 
		 	//System.out.println(C);
		 Integer valueinteger;
		 Double  valuedouble;
		 Boolean valueboolean;
		 String valuestring;
	
		 
		 if(C.toString().equals("java.lang.Integer")){
		 	 valueinteger = new Integer(dialog.changevalue.getText());
		 	dialog.change(dialog.copyentry,dialog.fieldCombo.getSelectedItem().toString(),valueinteger);
		 	//System.out.println("Integer  here");
		 }
		 else if( C.toString().equals("java.lang.Double")) {
		 	 valuedouble = new Double(dialog.changevalue.getText());
		 	dialog.change(dialog.copyentry,dialog.fieldCombo.getSelectedItem().toString(),valuedouble);
		 	//System.out.println(" double here");
		 } else if( C.toString().equals("java.lang.Boolean")){
		 	   if(dialog.changevalue.getText().equals("true")||dialog.changevalue.getText().equals("false")){
		 	  valueboolean = new Boolean(dialog.changevalue.getText());
		 dialog.change(dialog.copyentry,dialog.fieldCombo.getSelectedItem().toString(),valueboolean);
		 //System.out.println("boolean  here");
		 }else
		 	JOptionPane.showMessageDialog(this.dialog, "You have entered a bad boolean value");
		 }
		 else if(C.toString().equals("java.lang.String")){
		 	 valuestring =  dialog.changevalue.getText();
		 	 dialog.change(dialog.copyentry,dialog.fieldCombo.getSelectedItem().toString(),valuestring);		 
		 	//System.out.println("string here");
		 }
		  
		  
		 }catch(Exception e1){
		 	System.out.println("Class not found excpetion");
		 }
		 
		// if((dialog.entity !=null && !dialog.entity.isremoved())&& dialog.copyentry!=null)
		 //	  dialog.entity.setEntry(dialog.copyentry);
		 
		 SwingUtilities.invokeLater(new Runnable() {
		     public void run() {
		     dialog.dispose();
		     }
		 });
		  
		 
		
		
	}	
}