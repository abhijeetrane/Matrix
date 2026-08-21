//Team: Lanikai
//Date: 2005/12/05
//Vers: 1.0
//File: ClearMessageListener.java

package ipphonehome.ui;
import java.awt.event.*;

/** Listener for the clear messages menu button.

    @author Lanikai 
 */

 public class ClearMessageListener implements ActionListener {
  private IpPhoneHomeUI ui = null;

  /** Constructor.

      @param ui Reference to the Sip endpoint UI.
      @see IpPhoneHomeUI
  */
  public ClearMessageListener(IpPhoneHomeUI ui) {
    this.ui = ui;
  }

  /** 

      When invoked, it clears the message from the JTextArea.

      @param event Event information from Swing.
  */
  public void actionPerformed(ActionEvent event) {
                  ui.getTabPanel().getMainPanel().showMessages.setText("");
                  return; // to...Swing
  }
}