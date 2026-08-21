//Team: Lanikai
//Date: 2005/10/24
//Vers: 1.0
//File: HangupWorker.java

package ipphonehome.ui;

import javax.swing.*;

import rfly.sip.request.RfByeRequest;

/** This class implements the hangup worker thread  that hangs up the user.
 * 
   @author Lanikai
*/
public class HangupWorker extends Thread {
  
  /** Sip Endpoint UI
  */
  private IpPhoneHomeUI ui = null;

  /** Constructor.

      @param ui Reference to client UI.
  */
  public HangupWorker(IpPhoneHomeUI ui) {
    this.ui = ui;
  }

  /** Start here when start message sent to thread object.
 
  */
  public void run() {
	  int value = JOptionPane.showConfirmDialog(null, "Are you sure to Hang up?","Confirm Dialog", JOptionPane.YES_NO_OPTION);
	  
		 if (value == JOptionPane.YES_OPTION) {
			//ui.endpointsiplet.rtpcommunicator.sessionactive=false;
			ui.endpointsiplet.talking=false;
			
			//ui.endpointsiplet.requestBye(); 
			try{
	        	 // Instantiate our request
	        	 
	        	 RfByeRequest request = new RfByeRequest(ui.endpointsiplet.sipotherendpoint);

	    		 // Get the sequence number for use later
	    		    ui.endpointsiplet.seqno = request.getSeqno();
			    // Submit the bye request to 
	    		    ui.endpointsiplet.submit(request);
			    //talking=false;
	    		    ui.getTabPanel().getMainPanel().showMessages.append(" Submitted Bye request to "+ui.endpointsiplet.sipotherendpoint+" seqno="+ui.endpointsiplet.seqno+" : ok.\n");
			      
	    		    ui.endpointsiplet.bye=true;
	    		    synchronized(this) {
				      this.wait(ui.endpointsiplet.TIMEOUT_REQUEST);
				    }
	         }catch(Exception e){
		      System.out.println("I am in bye request exception");
		      System.out.println(e);
	          }			
			ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Hang up Successful....\n");
            ui.getDialButton().setText("   Dial    ");
            ui.getAnsButton().setEnabled(true);
 		 }
		 
		 
	 }
}
