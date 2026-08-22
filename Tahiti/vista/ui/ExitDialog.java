//Team: Tahiti
//Date: 2005/05/27
//Vers: 1.0
//File: ExitDialog.java

package vista.ui;
import java.awt.*;
import java.awt.event.*;
import vista.tuples.*;
import javax.swing.*;

import vista.object.*;

/** This class contains buttons for Submit and Cancel.

 @author Tahiti.

**/

public class ExitDialog extends JDialog {
   private JButton submitButton;
   private JButton cancelButton;

public ExitDialog(JFrame owner) {
 super(owner,"Vista - Inspect / Modify",true);  // true == modal
  GriddedPanel mainPanel = new GriddedPanel();
  mainPanel.addComponent(new JLabel("Do you want to exit Vista "),0,0);
  submitButton = new JButton("submit");
  submitButton.addActionListener(new ExitSubmitListener(this));
  mainPanel.addComponent(submitButton,1,0);
  cancelButton = new JButton("cancel");
  cancelButton.addActionListener(new CancelListener(this ));
  mainPanel.addComponent(cancelButton,2,0);
  getContentPane().add(mainPanel);
  setSize(200,250);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setVisible(true);
}


}
/** This class handles  clicks on the submit button. */
class ExitSubmitListener implements ActionListener {
    private JDialog di;
/**Constructor
*
* @param di JDialog
*/
public ExitSubmitListener(JDialog di) {
  
  this.di=di;
}
/** Invoked on button click.
 @param e Action event. */
public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
     public void run() {
     di.dispose();
     }
 });

System.exit(0);
}
}
/** This class handles  clicks on the cancel button. */
class CancelListener implements ActionListener {
  private JDialog di;
/**Constructor
*
* @param di JDialog
*/
public CancelListener(JDialog di) {
  
  this.di=di;
}
/** Invoked on button click.
 @param e Action event. */
public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
     public void run() {
     di.dispose();
     }
 });

}
}






