// Team: Ron Coleman
// Date: 2005/04/06
// Vers: 1.0
// File: MainSequenceEntry.java

package vista.test.star;

/** Main sequence entry bound for the space.

    @author R. Coleman, Ph.D. */
public class MainSequenceEntry extends StarEntry {
  /** Main sequence type: A, F, G, K, or M */
  public String type;

  /** Main sequence category: 0 is higest temp, 10 lowest. */
  protected Integer category;

  /** Constructor (default). */
  public MainSequenceEntry( ) {
  }

  /** Constructor.
      @param type Star type */
  public MainSequenceEntry(String type) {
    this.type = type;
  }

  /** Constructor.
      @param type Main sequence type.
      @param category Main sequence category. */
  public MainSequenceEntry(String type,Integer category) {
    this.type = type;

    this.category = category;
  }

  /** Constructor.
      @param type Star type.
      @param features Star features. */
  public MainSequenceEntry(String type,Features features) {
    super(features);

    this.type = type;
  }

  /** Constructor.
      @param type Main sequence type.
      @param category Main sequence category.
      @param features Star features. */
  public MainSequenceEntry(String type,Integer category,Features features) {
    super(features);

    this.type = type;

    this.category = category;
  }

  /** Get the main sequence category.
      @return Category. */
  public Integer getCategory() {
    return category;
  }

  /** Set the main sequence category.
      @param category Main sequence category. */
  public void setCategory(Integer category) {
    this.category = category;
  }
}