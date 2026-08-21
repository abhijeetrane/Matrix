// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: Heat.java

package vista.object;

import java.awt.Color;

/** This class contains heat intensity information for space map.

    @author Ron Coleman, Ph.D.
*/
public class Heat {
  /** Color when cell has no entity */
  public static final Color CLEAR = new Color(255,255,255);

  /** Color for text foreground when background not hot or cold. */
  public static final Color BLACK = new Color(0,0,0);

  /** Color for text foreground when background is hot. */
  public static final Color WHITE = new Color(255,255,255);

  /** Top hot intensities */
  protected static final int HOT_TOP = 3;

  /** Table map of heat intensities */
  protected static final Color INTENSITIES[] = {
    new Color(  0, 153, 255),  // blue-ice cold
    new Color(  0, 255, 255),
    new Color(153, 255, 204),
    new Color(153, 255, 153),
    new Color(204, 255, 153),
    new Color(255, 255, 153),
    new Color(255, 255, 102),
    new Color(255, 255,  51),
    new Color(255,  51,  51),
    new Color(255,   0,   0),  // red hot
    new Color(  0,   0,   0)   // black hole hot
  };

  /** Intensity (index) for this heat object. */
  private int index;

  /** Constructor (initial intensity is cold) */
  public Heat( ) {
    index = 0;
  }

  /** Constructor.
      @param index Heat intensity index. */
  public Heat(int index) {

    if(index < 0)
      this.index = 0;

    else if(index >= INTENSITIES.length)
      this.index = INTENSITIES.length - 1;

    else
      this.index = index;
  }

  /** Get current RGB intensity.
      @return RGB intensity. */
  public Color getIntensity( ) {
    return INTENSITIES[index];
  }

  /** Test whether intensity is hot which is not same as NOT(cold).
      @return true if intensity is hot. */
  public boolean ishot( ) {
    return (index >= INTENSITIES.length-HOT_TOP) ? true : false;
  }

  /** Test whether intensity is cold which is not same as NOT(hot).
      @return true if intensity is cold. */
  public boolean iscold( ) {
    return index == 0;
  }

  /** Get RGB intensity for a given intensity index.
      @param index Intensity index.
      @return RGB intensity. */
  public static Color getIntensity(int index) {
    if(index >= INTENSITIES.length)
      return INTENSITIES[INTENSITIES.length-1];

    else if(index < 0)
      return INTENSITIES[0];

    return INTENSITIES[index];
  }

  /** Count intensities in intensity scale.
      @return Number of intensity indices, starting at 0 */
  public static int countIntensities( ) {
    return INTENSITIES.length;
  }
}