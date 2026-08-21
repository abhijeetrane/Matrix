//Team: Lanikai
//Date: 2005/12/05
//Vers: 1.0
//File: RTPCommunicator.java

package ipphonehome.ui;

import java.net.URI;

import rfly.sip.circuit.RfCircuit;
import jroll.atp.GSMPlayer;
import jroll.atp.GSMRecorder;
import jroll.atp.PCMPlayer;
import jroll.atp.PCMRecorder;
import jroll.rtp.RTPThreadHandler;
import jroll.rtp.Session;
import jroll.rtp.packets.*;
import jroll.sip.SipCaller;
/**
 * This class handles the talking and listening
 *
 */
public class RTPCommunicator implements Runnable ,RTP_actionListener  {

	
	
 /**
  * It play pcm files
  */
	public PCMPlayer pcmplayer= new PCMPlayer();
	private PCMRecorder pcmrecorder = new PCMRecorder();
/**
 * It plays the gsm files
 */
	public GSMPlayer gsmplayer= new GSMPlayer();
	private GSMRecorder gsmrecorder = new GSMRecorder();
   /**
    * It checks if the session is active
    */
	protected boolean sessionactive=false;
    /**
     * It creates the RTP session
     */
	protected Session rtpSession;
   
    static int rindex=0;
    static byte[] rbuff = new byte[3000];
    /**
     * It checks if the mode is PCM or GSM
     */
    public int mode;
    private IpPhoneHomeUI ui;
    
    /**
     * Constructor
     * @param ui The actual ui
     * @param circuit The circuit for handling connection
     * @throws Exception
     */
  public RTPCommunicator(IpPhoneHomeUI ui,RfCircuit circuit) throws Exception {
    // multicastIpAddress == "234.5.6.7" works
    this.ui=ui;
	  

	  
	  mode = circuit.getType();

	  String address = circuit.getIPHost();
	  int groupPort = circuit.getPort(RfCircuit.Port.SEND_TO);
      int controlPort = circuit.getPort(RfCircuit.Port.CONTROL_SEND_TO);
      int sendFromPort = circuit.getPort(RfCircuit.Port.SEND_FROM);
      int controlSendFromPort = circuit.getPort(RfCircuit.Port.CONTROL_SEND_FROM);

      rtpSession =
        new Session(address,
                    groupPort,
                    controlPort,
                    sendFromPort,
                    controlSendFromPort,
                    0);

      ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" RTP session created.\n");
    rtpSession.addRTP_actionListener(this);

    ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" RTP listener registered.\n");
    

    

    URI myuri = SipCaller.getURI();
   
    rtpSession.setCName (myuri.toString());
    rtpSession.EnableLoopBack=false;
   
    // Start the session
   
    //sessionactive=true;
    
    rtpSession.Start() ;
    ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" RTP session started\n");
    try{
	       Thread.sleep(500);
   }catch(Exception e){
	   System.out.println("I am after thread");
	   e.printStackTrace();
   }
  }
  /**
   * It executes the thread functionality
   */
  public void run(){
   
    try{
    if(mode==0){
    int pktsize = pcmrecorder.getFormat().getFrameSize();
    int index=0;
    

   
    
    byte[] packet = new byte[pktsize];
    byte[] sbuff = new byte[500];
    
    while(ui.endpointsiplet.talking) {
    	pcmrecorder.capture(packet);
    	if(index<500){
    		sbuff[index++] = packet[0];
    		sbuff[index++] = packet[1];
    	}
    	else{
    		index =0;
    		if(sbuff!=null)
    		 rtpSession.SendRTPPacket(sbuff);
    		Thread.sleep(25);
    		//System.out.println("RTP packet sent");
    	}
    	
    	
     }
    }else{
    	int pktsize = 500;
        
        byte[] packet = new byte[pktsize];
       
        while(ui.endpointsiplet.talking) {
        	gsmrecorder.capture(packet);
        	rtpSession.SendRTPPacket(packet);
        	Thread.sleep(25);
        	//System.out.println("RTP packet sent");
        	
         }

    }
    }catch(Exception w){
    	w.printStackTrace();
    }
    
   
    
    // Stop the session
    
    rtpSession.Stop();

    ui.getTabPanel().getMainPanel().showMessages.append(ui.datemessage()+" RTP session stopped.\n");
  }

/**
 * It receives the events 
 */
  
  public void handleRTPEvent(RTPPacket rtppkt) {
   // System.out.println("rtp packet received"+rindex);
	  if(rtppkt!=null){
    try{
    	if(mode==0){
    	
    	    	    
    	     if(rindex < 3000){
    	    	for(int i=0;i<rtppkt.data.length;i++)
    	    	 	rbuff[rindex++] = rtppkt.data[i];
    	    		
    	    	}else
    	    	{
    	    		rindex=0;	
    	    		pcmplayer.play(rbuff);
    	    	}
    	    	
    	}else{
    		//System.out.println("I am in GSM playback");
    	      	gsmplayer.play(rtppkt.data);
   	    	}	
    	
    	    		
    	    	
    	    	
    	     
    }catch(Exception e1){
    System.out.println("I am in rtpevent");
    }
	  }
  }
}

