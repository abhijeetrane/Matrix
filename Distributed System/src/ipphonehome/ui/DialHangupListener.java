//Team: Lanikai
//Date: 2005/12/05
//Vers: 1.0
//File: DialHangupListener.java

package ipphonehome.ui;
import java.awt.event.*;

/** Listener for the dial/hangup button.

    @author Lanikai */

public class DialHangupListener implements ActionListener {
  private IpPhoneHomeUI ui = null;

  /** Constructor.

      @param ui Reference to the Sip endpoint UI.
  */
  public DialHangupListener(IpPhoneHomeUI ui) {
    this.ui = ui;
  }

  /** Button event handler.

      When invoked, it checks the button status and will do
      either dial or hangup.

      @param event Event information from Swing.
  */
  public void actionPerformed(ActionEvent event) {
    if(ui.getDialButton().getText().equals("   Dial    ")) {
      DialWorker dialWorker = new DialWorker(ui);
       dialWorker.start();
    }
    else {
      HangupWorker hangupWorker = new HangupWorker(ui);
      hangupWorker.start();
    }

    return; // to...Swing
  }
}