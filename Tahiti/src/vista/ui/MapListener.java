// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: MapListener.java

package vista.ui;

import vista.object.Cell;

/** This interface is used by space map to dispatch clicks.

    It is not a <em>true</em> listener in the sense that Swing doesn't
    invoke it directly. Rather it is invoked indirectly by CellListener.

    @see vista.ui.CellListener

    @author Ron Coleman, Ph.D.
*/
public interface MapListener {

  /** This is invoked if user clicks cell.
      @param cell user clicks. */
  public void actionPerformed(Cell cell);
}