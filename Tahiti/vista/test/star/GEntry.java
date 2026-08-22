// Team: Ron Coleman
// Date: 2005/04/06
// Vers: 1.0
// File: GEntry.java

package vista.test.star;

/** Class G main sequence entry bound for the space.

    @author R. Coleman, Ph.D. */
public class GEntry extends MainSequenceEntry {
  /** True if this system habitable, false if not, null don't care. */
  public Boolean habitable;

  /** Constructor. */
  public GEntry( ) {
    super("G");
  }


  /** Constructor.
      @param features Star features. */
  public GEntry(Features features) {
    super("G",features);
  }

  /** Constructor.
      @param features Star features.
      @param habitable Flag of habitable property. */
  public GEntry(Features features,boolean habitable) {
    super("G",features);

    this.habitable = new Boolean(habitable);
  }
}