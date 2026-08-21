// Team: Ron Coleman
// Date: 2005/04/06
// Vers: 1.0
// File: KEntry.java

package vista.test.star;

/** Class K main sequence entry bound for the space.

    @author R. Coleman, Ph.D. */
public class KEntry extends MainSequenceEntry {
  /** True if system has solar flares lethal for earth-type lifeforms. */
  public Boolean lethalFlares;

  /** Constructor. */
  public KEntry( ) {
    super("K");
  }

  /** Constructor.
      @param features Star features. */
  public KEntry(Features features) {
    super("K",features);
  }

  /** Constructor.
      @param features Star features.
      @param lethalFlares Flag of lethal solar flare property. */
  public KEntry(Features features,boolean lethalFlares) {
    super("K",features);

    this.lethalFlares = new Boolean(lethalFlares);
  }
}