// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: Cell.java

package vista.object;
import vista.object.*;

/** This class represents an abstract cell object in the space map.

    @author Ron Coleman, Ph.D.
*/
public class Cell {
  /** Row */
  private int row;

  /** Column */
  private int col;

  private Entity entity;

  /** Constructor.
      @param row Map row.
      @param col Map column. */
  public Cell(int row,int col) {
    this.row = row;
    this.col = col;
  }

  /**
   * Get the entity.
   * @return Entity
   */
  public Entity getEntity() {
  	return entity;
  }
  
  /**
   * Set the entity.
   * 
   */
  public void setEntity(Entity entity) {
  	this.entity = entity;
  }
  
  /** Get the row.
      @return Row. */
  public int getRow( ) {
    return row;
  }

  /** Get the column.
      @return column. */
  public int getCol( ) {
    return col;
  }

  /** Convert cell to string.
      @return String representing the cell. */
  public String toString() {
    return "("+row+","+col+")";
  }


  /** Tests if object is equal to cell.
      @param object Object to test.
      @return true if object is a Cell and row / column are equal. */
  public boolean equals(Object object) {
    if(object instanceof Cell) {
      Cell cell = (Cell) object;
      return this.row == cell.row && this.col == cell.col;
    }

    return super.equals(object);
  }

  /** Returns hash code for cell.
      @return Cell hash code. */
  public int hashCode( ) {
    return row * vista.ui.EntityMatrixPanel.MATRIX_COLS + col;
  }
}