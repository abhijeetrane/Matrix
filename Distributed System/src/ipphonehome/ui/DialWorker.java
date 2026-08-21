//Team: Lanikai
//Date: 2005/10/24
//Vers: 1.0
//File: DialWorker.java

package ipphonehome.ui;
import java.net.URI;
import jroll.sip.*;
import rfly.sip.*;
import rfly.sip.request.*;
import rfly.sip.response.*;
import rfly.sip.circuit.RfCircuit;


/** This class implements the dial worker thread that dials to the end user in the 
  Sip network
  @author Lanikai
*/
public class DialWorker extends Thread {

  /** Reference to the Sip Endpoint UI */
  IpPhoneHomeUI ui = null;

  /** Constructor.
      @param ui Reference to sip Endpoint UI. */
  public DialWorker(IpPhoneHomeUI ui) {
    this.ui = ui;
  }

  /** Start here when start message sent to thread object.

  */
     
  public void run() {
	 try{
	  

	    // Submit the circuit request to PBX and wait for a response
	    ui.endpointsiplet.requestCircuit(ui.servlet);
	    ui.otherendpoint=new URI(ui.getSipEndpointAdd());
	    
	    //Submit the invite request to other endpoint siplet  and wait for response
	    ui.endpointsiplet.requestInvite(ui.otherendpoint);
	    
	    if(!ui.endpointsiplet.trying)
	    	throw new Exception();
	    /*if(ui.endpointsiplet.trying && !ui.endpointsiplet.ringing){
    	//ui.endpointsiplet.talking=false;
    	RfRequest cancel = new RfCancelRequest(ui.endpointsiplet.sipotherendpoint);
        ui.endpointsiplet.submit(cancel);
    	ui.endpointsiplet.handback();
    	
    }
      if(ui.endpointsiplet.trying && ui.endpointsiplet.ringing && !ui.endpointsiplet.talking){
    	//ui.endpointsiplet.talking=false;
    	RfRequest cancel = new RfCancelRequest(ui.endpointsiplet.sipotherendpoint);
        ui.endpointsiplet.submit(cancel);
    	ui.endpointsiplet.handback();
    	
    }*/    
	      ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Dial Successful....\n");
	      ui.getDialButton().setText("Hangup");
	      ui.getAnsButton().setEnabled(false);
	     
	    
  }
  catch(Exception e) {
    //System.out.println(e);
    //e.printStackTrace();
	  ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Endpoint unknown Dial UnSuccessful\n");
     ui.endpointsiplet.handback();
     if(ui.endpointsiplet.handback)
    	 ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Handback  successful\n");
     else
    	 ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Handback failed\n");
    	 
     
  }
	    
	  
 
    }
 
}
