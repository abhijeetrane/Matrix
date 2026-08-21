// Team: Ron Coleman
// Date: 2005/04/06
// Vers: 1.0
// File: AEntry.java

package vista.test.star;

/** Class A main sequence entry bound for the space.

    @author R. Coleman, Ph.D. */
public class AEntry extends MainSequenceEntry {

  /** Constructor. */
  public AEntry( ) {
    super("A");
  }

  /** Constructor.
      @param features Star features. */
  public AEntry(Features features) {
    super("A",features);
  }
}