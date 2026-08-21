//Team: Lanikai
//Date: 2005/10/24
//Vers: 1.0
//File: MainPanel.java

package ipphonehome.ui;
import javax.swing.*;


/** The main panel allows user to logon/logoff ,dial/hangup ,answer/hangup and see sip
 * messages.
 @author Lanikai
*/

public class MainPanel extends GriddedPanel  {
  /**
   *
   *  To take sip server address
   */
	public JTextField sipServerAdd;
    /**
	*To take sip endpoint address
	*/

	public JTextField sipEndpointAdd;
	/**
	*To see the caller address
	*/
	public JTextField sipCallerEndpointAdd;

	/**
	 *
	 *To see the messages/
	*/
	public JTextArea showMessages;

	/**
	 * Button for logon/logoff
	*/
	public JButton logonoff;

   /**
    *
    *	Button for dial/hangup
	*/
	public JButton dialHang;

   /**	Button for answer/hangup
	*/
	public JButton answer;

	/**
	 *
	 *Labels for sip server address
	 */
	public JLabel sipServer;
	/**
	 *
	 *Labels for sip endpoint address
	 */
	public JLabel sipUserAdd;
	/**
	 *
	 *Labels for sip messages
	 */
	public JLabel sipMessages;
	/**
	 *
	 *Labels for sip caller address
	 */
	public JLabel sipCallerAdd;
	/**
	 *
	 *Checkbox for sip show messages checkbox
	 */
	public JCheckBox showMessageBox;
	/**
	 *
	 *Jscrollpane for sip show messages scrollpane
	 */
	public JScrollPane jshowmessages;

	/** Constructor. */

	public MainPanel(IpPhoneHomeUI ui ) {
    super( );

    GriddedPanel firstPanel = new GriddedPanel( );

    sipServer = new JLabel("SIPlet Server         ");
    firstPanel.addComponent(sipServer,0,0);
    sipServerAdd= new JTextField(20);

    firstPanel.addComponent(sipServerAdd,0,1);
    logonoff= new JButton(" Logon ");
    logonoff.addActionListener(new LogonoffListener(ui));
    firstPanel.addComponent(logonoff,0,2);

    sipUserAdd = new JLabel("SIP User Address");
    firstPanel.addComponent(sipUserAdd,1,0);
    sipEndpointAdd= new JTextField(20);

    firstPanel.addComponent(sipEndpointAdd,1,1);
    dialHang= new JButton("   Dial    ");
    dialHang.setEnabled(false);
    dialHang.addActionListener(new DialHangupListener(ui));
    firstPanel.addComponent(dialHang,1,2);

    sipCallerAdd = new JLabel("SIP Caller Address");
    firstPanel.addComponent(sipCallerAdd,2,0);
    sipCallerEndpointAdd= new JTextField(20);
    sipCallerEndpointAdd.setEditable(false);

    firstPanel.addComponent(sipCallerEndpointAdd,2,1);
    answer= new JButton("Answer");
    answer.setEnabled(false);
    answer.addActionListener(new AnsHangupListener(ui));
    firstPanel.addComponent(answer,2,2);


    sipMessages = new JLabel("Show Sip Messages");
    firstPanel.addComponent(sipMessages,3,0);

    GriddedPanel secondPanel =new GriddedPanel();
    showMessages= new JTextArea(5,30);
     jshowmessages=new JScrollPane(showMessages);

    secondPanel.addComponent(jshowmessages,4,0,470,300);
    showMessageBox=new JCheckBox();
    showMessageBox.setSelected(true);
    showMessageBox.addItemListener(new ShowMessageListener(ui));
    firstPanel.addComponent(showMessageBox,3,1);
    addAnchoredComponent(firstPanel,2,0,GriddedPanel.C_WEST);
    addAnchoredComponent(secondPanel,3,0,GriddedPanel.C_WEST);
}

	/** Gets the button on this subpanel.
      @return "Logon" or "Logoff" (same) button.   */

    public JButton getLogonButton() {
      return logonoff;
}
    /** Gets the button on this subpanel.
    @return "dial" or "hangup" (same) button.   */
    public JButton getDialButton() {
    	return dialHang;
    }

    /** Gets the button on this subpanel.
    @return "answer" or "hangup" (same) button.   */
    public JButton getAnsButton() {
    	return answer;
    }

    /** Gets the textarea on this subpanel.
    @return textarea
    */
    public JTextArea getTextArea(){
    	return showMessages;
    }

    /** Gets the scrollpane on this subpanel.
    @return scrollpane */
    public JScrollPane getScrollPane(){
    	return jshowmessages;
    }


    /**
     * Sets the dial/hangup button to  enable/disable
     * @param value It changes the value of dialbutton
     */
    public void setDialButton(boolean value){
    	dialHang.setEnabled(value);
    }


    /**
     * Sets the answer/hangup button to  enable/disable
     * @param value It changes the value of answer and hangup button
     */
    public void setAnswerButton(boolean value){
    	answer.setEnabled(value);
    }
     /**
      * Gets the sip server address
      *
      */
	public String getSipServerAdd() {
		return sipServerAdd.getText();
	}
    /**
     * Gets the text of the other sip  endpoint address
     * @return String Other endpoint address
     */
	public String getSipEndpointAdd() {
		return sipEndpointAdd.getText();
	}

}