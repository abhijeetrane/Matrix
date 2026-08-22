// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: CellDrill.java

package vista.test;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import vista.ui.InspectDialog;
import vista.ui.MapPanel;
import vista.ui.MapListener;
import vista.object.Cell;
import vista.object.*;

/** This class unit tests the cell drill-through capability.

    @author Ron Coleman, Ph.D.
*/
public class CellDrill implements MapListener {
  /** Owner frame */
  private JFrame frame;

  /** Constructor.
      @param mp Map panel. */
  public CellDrill(JFrame frame,MapPanel mp) {
    mp.setListener(this);

    this.frame = frame;
  }

  /** Invoked if user clicks cell.
      @param cell Cell receiving click */
  public void actionPerformed(Cell cell) {
    //System.out.println("Cell "+cell+" selected. I am in Cell Drill");
     new InspectDialog(frame,cell.getEntity(),cell,false);
    //new InspectSpidUpdate(d).update();

  	//new InspectDialog(frame,cell.entity,cell,false);
    //new Worker(frame,cell).start();
  }

}

class Worker extends Thread {
	private JFrame frame;
	private Cell cell;
	public Worker(JFrame frame,Cell cell) {
		this.cell = cell;
	}
	
	public void  run() {
	    System.out.println("Cell "+cell+" selected. I am in Cell Drill");
	    InspectDialog d = new InspectDialog(frame,cell.getEntity(),cell,false);
	   		
	}
}