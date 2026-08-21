// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: LegendPanel.java

package vista.ui;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import vista.object.Heat;

/** This class contains the heat intensity legend.

    It is a sub-panel of Map panel.

    @author Ron Coleman, Ph.D.
*/
public class LegendPanel extends GriddedPanel
{
  /** Font point size 11 */
  private final static int PT_SIZE_11 = 11;

  /** Font point size 9 */
  private final static int PT_SIZE_9 = 9;
  public LegendPanel( ) {
    // Set up new font and border using dummy button
    JButton dummy = new JButton("");
    Font f = dummy.getFont();

    Font fontNew = new Font(f.getFontName(), f.getStyle(), PT_SIZE_11);
    Font fontNewSmall = new Font(f.getFontName(), f.getStyle(), PT_SIZE_9);

    // Set layout as box in Y direction for strip (see below) in X direction
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Create legend label on top
    JLabel masterPanelLabel = new JLabel("Legend");

    masterPanelLabel.setFont(fontNew);
    masterPanelLabel.setForeground(Color.black);
    masterPanelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    add(masterPanelLabel);

    // Create single strip in next row, spaning in X direction.
    JPanel legendStripPanel = new JPanel();
    legendStripPanel.setLayout(new BoxLayout(legendStripPanel, BoxLayout.X_AXIS));

    JLabel hiLabel = new JLabel("hot");
    JLabel lowLabel = new JLabel("cold");

    hiLabel.setFont(fontNewSmall);
    lowLabel.setFont(fontNewSmall);

    lowLabel.setForeground(Color.black);
    hiLabel.setForeground(Color.black);

    // (The next few Dimensions have consts we know what they do
    // but we won't try to explain!)
    legendStripPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    legendStripPanel.add(hiLabel);
    legendStripPanel.add(Box.createRigidArea(new Dimension(5, 0)));

    Dimension labelDimension = new Dimension(20, 10);

    for (int i = Heat.countIntensities( ) - 1; i >= 0; i--) {
      JButton legend = new JButton();

      legend.setBackground(Heat.getIntensity(i));

      legend.setHorizontalAlignment(legend.CENTER);

      legend.setMinimumSize(labelDimension);
      legend.setMaximumSize(labelDimension);
      legend.setPreferredSize(labelDimension);

      legend.setBorder(new EmptyBorder(legend.getInsets()));

      legendStripPanel.add(legend);
    }

    // (Ditto!)
    legendStripPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    legendStripPanel.add(lowLabel);

    add(legendStripPanel);
  }
}
