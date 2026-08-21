//Team: Lanikai
//Date: 2005/12/06
//Vers: 1.0
//File: EndpointSiplet.java

package ipphonehome.ui;
import java.net.URI;
import jroll.atp.MP3FilePlayer;
import jroll.sip.*;
import rfly.sip.*;
import rfly.sip.request.*;
import rfly.sip.response.*;
import rfly.sip.response.entry.RfResponseEntry;
import rfly.sip.response.entry.RfTryingEntry;
import rfly.sip.circuit.RfCircuit;

/** Siplet which handles the sip messages. */
public class EndpointSiplet extends RfSiplet implements SipListener {
	/** Use PCM mode */
	  public final static int MODE_PCM = 0;

	  /** Use GSM mode */
	  public final static int MODE_GSM = 1;

	/** Circuit object */
	  protected RfCircuit circuit;

	  /** Sequence number of a request/response */
	  protected Long seqno;

	  /** Ten second wait for responses */
	  protected static long TIMEOUT = 10000;

	  /** Ring player */
	  protected RingPlayer ringplayer;

	  /** Busy ring player */
	  protected BusyRingPlayer busyringplayer;

	  /** SIP OtherendPoint */
	  protected SipEndPoint sipotherendpoint;

	  /** SipEndPoint pbx */
	  protected SipEndPoint pbx;
	  /** It checks if this endpoint dialed the other party**/
	  protected  boolean dialer=false;
	  /**It checks if it got the trying response**/
	  protected  boolean trying=false;
	  /**It checks if it is ringing**/
	  protected  boolean ringing=false;
	  /**It checks if it is talking**/
	  protected  boolean talking=false;
	  /**It checks if it send the bye**/
	  protected  boolean bye=false;
	  /**It checks if it handback the circuit**/
	  protected  boolean handback=false;
	  /**It starts the thread for talking and listening**/
	  protected RTPCommunicator rtpcommunicator;
	  /**It is an instance of ipphonehomeui**/
	  private IpPhoneHomeUI ui;

	   /** Wait for space responses */
	   protected final static long TIMEOUT_RESPONSE = 10000;


	   /** Wait for space requests */
	   protected final static long TIMEOUT_REQUEST = 120 * 1000;

	   /** Default listen duration */
	   protected final static long TIMEOUT_LISTEN = 10 * 1000;

	   /** Default ringing duration */
	   protected final static long TIMEOUT_RINGING = 5 * 1000;

	   /** Default trying duration */
	   protected final static long TIMEOUT_TRYING = 2 * 1000;

	   /** Default busy duration */
	   protected final static long TIMEOUT_BUSY = 15 * 1000;
	  /** Constructor */

   public EndpointSiplet(URI servlet,IpPhoneHomeUI ui) throws Exception {
	    super(servlet);
	    ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Siplet constructed: ok.");
	    addListener(this);
	    this.ui=ui;
	    start();
	  }

	  /** Request a circuit */
	public void requestCircuit(URI servlet) throws Exception {
	    // Get info about this siplet
	    System.out.println("Siplet at "+SipCaller.getURI() +
	      " aka "+SipCaller.getName()+" blocking id "+SipCaller.getBlockId()+" : ok");

	    // Get end point to (PBX) switchboard
	    String pbxHost = new RfURIParser(servlet).getHostPort();
	     pbx = new SipEndPoint(new URI("sip:pbx"+"@"+pbxHost));

	    // Instantiate our request
	    RfRequest request = new RfCircuitRequest(pbx);

	    // Get the sequence number for use later
	    seqno = request.getSeqno();

	    // Submit the request to PBX and wait for a response
	    submit(request);
	    ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Submitted circuit request to "+pbx+" seqno="+seqno+" : ok.\n");

	    synchronized(this) {
	      this.wait(TIMEOUT);
	    }

	    if(circuit == null) {
	    	ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Sorry: PBX down or all circuits busy.\n");
	      System.exit(1);
	    }
	    dialer=true;
	    ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Got circuit "+circuit+" : ok.\n");
    //	  Configure the circuit for PCM or GSM via the vocode

	    if(ui.getTabPanel().getConfigPanel().group.getSelection().getActionCommand().equals("MODE_PCM"))
	    	circuit.setType(MODE_PCM);
	    if(ui.getTabPanel().getConfigPanel().group.getSelection().getActionCommand().equals("MODE_GSM"))
	    	circuit.setType(MODE_GSM);


	}
/**
 * It sends an invite request to other endpoint
 * @param otherendpoint
 */
  public void requestInvite(URI otherendpoint ){

	    System.out.println("Siplet at "+SipCaller.getURI() +
		      " aka "+SipCaller.getName()+" blocking id "+SipCaller.getBlockId()+" : ok\n");

	    // Get end point to (PBX) switchboard
		sipotherendpoint = new SipEndPoint(otherendpoint);


	   // Instantiate our request
	   RfRequest request = new RfInviteRequest(sipotherendpoint,circuit);

	   if(ui.getTabPanel().getConfigPanel().blockidButton.isSelected())
		   ((RfInviteRequest)request).setBlock(true);
	   else
		   ((RfInviteRequest)request).setBlock(false);

        // Get the sequence number for use later
        seqno = request.getSeqno();
        try{
		    // Submit the request to PBX and wait for a response
		    submit(request);
		    ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Submitted invite request to "+sipotherendpoint+" seqno="+seqno+" : ok.\n");
		    synchronized(this) {
			      this.wait(TIMEOUT_RESPONSE);
			    }

         }catch(Exception e){
  	     System.out.println("I am in invite request exception");
	     System.out.println(e);
         }
	  }
/**
 * It sends a bye request to the other endpoint
 *
 */
   public void requestBye(){

         try{
        	 // Instantiate our request

        	 RfByeRequest request = new RfByeRequest(sipotherendpoint);

    		 // Get the sequence number for use later
    		    seqno = request.getSeqno();
		    // Submit the bye request to
    		    submit(request);
		    //talking=false;
    		    ui.getTabPanel().getMainPanel().showMessages.append(" Submitted Bye request to "+sipotherendpoint+" seqno="+seqno+" : ok.\n");
		      bye=true;
    		    synchronized(this) {
			      this.wait(TIMEOUT_REQUEST);
			    }
         }catch(Exception e){
	      System.out.println("I am in bye request exception");
	      System.out.println(e);
          }


	  }
   /**
    * SIP request events get dispatched here
    */
  public void requestEvent(SipRequest request) {
		 seqno = request.getSeqno();

		  if(request instanceof RfInviteRequest){
			  if(ui.getTabPanel().getConfigPanel().busyButton.isSelected()){
				  sipotherendpoint = new SipEndPoint(request.getSource().getURI());

				  ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Responding TRYING seqno="+seqno+"\n");
                   try{
				  RfResponse response= new RfTryingResponse(sipotherendpoint,seqno);

				  respond(response);
                   }catch(Exception e){
                	   e.printStackTrace();
                   }
				    try {
				      Thread.sleep(TIMEOUT_TRYING);
				    }
				    catch(Exception e) {
				    }
                    try{
                    	ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Responding BUSY seqno="+seqno+"\n");

				    RfResponse busyresponse = new RfBusyResponse(sipotherendpoint,seqno);
					 respond(busyresponse);
                    }catch(Exception e){
                    	e.printStackTrace();
                    }

			  }else{//start of else
			 try{
				 circuit = ((RfInviteRequest)request).getCircuit();

				 ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Invite request received\n");
                // Instantiate our response
			  sipotherendpoint = new SipEndPoint(request.getSource().getURI());
			  if(!((RfInviteRequest)request).getBlock())
			  ui.getTabPanel().getMainPanel().sipCallerEndpointAdd.setText(sipotherendpoint.getURI().toString());
			  RfResponse response= new RfTryingResponse(sipotherendpoint,seqno);

			  respond(response);
			 if(talking){
				 RfResponse busyresponse = new RfBusyResponse(sipotherendpoint,seqno);
				 respond(busyresponse);
				 return;
			 }

			  RfResponse ringingresponse= new RfRingingResponse(sipotherendpoint,seqno);

			  respond(ringingresponse);
		      ringing=true;
		      //System.out.println("ringing is true");
		      ringplayer = new RingPlayer();
			  Thread ringing  =new Thread(ringplayer);
	      	  ringing.start();
			  }catch(Exception e){
		 	  System.out.println("I am the ringing response");
			  System.out.println(e);
				}
			  }//end of else


			    }
		  if(request instanceof RfAckRequest){
			  try {
				  ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Acknowedge request received\n");
				talking=true;
				rtpcommunicator= new RTPCommunicator(ui,circuit);
				 new Thread(rtpcommunicator).start();


			} catch (Exception e) {
				e.printStackTrace();
			}

		  }
		  if(request instanceof RfCancelRequest){
			  try {
				  ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Cancel request received\n");

				  RfResponse response = new RfOkResponse(sipotherendpoint,seqno);
				  respond(response);
				  ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" OK response to CANCEL\n");
				  ringplayer.player.stop();


			} catch (Exception e) {
				e.printStackTrace();
			}

		  }
		  if(request instanceof RfByeRequest){
			  try{

				  ui.getTabPanel().getMainPanel().showMessages.append("\nbye request received \n");
				  talking=false;
				  RfResponse response = new RfOkResponse(sipotherendpoint,seqno);
				  respond(response);
				  ui.getTabPanel().getMainPanel().showMessages.append("OK sent for bye\n");
				  if(dialer)
					  handback();
			  } catch (Exception e) {
					e.printStackTrace();
				}
		  }
	  }
/**
 * SIP response events get dispacted here
 */
public void responseEvent(SipResponse response) {
	    Long inSeqno = response.getSeqno();
       // System.out.println(ringing);
	    ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Got response "+response.getClass().getName()+" for seqno="+inSeqno+" : ok\n");
	    if(response instanceof RfCircuitResponse) {
	      if(!seqno.equals(inSeqno)) {
	    	  ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Sequence number "+seqno+" not correct: ignoring\n");
	        return;
	      }

	      circuit = ((RfCircuitResponse) response).getCircuit();

	      synchronized(this) {
	        System.out.println("Synchronizing with main thread: ok");
	        this.notify();
	      }
	    }
	    else if(response instanceof RfAllBusyResponse && seqno.equals(inSeqno)) {
	      synchronized(this) {
	    	  ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Received all circuits busy for "+seqno+" : NOT ok.\n");
	        this.notify();
	      }
	    }
	    else if(response instanceof RfOkResponse) {
	      // Good, they liked our request...so far.
	      System.out.println("Received ok response: ok");
	      if(bye){

	    	  synchronized(this) {
	    		//handback=false;
	    		  ui.getTabPanel().getMainPanel().showMessages.append("Received OK response for bye..\n");
	  	        this.notify();
	  	        bye=false;
	  	    	}

	      }
	      if(handback){

	    	  synchronized(this) {
	    		//handback=false;
	    		  ui.getTabPanel().getMainPanel().showMessages.append("Received OK response for handback request..\n");
	  	        this.notify();
	  	    	}



	      }

	      if(ringing){
	    	  synchronized(this) {
	    		  ui.getTabPanel().getMainPanel().showMessages.append("Received OK for INVITE seqno="+seqno+"\n");
			        this.notify();
			      }

	    	 ringplayer.player.stop();
	         ringing=false;
	      //System.out.println("I am in ringing");

        try{
        	RfRequest request = new RfAckRequest(sipotherendpoint,new Integer(0));
            submit(request);
            talking=true;
            ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" ack request sent\n");
            rtpcommunicator= new RTPCommunicator(ui,circuit);
            new Thread(rtpcommunicator).start();


        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("this is RTP exception");
        	System.out.println(e);
        }
        //return;
        }//end of ringing
	    if(talking){
	    	synchronized(this) {
	    		ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Received OK response for bye request\n");
	        this.notify();
	    	}

	    	}

	    }else if(response instanceof RfTryingResponse){
	    	trying=true;
	    	synchronized(this) {
	    		ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Received trying response ok.");
		        this.notify();
		      }
	    }   else if(response instanceof RfRingingResponse){
	    	synchronized(this) {
	    		ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" Received trying response ok.\n");
		        this.notify();
		      }

	    	try{
	            ringing=true;
	         ringplayer = new RingPlayer();
	       	 Thread ringing  =new Thread(ringplayer);
	       	 ringing.start();
	       	ui.getTabPanel().getMainPanel().showMessages.append("Waiting for OK (pickup) response seqno="+seqno+" from "+sipotherendpoint+"\n");


	       	try {
			      Thread.sleep(TIMEOUT_RINGING);
			    }catch(Exception e){
			    	e.printStackTrace();
			    }



	         }catch(Exception e){
	        	 e.printStackTrace();
	       		System.out.println("I am the ringing response");
	       		System.out.println(e);

	       	}



	    }else if(response instanceof RfBusyResponse){
	    	try{

	       	 busyringplayer = new BusyRingPlayer();
	       	 Thread busyringing  =new Thread(busyringplayer);
	       	 busyringing.start();
	       	try {
			      Thread.sleep(TIMEOUT_BUSY);
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
	       	 busyringplayer.player.stop();
	       	 handback();

	         }catch(Exception e){
	        	 e.printStackTrace();
	       		System.out.println("I am the ringing response");
	       		System.out.println(e);

	       	}
	    }else
	    	ui.getTabPanel().getMainPanel().showMessages.append(" Response unexpected: ignoring\n");
	  }

/** Hands back a circuit
      */

public void handback() {
	   try{
           RfHandbackRequest request = new RfHandbackRequest(pbx,circuit);

          seqno = request.getSeqno();

         submit(request);

         ui.getTabPanel().getMainPanel().showMessages.append("HANDBACK request submitted to PBX seqno="+seqno+" circuit="+circuit+"..\n");
          handback=true;

          synchronized(this) {
		      this.wait(TIMEOUT_RESPONSE);
		    }
	   }catch(Exception e){
		   handback=false;
		   ui.getTabPanel().getMainPanel().showMessages.append("Handback request failed..\n");
	   }


}
}