//Team: Tahiti
//Date: 2005/05/27
//Vers: 1.0
//File: DeleteDialog.java


package vista.ui;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import vista.tuples.*;
import javax.swing.*;
import vista.object.*;

/** This class deletes an entry by Clid
 @author Tahiti.
 */
public class DeleteDialog extends JDialog {
   private JButton submitButton;
/** value field */
	private JTextField id;
   private JButton cancelButton;


public DeleteDialog(JFrame owner) {
 super(owner,"Vista - Inspect / Modify",true);  // true == modal

 GriddedPanel mainPanel = new GriddedPanel();

// mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));


 mainPanel.addComponent(new JLabel("Enter the Id"),0,0);


 id=new JTextField(5);

 mainPanel.addComponent(id,0,1);



  submitButton = new JButton("submit");

submitButton.addActionListener(new DeleteSubmitListener(this,id));

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
class DeleteSubmitListener implements ActionListener {
  private JTextField id;
  private JDialog di;


/**Constructor
*
* @param di JDialog
* @param id JTextfield
*/
public DeleteSubmitListener(JDialog di,JTextField id) {
  this.id=id;
  this.di=di;
}
/** Invoked on button click.
 @param e Action event. */
public void actionPerformed(ActionEvent e) {
	
	
	try{
		if(id.getText().equals(""))
			JOptionPane.showMessageDialog(id, "You have not entered a clid");
		else 
		{	

	
			
			Vector vector = new Vector();
			Integer tempint = new Integer(id.getText());
			LiveEntities live = VistaUI.getLiveEntities();
	    	vista.tuples.Iterator iter = live.getIterator(tempint);
		
	    	Entities dead = VistaUI.getDeadEntities();
			
	    	
	    	Entity localentity = iter.next();
	    	
		    while(localentity !=null)
			{
		    	
		    	vector.addElement(localentity);
		    	dead.insert(localentity);
		    	localentity.setDisposed();	
		    	localentity.setDeath(System.currentTimeMillis());
		    	localentity = iter.next();

				   	
			}
		    for(int j=0; j < vector.size(); j++) {
		    	live.remove(((Entity)vector.elementAt(j)).getId());
		    }
			
			
			
			
			
		}
	}catch(NumberFormatException ne){
		JOptionPane.showMessageDialog(id, "You have entered an invalid clid");
	}
	 SwingUtilities.invokeLater(new Runnable() {
	     public void run() {
	     di.dispose();
	     }
	 });


}

}







