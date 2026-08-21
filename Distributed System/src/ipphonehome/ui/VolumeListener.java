//Team: Lanikai
//Date: 2005/12/05
//Vers: 1.0
//File: VolumeListener.java

package ipphonehome.ui;

import javax.swing.JSlider;
import javax.swing.event.*;

/** Listener for the Volume slider.

@author Lanikai 

*/

public class VolumeListener implements ChangeListener {
	IpPhoneHomeUI ui;
	
/** Constructor.

    @param ui Reference to the Sip endpoint UI.
    @see IpPhoneHomeUI
*/
	
	public VolumeListener(IpPhoneHomeUI ui){
		this.ui=ui;
	}
	
	/**
	 * Listen to the slider
	 * @param e Change event
	 */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            int fps = (int)source.getValue();
            System.out.println(fps-100.0);
            if(ui.endpointsiplet.rtpcommunicator!=null){
            	if(ui.endpointsiplet.rtpcommunicator.mode==0){
            	if((fps-100.0+6.0206+40.0) <-80.0)
            		ui.endpointsiplet.rtpcommunicator.pcmplayer.setVolume(-80.0);
            	else if((fps-100.0+6.0206+40.0) >6.0206)
            	  ui.endpointsiplet.rtpcommunicator.pcmplayer.setVolume(6.0206);
            	else
            		ui.endpointsiplet.rtpcommunicator.pcmplayer.setVolume(fps-100.0+6.0206+40.0);
            	}else{
            		if((fps-100.0+6.0206+40.0) <-80.0)
                		ui.endpointsiplet.rtpcommunicator.gsmplayer.setVolume(-100.0);
                	else if((fps-100.0+6.0206+40.0) >6.0206)
                	  ui.endpointsiplet.rtpcommunicator.gsmplayer.setVolume(6.0206);
                	else
                		ui.endpointsiplet.rtpcommunicator.gsmplayer.setVolume(fps-100.0+6.0206+40.0);
            		
            		
            	}//end of else
            
            
            }//end of main if
             
            
        }
    }

}
