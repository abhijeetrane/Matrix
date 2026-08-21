//Team: Lanikai
//Date: 2005/12/05
//Vers: 1.0
//File: AnsHangupListener.java

package ipphonehome.ui;
import java.awt.event.*;

/** Listener for the Answer/Hangup button.

    @author Lanikai */

public class AnsHangupListener implements ActionListener {
  private IpPhoneHomeUI ui = null;

  /** Constructor.

      @param ui Reference to the Sip endpoint UI.
  */
  public AnsHangupListener(IpPhoneHomeUI ui) {
    this.ui = ui;
  }

  /** Button event handler.

      When invoked, it checks the button status and will do
      either answer or hangup.

      @param event Event information from Swing.
  */
  public void actionPerformed(ActionEvent event) {
    if(ui.getAnsButton().getText().equals("Answer")) {
      AnsWorker ansWorker = new AnsWorker(ui);
       ansWorker.start();
    }
    else {
      AnsHangupWorker anshangupWorker = new AnsHangupWorker(ui);

      anshangupWorker.start();
    }

    return; // to...Swing
  }
}