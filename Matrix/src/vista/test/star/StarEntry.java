// Team: Ron Coleman
// Date: 2005/04/06
// Vers: 1.0
// File: StarEntry.java

package vista.test.star;

/** Star entry bound for the space.

    @author R. Coleman, Ph.D. */
public abstract class StarEntry implements space.Entry {
  /** Star features. */
  public Features features;

  /** Constructor. */
  public StarEntry( ) {
  }

  /** Constructor.
      @param features Star features. */
  public StarEntry(Features features) {
    this.features = features;
  }
}