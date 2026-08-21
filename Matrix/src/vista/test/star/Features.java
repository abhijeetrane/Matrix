// Team: Ron Coleman
// Date: 2005/04/06
// Vers: 1.0
// File: Features.java

package vista.test.star;

/** Solar features which can be contained by a space Entry.

    @author R. Coleman, Ph.D. */
public class Features implements java.io.Serializable {
  /** Surface temperature */
  public Double temp;

  /** Mass */
  public Double mass;

  /** Brightness or luminosity */
  public Double luminosity;

  /** Lifespace (billions of years) */
  public Double lifespan;

  /** Constructor. */
  public Features( ) {
  }

  /** Constructor.
      @param temp Surface temperature.
      @param mass Mass.
      @param luminosity Luminosity.
      @param lifespace Lifespace. */
  public Features(double temp,double mass,double luminosity,double lifespan) {
    this.temp = new Double(temp);
    this.mass = new Double(mass);
    this.luminosity = new Double(luminosity);
    this.lifespan = new Double(lifespan);
  }

  public String toString( ) {
    return "( "+temp +", "+
               mass +", "+
               luminosity +", "+
               lifespan +" )";
  }
}