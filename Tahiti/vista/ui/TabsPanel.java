// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: TabsPanel.java

package vista.ui;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;

/** This class contains all other tabbed sub-panels.

    @author Ron Coleman, Ph.D.
*/
public class TabsPanel extends JTabbedPane {
  /** Map panel containing the space map of live entries */
  private MapPanel mapPanel = new MapPanel( );

  /** Spids panel containing the list of live entries */
  private SpidsPanel spidsPanel = new SpidsPanel( );

  /** Clids panel containing the list of live entries */
  private ClidsPanel clidsPanel = new ClidsPanel( );

  /** Grave panel containing the list of dead entries */
  private GravePanel gravePanel = new GravePanel( );
  
  /** Statistics panel containing the list of statistics */
  private StatsPanel statsPanel = new StatsPanel( );
  /** Constructor */
  public TabsPanel( ) {
    super( );

    // Add each subpanel as a separate tab with tooltips.
    addTab("Map",null, mapPanel,"Select to see space map.");

    addTab("Clids",null,clidsPanel,"Select to see all live clids.");

    addTab("Spids",null, spidsPanel,"Select to see all live entries by spid.");

    addTab("Stats",null,statsPanel,"Select to see global statistics.");

    addTab("Graveyard",null,gravePanel,"Select to see dead entries.");
      
  }

  /** Gets the map panel.
      @return Map panel reference. */
  public MapPanel getMapPanel( ) {
    return mapPanel;
  }

  /** Gets the spids panel.
      @return Spids panel reference. */
  public SpidsPanel getSpidsPanel( ) {
    return spidsPanel;
  }
  /** Gets the clids panel.
  @return Clids panel reference. */
  public ClidsPanel getClidsPanel( ) {
  return clidsPanel;
  }
  
  /** Gets the graveyard panel.
  @return Graveyard panel reference. */
  public GravePanel getGravePanel( ) {
  return gravePanel;
  }
  /** Gets the statistics panel.
  @return Statistics panel reference. */
  public StatsPanel getStatsPanel( ) {
  return statsPanel;
  
  
  } 
}