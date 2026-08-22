// Team: Ron Coleman
// Date: 2005/04/06
// Vers: 1.0
// File: MEntry.java

package vista.test.star;

/** Class M main sequence entry bound for the space.

    @author R. Coleman, Ph.D. */
public class MEntry extends MainSequenceEntry {
  /** True if system creates planetoid rotational lock. */
  public Boolean rotationalLock;

  /** Constructor. */
  public MEntry( ) {
    super("M");
  }

  /** Constructor.
      @param features Star features. */
  public MEntry(Features features) {
    super("M",features);
  }

  /** Constructor.
      @param features Star features.
      @param rotationalLock Flag of rotation lock property. */
  public MEntry(Features features,boolean rotationalLock) {
    super("M",features);

    this.rotationalLock = new Boolean(rotationalLock);
  }
}