// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: EntityLocator.java

package vista.object;

import java.awt.Point;

/** This class contains features to "locate" or place an Entity on the space map.

    @author Ron Coleman, Ph.D.
*/
public class EntityLocator {
  /** Physical cell on map */
  private Cell cell;

  /** Label for cell: either a spid or clid */
  private String label;

  /** Heat intensity */
  private Heat heat;

  /** Flyover or tooltip text which is usualls the entry class name. */
  private String flyover;
  
  private Entity entity;

  /** Constructor.
      @param cell Cell on map.
      @param label Usually spid or clid.
      @param heat Heat intensity.
      @param flyover Tooltip text, usually entry class name. */
  public EntityLocator(Entity entity,Cell cell,String label,Heat heat,String flyover) {
    this.entity = entity;
  	this.cell = cell;
    this.label = label;
    this.heat = heat;
    this.flyover = flyover;
  }

  /**
   * Get entity.
   * @return Entity
   */
  public Entity getEntity() {
  	return entity;
  }
  
  /** Get the row.
      @return Row. */
  public int getRow( ) {
    return cell.getRow();
  }

  /** Get the column.
      @return Column. */
  public int getCol( ) {
    return cell.getCol( );
  }

  /** Get the cell.
      @return Cell. */
  public Cell getCell( ) {
    return cell;
  }

  /** Get the label.
      @return Label. */
  public String getLabel( ) {
    return label;
  }

  /** Get the heat intensity.
      @return Heat intensity. */
  public Heat getHeat( ) {
    return heat;
  }

  /** Get the flyover text.
      @return Flyover text. */
  public String getFlyover( ) {
    return flyover;
  }
}