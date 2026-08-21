// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: CellListener.java

package vista.ui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import vista.object.Cell;

/** This class is a TRUE button listener.

    It invoked a MapListener.

    @see vista.ui.MapListener

    @author Ron Coleman, Ph.D.
*/
public class CellListener implements ActionListener {
  /** Map listener */
  private MapListener ml;

  /** Reference cell */
  private Cell cell;

  /** Constructor.
      @param cell Cell.
      @param ml MapListener.
      @see vista.ui.MapListener */
  public CellListener(Cell cell,MapListener ml) {
    this.cell = cell;
    this.ml = ml;
  }

  /** Invoked by Swing when cell button clicked.
      @param event Event information. */
  public void actionPerformed(ActionEvent event) {
  	ExtendedJButton ejb = (ExtendedJButton)event.getSource();
  	
  	cell.setEntity(ejb.getEntity());
    ml.actionPerformed(cell);
  }
}