//Team: Lanikai
//Date: 2005/10/24
//Vers: 1.0
//File: LogonoffListener.java

package ipphonehome.ui;
import java.awt.event.*;

/** Listener for the logon/off button.

    @author Lanikai 
 */

 public class LogonoffListener implements ActionListener {
  private IpPhoneHomeUI ui = null;

  /** Constructor.

      @param ui Reference to the Sip endpoint UI.
  */
  public LogonoffListener(IpPhoneHomeUI ui) {
    this.ui = ui;
  }

  /** Button event handler.

      When invoked, it checks the button status and will do
      either logon or logoff.

      @param event Event information from Swing.
  */
  public void actionPerformed(ActionEvent event) {
    if(ui.getLogonButton().getText().equals(" Logon ")) {
      LogonWorker logonWorker = new LogonWorker(ui);
       logonWorker.start();
    }
    else {
      LogoffWorker logoffWorker = new LogoffWorker(ui);

      logoffWorker.start();
    }

    return; // to...Swing
  }
}