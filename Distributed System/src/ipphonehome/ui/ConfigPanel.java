//Team: Lanikai
//Date: 2005/10/24
//Vers: 1.0
//File: ConfigPanel.java

package ipphonehome.ui;
import javax.swing.*;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.ButtonGroup;
import javax.swing.JSeparator;

/** Contains all the components for Configuration Panel.
@author Lanikai
@see GriddedPanel 
*/

public class ConfigPanel extends GriddedPanel  {
	/**The analog slider
	*/
	public JSlider analvolSlider;
	
	/**checkbox for mute button
	*/
	public JCheckBox muteButton;
	
	/**Radiobutton for mode selection for PCM 
	*/
	public JRadioButton pcmButton;
	/**Radiobutton for mode selection  for GSM
	*/
	public JRadioButton gsmButton;
	
	/**Checkbox for blockid
	*/
	public JCheckBox blockidButton;
	
    /**	Checkbox for busybutton
	*/
	public JCheckBox busyButton;

    /**
     * 
     *Labels for volume.
	*/
	public JLabel vol;
	/**
     * 
     *Labels for mute
	*/
    public JLabel mute;
    /**
     * 
     *Labels for mode
	*/
    public JLabel mode;
    /**
     * 
     *Labels for pcmmode
	*/
    public JLabel pcmmode;
    /**
     * 
     *Labels for gsmmode
	*/
    public JLabel gsmmode;
    /**
     * 
     *Labels for blockid.
	*/
    public JLabel blockid;
    /**
     * 
     *Label for busy.
	*/
    public JLabel busy;
    /**
     * Radio button group
     */
    public ButtonGroup group;
   
    /**
     * Constructor
     *
     */
	public  ConfigPanel(IpPhoneHomeUI ui){
	super();
	GriddedPanel configGridPanel = new GriddedPanel();
	

     vol=new JLabel("Volume");
     configGridPanel.addComponent(vol,0,0);
     analvolSlider= new JSlider();
     analvolSlider.setBounds(20, 30, 60, 40);
     analvolSlider.setMajorTickSpacing(20);
     analvolSlider.setMinorTickSpacing(5);
     analvolSlider.setPaintTicks(true);
     analvolSlider.setPaintLabels(true);
     configGridPanel.addComponent(analvolSlider,0,1);
     JSeparator js=new JSeparator(JSeparator.HORIZONTAL);
     analvolSlider.add(js); 
     analvolSlider.addChangeListener(new VolumeListener(ui));

 

     mute=new JLabel("Mute");
     configGridPanel.addComponent(mute,1,0);
     muteButton=new JCheckBox();
     configGridPanel.addComponent(muteButton,1,1); 
     muteButton.addItemListener(new MuteButtonListener(ui));
  
 
    group = new ButtonGroup();
    mode=new JLabel("Mode:");
    configGridPanel.addComponent(mode,2,0);
 
    pcmButton=new JRadioButton("PCM");
    pcmButton.setHorizontalTextPosition(SwingConstants.LEFT );
    pcmButton.setSelected(true);
    pcmButton.setActionCommand("MODE_PCM");
    group.add(pcmButton);
    configGridPanel.addComponent(pcmButton,2,1);
 
  
    gsmButton=new JRadioButton("GSM");
    gsmButton.setHorizontalTextPosition(SwingConstants.LEFT );
    gsmButton.setActionCommand("MODE_GSM");
    group.add(gsmButton);
    configGridPanel.addComponent(gsmButton,3,1);
 

    blockid=new JLabel("Block ID");
    configGridPanel.addComponent(blockid,4,0);
    blockidButton=new JCheckBox();
    configGridPanel.addComponent(blockidButton,4,1); 

    busy=new JLabel("Busy");
    configGridPanel.addComponent(busy,5,0);
    busyButton=new JCheckBox();
    configGridPanel.addComponent(busyButton,5,1);
 
    addAnchoredComponent(configGridPanel,2,0,GriddedPanel.C_WEST);
}
	/**
	 * It returns the buttongroup
	 * @return ButtonGroup
	 */
 public ButtonGroup getGroup(){
	 return group;
 }
}