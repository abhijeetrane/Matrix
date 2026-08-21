//Team: Lanikai
//Date: 2005/10/24
//Vers: 1.0
//File: IPMenu.java

package ipphonehome.ui;


import java.awt.event.*;
import javax.swing.*;

/** This class implements the pulldown menu on the menu bar 
    @author Lanikai
 */

 public class IPMenu extends JMenuBar implements ActionListener {
	 /**
	  *  Sip Endpoint UI
	  */
	 private IpPhoneHomeUI ui;
	

	 public IPMenu(IpPhoneHomeUI ui) {
		 this.ui=ui;
		 // Creating the Menu
		 JMenu menu=new JMenu("Menu");
		 menu.setMnemonic('M');
		 add(menu);
		 
         //Add the menu items
		 JMenuItem save=new JMenuItem("Save Messages",KeyEvent.VK_S);
		 //save.addActionListener(this);
		 save.setAccelerator(KeyStroke.getKeyStroke(
				 KeyEvent.VK_S, ActionEvent.ALT_MASK));
		 save.getAccessibleContext().setAccessibleDescription(
				 "This doesn't really do anything");
		 menu.add(save);

		 JMenuItem clear=new JMenuItem("Clear Messages", KeyEvent.VK_C);
		 //calls the actionlisterner for clear messages.
		 clear.addActionListener(new ClearMessageListener(ui));
		 clear.setAccelerator(KeyStroke.getKeyStroke(
				 KeyEvent.VK_C, ActionEvent.ALT_MASK));
		 clear.getAccessibleContext().setAccessibleDescription(
				 "This doesn't really do anything");
		 menu.add(clear);
		 

		 JMenuItem exit=new JMenuItem("Exit",KeyEvent.VK_X);
		 exit.addActionListener(this);
		 exit.setAccelerator(KeyStroke.getKeyStroke(
		            KeyEvent.VK_X, ActionEvent.ALT_MASK));
		 exit.getAccessibleContext().setAccessibleDescription(
		         "This doesn't really do anything");
		 menu.add(exit);
		 exit.add(new JSeparator());

	 }
	 /** 

     When invoked, it asks user for confirmation for exit.

     @param e  Event information from Swing.
    */
	 public void actionPerformed(ActionEvent e) {
	
		int value = JOptionPane.showConfirmDialog(null, "Are you sure to Exit?","Exit Dialog", JOptionPane.YES_NO_OPTION);
	    if (value == JOptionPane.YES_OPTION) 
			 System.exit(0);
		
		 

			 		   
       }
       
	 
	 

 }
