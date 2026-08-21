// Team: Lanikai 
// Date: 2005/10/21
// Vers: 1.0
// File: TabsPanel.java

package ipphonehome.ui;

import javax.swing.JTabbedPane;


/** This class contains all other tabbed sub-panels.

    @author Lanikai
*/
public class TabsPanel extends JTabbedPane {
  /** Main panel containing the basic functionality */
  private MainPanel mainPanel ;

  /** Configuration panel containing the tools to modify the system*/
  private ConfigPanel configPanel;


  /** Constructor */
  public TabsPanel(IpPhoneHomeUI ui ) {
    super( );
    mainPanel = new MainPanel(ui );
    configPanel = new ConfigPanel(ui);
    // Add each subpanel as a separate tab with tooltips.
    addTab("Main",null, mainPanel,"Select to see Main Panel");

    addTab("Configuration",null,configPanel,"Select to see Configuration Panel");
  
      
  }

  /** Gets the main panel.
      @return Main panel reference. */
  public MainPanel getMainPanel( ) {
    return mainPanel;
  }

  /** Gets the config panel.
      @return Config panel reference. */
  public ConfigPanel getConfigPanel( ) {
    return configPanel;
  }
  
  
  
  } 
