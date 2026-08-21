//Team: Lanikai
//Date: 2005/10/24
//Vers: 1.0
//File: LogoffWorker.java

package ipphonehome.ui;
import java.net.URI;

import javax.swing.*;

/** This class implements the logoff worker that logs off the user from the Sip network.
  @author Lanikai
*/

 public class LogoffWorker extends Thread {
	 private EndpointSiplet endpointsiplet;
  /** Sip Endpoint UI
  */
  private IpPhoneHomeUI ui = null;

  /** Constructor.

      @param ui Reference to client UI.
  */
  public LogoffWorker(IpPhoneHomeUI ui) {
    this.ui = ui;
  }

  /** Start here when start message sent to thread object.
 
  */
  public void run() {
	  try{  
	      
		  int value = JOptionPane.showConfirmDialog(null, "Are you sure to Log off?","Confirm Dialog", JOptionPane.YES_NO_OPTION);
		  
			 if (value == JOptionPane.YES_OPTION) {
				 ui.endpointsiplet.stop();
				
				 ui.getLogonButton().setText(" Logon ");
				  ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Logoff Successful...");
				  ui.getTabPanel().getMainPanel().showMessages.append("\n");
				  ui.getTabPanel().getMainPanel().setDialButton(false);
				  ui.getTabPanel().getMainPanel().setAnswerButton(false);
	            JOptionPane.showMessageDialog(
	            	      ui.getFrame(),
	            	        "Logoff Successful !!!",
	            	        "IP Phone Home Logoff Status",
	            	        JOptionPane.INFORMATION_MESSAGE
	            	      );
	 		 }	      
	       
	    	      
	  }
	  catch(Exception e) {
	    //System.out.println(e);
	    //e.printStackTrace();
	    JOptionPane.showMessageDialog(
      	      ui.getFrame(),
      	        "Logoff UnSuccessful !!!",
      	        "IP Phone Home Logoff Status",
      	        JOptionPane.INFORMATION_MESSAGE
      	      );
	    ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Logoff UnSuccessful...");  
	  }
  
   
   
   
  
  }
}