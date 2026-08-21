//Team: Lanikai
//Date: 2005/12/05
//Vers: 1.0
//File: MuteButtonListener.java

package ipphonehome.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
/**
It mutes the player
*/

public class MuteButtonListener implements ItemListener {
 private IpPhoneHomeUI ui;

 /** Constructor.

 @param ui Reference to the Sip endpoint UI.
 @see IpPhoneHomeUI
*/

	public MuteButtonListener(IpPhoneHomeUI ui){
		this.ui=ui;
	}

	/** Mute button actions for PCM and GSM Player. */

	public void itemStateChanged(ItemEvent ie){
		Object source = ie.getItem();
	    if (source.equals(ui.getTabPanel().getConfigPanel().muteButton))
	    {
	       int select = ie.getStateChange();
	       if(ui.endpointsiplet.rtpcommunicator!=null){
	       if (select == ItemEvent.SELECTED)
	       {
	    	  System.out.println("mode is"+ui.endpointsiplet.rtpcommunicator.mode);
	    	   if(ui.endpointsiplet.rtpcommunicator.mode == 0)
	                ui.endpointsiplet.rtpcommunicator.pcmplayer.mute(true);
	    	   if(ui.endpointsiplet.rtpcommunicator.mode == 1)
	    		   ui.endpointsiplet.rtpcommunicator.gsmplayer.mute(true);
	   	   }else if(select== ItemEvent.DESELECTED)
	   	 {
	    	   if(ui.endpointsiplet.rtpcommunicator.mode == 0)
	                ui.endpointsiplet.rtpcommunicator.pcmplayer.mute(false);
	    	   if(ui.endpointsiplet.rtpcommunicator.mode == 1)
	    		   ui.endpointsiplet.rtpcommunicator.gsmplayer.mute(false);
	      }
	    }

	}
}
}
