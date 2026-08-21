//Team: Tahiti
//Date: 2005/04/27
//Vers: 1.0
//File: SaveDialog.java

package vista.ui;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import vista.test.LastUpdate;
import vista.tuples.*;
import vista.util.*;
import javax.swing.*;

import space.nucleus.*;
import vista.object.*;

import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * This class saves the  status of the vista into the file.
 * 
 * @author Tahiti
 *
 * 
 */
public class SaveDialog extends JDialog {
   private JButton submitButton;
/** value field */
	private JTextField id;
    private JButton cancelButton;


public SaveDialog(JFrame owner) {
 super(owner,"Vista ",true);  // true == modal

 GriddedPanel mainPanel = new GriddedPanel();

// mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));


 mainPanel.addComponent(new JLabel("Enter the File Name"),0,0);


 id=new JTextField(5);

 mainPanel.addComponent(id,0,1);



  submitButton = new JButton("submit");

  submitButton.addActionListener(new SaveSubmitListener(this,id));

  mainPanel.addComponent(submitButton,3,0);

  cancelButton = new JButton("cancel");

  cancelButton.addActionListener(new CancelListener(this ));
  mainPanel.addComponent(cancelButton,4,0);




 getContentPane().add(mainPanel);

 setSize(200,200);

 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

 setVisible(true);
}



}
/** This class handles  clicks on the inspect button. */
class SaveSubmitListener implements ActionListener {
  private JTextField id;
  private JDialog di;
  private SimpleDateFormat formatter = new SimpleDateFormat ("hh:mm:ss a");


/**Constructor
*
* @param  di JDialog
* @param  id JTextField
*/
public SaveSubmitListener(JDialog di,JTextField id) {
  this.id=id;
  this.di=di;
}
/** Invoked on button click.
 @param e Action event. */
public void actionPerformed(ActionEvent e) {
	
	if(id.getText().equals(""))
		JOptionPane.showMessageDialog(id, "You have not entered a file name");
	else{ 
	FileSave.getInstance(id.getText()+".txt");
	
	LiveEntities live = VistaUI.getLiveEntities();
	FileSave.getInstance().write("Entries "+new Integer(live.getSize()).toString());
	FileSave.getInstance().write("Threads "+new Integer(VistaUI.spaceSleeping).toString());
	FileSave.getInstance().write("Listeners "+new Integer(VistaUI.spaceRegisters).toString());
	FileSave.getInstance().write("Writes "+new Integer(VistaUI.spaceWrites).toString());
	FileSave.getInstance().write("Reads "+new Integer(VistaUI.spaceReads).toString());
	FileSave.getInstance().write("Takes "+new Integer(VistaUI.spaceTakes).toString());
	FileSave.getInstance().write("Notifies "+new Integer(VistaUI.spaceNotifies).toString());
	long uptimedate = System.currentTimeMillis()-VistaUI.spaceStart;
	 LastUpdate uptime =new LastUpdate();
	 uptime.calculate(uptimedate);
	 
	 FileSave.getInstance().write("Uptime "+uptime.hours+":"+uptime.minutes+":"+uptime.seconds);
	 long tempdate =System.currentTimeMillis() - VistaUI.lastupdate;
	 
	 LastUpdate lastupdate =new LastUpdate();
	 lastupdate.calculate(tempdate);
	 if(VistaUI.lastupdate==0)
	 	FileSave.getInstance().write("Last update "+"0"+":"+"0"+":"+"0");
	 else
	 	FileSave.getInstance().write("Last update "+"-"+lastupdate.hours+":"+lastupdate.minutes+":"+lastupdate.seconds);
	 FileSave.getInstance().write("");
	
	FileSave.getInstance().write("Live Records "+ live.getSize());
	
	Iterator iter = live.getIterator();
	for(int i= 0; i< live.getSize();i++){
	Entity entity =iter.next();
	if(entity!= null){
	FileSave.getInstance().write("Class "+entity.getEntry().getClass().getName());
	FileSave.getInstance().write("Clid "+(Entities.clidtab.get(entity.getEntry())).getId().toString());
	FileSave.getInstance().write("Spid "+new Long (entity.getId()).toString());
	FileSave.getInstance().write("Reads "+new Long(entity.getReads()).toString());
	FileSave.getInstance().write("Owner "+entity.getOwner().toString());
	FileSave.getInstance().write("Written "+formatter.format(new Date(entity.getBirth())).toString());
	FileSave.getInstance().write("Expires "+formatter.format(new Date(entity.getDeath())).toString());
	}
	}
	FileSave.getInstance().write("");
	DeadEntities dead =VistaUI.getDeadEntities();
	FileSave.getInstance().write("Dead Records "+ dead.getSize());
	Iterator iterdead = dead.getIterator();
	for(int i= 0; i< dead.getSize();i++){
	Entity entity =iterdead.next();
	if(entity!= null){
	FileSave.getInstance().write("Class "+entity.getEntry().getClass().getName());
	FileSave.getInstance().write("Clid "+(Entities.clidtab.get(entity.getEntry())).getId().toString());
	FileSave.getInstance().write("Spid "+new Long (entity.getId()).toString());
	FileSave.getInstance().write("Reads "+new Long(entity.getReads()).toString());
	FileSave.getInstance().write("Owner "+entity.getOwner().toString());
	FileSave.getInstance().write("Written "+formatter.format(new Date(entity.getBirth())).toString());
	FileSave.getInstance().write("Expires "+formatter.format(new Date(entity.getDeath())).toString());
	}
	
	}
	}
	SwingUtilities.invokeLater(new Runnable() {
	     public void run() {
	     di.dispose();
	     }
	 });

}
}







