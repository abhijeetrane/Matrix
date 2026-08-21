//Team: Lanikai
//Date: 2005/12/05
//Vers: 1.0
//File: AnsHangupWorker.java

package ipphonehome.ui;

import javax.swing.*;

import rfly.sip.request.RfByeRequest;

/** This class implements the answer's hangup worker thread  that hangs up  the end user.
 *   @author Lanikai
*/
public class AnsHangupWorker extends Thread {

  /** Reference to the Sip Endpoint UI */
  IpPhoneHomeUI ui = null;

  /** Constructor.
      @param ui Reference to sip Endpoint UI. */
  public AnsHangupWorker(IpPhoneHomeUI ui) {
    this.ui = ui;
  }

  /** Start here when start message sent to thread object.

  */
     
  public void run() {
	  int value = JOptionPane.showConfirmDialog(null, "Are you sure to Hang up?","Confirm Dialog", JOptionPane.YES_NO_OPTION);
	 
		 
		if (value == JOptionPane.YES_OPTION) {
		   //ui.endpointsiplet.rtpcommunicator.sessionactive=false;
			ui.endpointsiplet.talking=false;
			/*ui.endpointsiplet.requestBye();*/
			
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
	        ui.getAnsButton().setText("Answer");
            ui.getDialButton().setEnabled(true);
		 }
    }
 
}
