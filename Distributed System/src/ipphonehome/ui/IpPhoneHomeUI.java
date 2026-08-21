//Team: Lanikai
//Date: 2005/10/24
//Vers: 1.0
//File: IpPhoneHomeUI.java

package ipphonehome.ui;
import java.net.URI;

import java.util.Date;
import jroll.sip.*;
import rfly.sip.*;
import rfly.sip.request.*;
import rfly.sip.response.*;
import rfly.sip.circuit.RfCircuit;
import javax.swing.*;
import java.awt.*;
/**
 *  This class implements the sip endpoint UI.
  @author Lanikai
*/

public class IpPhoneHomeUI extends JFrame {
	/**
	 * It is the siplet of this user
	 */
	 protected  EndpointSiplet endpointsiplet;
	 /**
	  * It is the server address
	  */
	 protected URI servlet;
	 /**
	  * It is the address of the other endpoint
	  */
	 protected URI otherendpoint;

	 /** Formatter for SIP messages */
	  static protected java.text.SimpleDateFormat formatter =
	      new java.text.SimpleDateFormat ("hh:mm:ss a");
	/*
	 * Menu for save,clear messages and exit.
	 */
	private IPMenu ipMenu;
	/*
	 * Tabpanel for adding panels.
	 */
	private TabsPanel tabPanel = new TabsPanel(this );
	/**
	 * Constructor.
	 *
	 */
	public IpPhoneHomeUI(){

		super("IP Phone Home v1.0 - Lanikai");

		ipMenu = new IPMenu(this);
		setJMenuBar(ipMenu);
		GriddedPanel frontPanel = new GriddedPanel();
		frontPanel.addComponent(tabPanel,1,1);
		JPanel topPanel = new JPanel();
	    topPanel.add(frontPanel);
        //Add the top panel to the content pane and size it
	    getContentPane().add(topPanel, BorderLayout.CENTER);


	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,350);
		this.setBackground(java.awt.Color.GRAY);
		this.setVisible(true);

	}

	/** Returns the frame of the UI.
    @return JFrame of the UI.
    */
    public JFrame getFrame() {
       return this;
    }

    /** Get the logon button from the UI
    @return Logon button
    */

   public JButton getLogonButton() {
       return tabPanel.getMainPanel().getLogonButton();
   }
   /** Get the Dial button from the UI
   @return Dial button
   */
   public JButton getDialButton() {
	   return tabPanel.getMainPanel().getDialButton();
   }
   /** Get the Answer button from the UI
   @return Answer button
   */
   public JButton getAnsButton() {
	   return tabPanel.getMainPanel().getAnsButton();
   }
   /** Get the sip server address from the UI
   @return Answer button
   */
   public String getSipServerAdd() {
	   return tabPanel.getMainPanel().getSipServerAdd();
   }
   /** Get the sip endpoint address from the UI
   @return Answer button
   */
   public String getSipEndpointAdd() {
	   return tabPanel.getMainPanel().getSipEndpointAdd();
   }


   /** Get the tabpanel from the UI
   @return tabpanel
   */
   public TabsPanel getTabPanel(){
	   return tabPanel;
   }
   /**
    * It is invoked when the application starts
    * @param args  no arguments.
    */
	public static void main(String [] args){
		 // Set the look and feel.
	    try {
	      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	    } catch(Exception lfe) {}
        //Put up the GUI
	    IpPhoneHomeUI ipphonehomeUI = new IpPhoneHomeUI();
        ipphonehomeUI.setVisible(true);

	}
/**
 * It gives the endpoint siplet
 * @return EndpointSiplet
 */
public EndpointSiplet getEndpointsiplet() {
	System.out.println(endpointsiplet);
	return endpointsiplet;
}
/** Gives the date in particular format
 */
public  String datemessage() {
   Date now = new Date();

        return formatter.format(now);



}


}
