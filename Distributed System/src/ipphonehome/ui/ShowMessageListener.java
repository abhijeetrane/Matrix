//Team: Lanikai
//Date: 2005/12/05
//Vers: 1.0
//File: ShowMessageListener.java

package ipphonehome.ui;
import java.awt.event.*;

/** Listener for the show message checkbox .

    @author Lanikai 
 */

 public class ShowMessageListener implements ItemListener {
  private IpPhoneHomeUI ui = null;

  /** Constructor.

      @param ui Reference to the Sip endpoint UI.
  */
  public ShowMessageListener(IpPhoneHomeUI ui) {
    this.ui = ui;
  }

  /** 

      When checkbox selected it shows textarea and when deselected it makes it invisible.

      @param e Event information from Swing.
  */
  public void itemStateChanged(ItemEvent e) {
	    if (e.getStateChange() == ItemEvent.DESELECTED)
	       	    	ui.getTabPanel().getMainPanel().getScrollPane().setVisible(false);
	    if (e.getStateChange() == ItemEvent.SELECTED)
   	    	ui.getTabPanel().getMainPanel().getScrollPane().setVisible(true);    
	    	
  }

}