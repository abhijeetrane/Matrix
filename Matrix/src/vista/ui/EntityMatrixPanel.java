// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: EntityMatrixPanel.java

package vista.ui;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import vista.object.EntityLocator;
import vista.object.Heat;
import vista.tuples.UpdateMethod;
import vista.tuples.Entities;
import vista.object.Cell;
import vista.object.Entity;

/** Container of cells which renders entries in space.

    @author Ron Coleman, Ph.D.
*/
public class EntityMatrixPanel extends JScrollPane implements MapListener {
  /** Matrix size in rows */
  public final static int MATRIX_ROWS = 20;

  /** Matrix size in columns */
  public final static int MATRIX_COLS = 20;

  /** Panel width */
  private final static int WIDTH = 350;

  /** Panel height */
  private final static int HEIGHT = 300;

  /** Listener to invoke if user clicks cell */
  private MapListener listener = null;

  /* Cell dimensions which for the moment is not used. */
  private final Dimension cellDimension = new Dimension(27, 27);

  /** Cells implemented as 2D array of buttons. */
  private ExtendedJButton cells[][] = new ExtendedJButton[MATRIX_ROWS][MATRIX_COLS];

  /** Vector of entity locators which describe how to map entities into cells */
  private Vector locators;

  /** Constructor */
  public  EntityMatrixPanel( ) {

    JPanel panel = new JPanel( );

    // Set layout as box in X direction for each strip in Y direction
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

    // add the actual strips of "cells" of confirmation
    for (int j = 0; j < 20/*variables.size()*/; j++) {
      JPanel stripPanel = new JPanel();
      stripPanel.setLayout(new BoxLayout(stripPanel, BoxLayout.Y_AXIS));

      // build the strip
      for (int i = 0; i < 20/*ariables.size()*/; i++) {
        cells[i][j] = new ExtendedJButton( );

        // TODO: For some reason any one of these causes "..." to be printed
        // on the cell which seems to mean the button is not big enough.
        //cells[i][j].setMinimumSize(cellDimension);
        //cells[i][j].setMaximumSize(cellDimension);
        //cells[i][j].setPreferredSize(cellDimension);

        cells[i][j].setBackground(Heat.CLEAR);

        cells[i][j].setEnabled(false);

        cells[i][j].setFont(new Font("Courier New", Font.PLAIN, 10));
        cells[i][j].setText(fluff(""));

        // NOTE: Matrix looks better WITH borders.
        //cells[i][j].setBorder(new EmptyBorder(cells[i][j].getInsets()));

        cells[i][j].addActionListener(new CellListener(new Cell(i,j),this));



        stripPanel.add(cells[i][j]);
      }

      panel.add(stripPanel);

    }

    // Create view port and set its size (determined by trial-error)
    setViewportView(panel);

    setPreferredSize(new Dimension(WIDTH,HEIGHT));
  }

  /** Sets the actual listener to invoke if user clicks cell.
      @param listener Listener invoked if user clicks cell. */
  public void setListener(MapListener listener) {
    this.listener = listener;
  }

  /** Invoked as primary listener if user clicks cell.
      @param cell Cell clicked. */
  public void actionPerformed(Cell cell) {
    if(listener != null) {
      listener.actionPerformed(cell);
    }
  }

  /** Updates the cells.
      @param method Update method. */
  public void update(UpdateMethod method ) {
	  
	  if(this.locators == null) {
	    	System.out.println("this.Locators is null");
	  }else {
	    	System.out.println("thisLocators size is "+this.locators.size());
	  }  
	  
	  
    // Clear out the old ones so that panel is reset
    clearout(this.locators);

    // Get the new locators and map into matrix cells
    Vector locators = method.getLocators( );
    
    if(locators == null) {
    	System.out.println("Locators is null");
    }else {
    	System.out.println("Locators size is "+locators.size());
    }
    
    

    Enumeration enum2 = locators.elements();

    if(enum2 == null) {
    	System.out.println("enum2 is null");
    } 
    
    
    
    while(enum2.hasMoreElements( )) {
      EntityLocator locator = (EntityLocator) enum2.nextElement();

      int row = locator.getRow( );
      int col = locator.getCol( );

    
     
     //cells[row][col].addActionListener(new CellListener(new Cell(row,col,locator.getCell().entity),this));
     if(row < 20){ 
      cells[row][col].setBackground(locator.getHeat().getIntensity());

      cells[row][col].setToolTipText(locator.getFlyover());

      cells[row][col].setEnabled(true);

      cells[row][col].setText(fluff(locator.getLabel()));
      
      cells[row][col].setEntity(locator.getEntity());
      
      if(locator.getHeat().ishot() || locator.getHeat().iscold())
        cells[row][col].setForeground(Heat.WHITE);
    }
    }
    // Remember these locators so that we can clear them on next update
    this.locators = locators;
  }

  /** Clears out matrix of locators.
      @param locators Entity locators to clear out. */
  private void clearout(Vector locators) {
    if(locators == null)
      return;

    Enumeration enum3 = locators.elements();

    while(enum3.hasMoreElements()) {
      EntityLocator locator = (EntityLocator) enum3.nextElement( );

      int row = locator.getRow( );
      int col = locator.getCol( );
  if(row < 20){
      cells[row][col].setBackground(Heat.CLEAR);

      cells[row][col].setToolTipText("");

      cells[row][col].setEnabled(false);

      cells[row][col].setForeground(Heat.BLACK);

      cells[row][col].setText(fluff(""));
    }
    }
  }

  private final static int CELL_WIDTH = 4;

  private String fluff(String s) {
    StringBuffer t = new StringBuffer(s);

    t.reverse( );

    for(int k=CELL_WIDTH-s.length(); k > 0; k--)
      t.append(' ');

    return t.reverse( ).toString( );
  }

}

class ExtendedJButton extends JButton {
	private Entity entity;
	
	public ExtendedJButton() {
		super();
	}
	
	public ExtendedJButton(String label) {
		super(label);
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
  
	public Entity getEntity() {
		return entity;
		
	}
}
