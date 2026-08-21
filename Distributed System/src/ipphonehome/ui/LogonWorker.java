//Team: Lanikai
//Date: 2005/10/24
//Vers: 1.0
//File: LogonWorker.java

package ipphonehome.ui;
import javax.swing.*;
import java.net.URI;

/** This class implements the logon worker that logs the user to the Sip network.

    @author Lanikai
*/
public class LogonWorker extends Thread {
	private EndpointSiplet endpointsiplet;
  /** Reference to the Sip Endpoint UI */
  IpPhoneHomeUI ui = null;

  /** Constructor.
      @param ui Reference to sip Endpoint UI. */
  public LogonWorker(IpPhoneHomeUI ui) {
    this.ui = ui;
  }
  
  

  /** Start here when start message sent to thread object.

  */
     
  public void run() {
    // Pop-up wait dialog
    StatusDialog waitDialog = new StatusDialog(ui.getFrame(),"Logging on, Please Wait...");      
    try{  
       ui.servlet = new URI(ui.getSipServerAdd());     
    	
    	waitDialog.show();
    	if(ui.endpointsiplet==null)
    	ui.endpointsiplet=new EndpointSiplet(ui.servlet,ui);    
      
      
  	  ui.endpointsiplet.start(); 	  
     // Destroy the wait dialog and pop-up modal status dialog.
      waitDialog.dispose();       
      JOptionPane.showMessageDialog(
  	        ui.getFrame(),
  	        "Logon Successful !!!",
  	        "IP Phone Home Logon Status",
  	        JOptionPane.INFORMATION_MESSAGE
  	      );
        ui.getLogonButton().setText(" Logoff ");
        ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Logon Successful...");
        ui.getTabPanel().getMainPanel().showMessages.append("\n");
        ui.getTabPanel().getMainPanel().setDialButton(true);
        ui.getTabPanel().getMainPanel().setAnswerButton(true);
  }
  catch(Exception e) {
    //System.out.println(e);
    //e.printStackTrace();
	  waitDialog.dispose();  
	  JOptionPane.showMessageDialog(
		        ui.getFrame(),
		        "Logon UnSuccessful !!!",
		        "IP Phone Home Logon Status",
		        JOptionPane.INFORMATION_MESSAGE
		      );
	  ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Logon UnSuccessful...\n");  
  }
  
    }
 
}
