//Team: Lanikai
//Date: 2005/10/24
//Vers: 1.0
//File: AnsWorker.java

package ipphonehome.ui;

import rfly.sip.response.RfOkResponse;
import rfly.sip.response.RfResponse;

/** This class implements the answer worker thread that answer's to the end user in the 
 Sip network
  @author Lanikai
*/
public class AnsWorker extends Thread {

  /** Reference to the Sip Endpoint UI */
  IpPhoneHomeUI ui = null;

  /** Constructor.
      @param ui Reference to sip Endpoint UI. */
  
  public AnsWorker(IpPhoneHomeUI ui) {
    this.ui = ui;
  }

  /** Start here when start message sent to thread object.

  */
     
  public void run() {
	  try{
		  
		 	  
	  ui.endpointsiplet.ringplayer.player.stop();
	  ui.endpointsiplet.ringing=false;
	  RfResponse response = new RfOkResponse(ui.endpointsiplet.sipotherendpoint,ui.endpointsiplet.seqno); 
	  ui.getTabPanel().getMainPanel().sipCallerEndpointAdd.setText(ui.endpointsiplet.sipotherendpoint.getURI().toString());
	  ui.endpointsiplet.respond(response);
	 
    }catch(Exception e){
		 System.out.println("this is RTP exception");
		 System.out.println(e);	 
		 
	 }
	  //Call answered message displayed in the text area of Main Panel
      ui.getTabPanel().getMainPanel().showMessages.append("Call Answered....\n");
      ui.getAnsButton().setText("Hangup");
      ui.getDialButton().setEnabled(false);
 
    }
 
}
