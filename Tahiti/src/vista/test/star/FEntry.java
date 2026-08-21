// Team: Ron Coleman
// Date: 2005/04/06
// Vers: 1.0
// File: FEntry.java

package vista.test.star;

/** Class F main sequence entry bound for the space.

    @author R. Coleman, Ph.D. */
public class FEntry extends MainSequenceEntry {
  /** True if this system is sun-like, false if not, null don't care. */
  public Boolean sunlike;

  /** Constructor. */
  public FEntry( ) {
    super("F");
  }

  /** Constructor.
      @param features Star features. */
  public FEntry(Features features) {
    super("F",features);
  }

  /** Constructor.
      @param features Star features.
      @param sunlike Flag of sun-like property. */
  public FEntry(Features features,boolean sunlike) {
    super("F",features);

    this.sunlike = new Boolean(sunlike);
  }
}