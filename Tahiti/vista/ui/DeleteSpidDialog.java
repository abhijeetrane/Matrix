//Team: Tahiti
//Date: 2005/05/27
//Vers: 1.0
//File: DeleteSpidDialog.java

package vista.ui;
import java.awt.*;
import java.awt.event.*;
import vista.tuples.*;
import javax.swing.*;

import vista.object.*;

/** This class deletes an entry by Spid
 @author Tahiti.
*/
   public class DeleteSpidDialog extends JDialog{
   
   private JButton submitButton;
    /** value field */
	private JTextField id;
    private JButton cancelButton;


public DeleteSpidDialog(JFrame owner) {
 super(owner,"Vista - Inspect / Modify",true);  // true == modal

 GriddedPanel mainPanel = new GriddedPanel();

// mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));


 mainPanel.addComponent(new JLabel("Enter the Id"),0,0);


 id=new JTextField(5);

 mainPanel.addComponent(id,0,1);



  submitButton = new JButton("submit");

  submitButton.addActionListener(new DeleteSpidSubmitListener(this,id));

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
class DeleteSpidSubmitListener implements ActionListener {
  private JTextField id;
  private JDialog di;


/**Constructor
*
* @param di JDialog 
* @param id JTextField 
*/
public DeleteSpidSubmitListener(JDialog di,JTextField id) {
  this.id=id;
  this.di=di;
}
/** Invoked on button click.
 @param e Action event. */
public void actionPerformed(ActionEvent e) {
	
	   
	   try{
		if(id.getText().equals(""))
			JOptionPane.showMessageDialog(id, "You have not entered a spid");
		else 
		{
			 //System.out.println("The dead entities "+VistaUI.getDeadEntities().getSize());
			  Long tempint = new Long(Long.parseLong(id.getText()));
		      Entity w =VistaUI.getLiveEntities().remove(tempint.longValue());
		      
		      if(w!=null){
			  		 VistaUI.vistaSpace.dispose(w.getId());
			         w.setDisposed();
	                 		    
		      	VistaUI.getDeadEntities().insert(w);
			         w.setDeath(System.currentTimeMillis());
			         //System.out.println("The dead entities "+VistaUI.getDeadEntities().getSize());
		        
		      } 
		      else
		      	JOptionPane.showMessageDialog(id, "You have entered an invalid spid");
		      
		}
		}catch(NumberFormatException ne){
			JOptionPane.showMessageDialog(id, "You have entered an invalid spid");
		}
	  
	      
	     
	
	      
	      
	SwingUtilities.invokeLater(new Runnable() {
     public void run() {
     di.dispose();
     }
 });


}
}







