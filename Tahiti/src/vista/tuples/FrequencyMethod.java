// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: FrequencyMethod.java

package vista.tuples;
import java.util.*;
import java.util.Vector;
import vista.object.EntityLocator;
import vista.object.Heat;
import vista.object.Cell;
import vista.object.*;
import vista.tuples.*;

/** This class implement the frequency update method.

    @author Ron Coleman, Ph.D.
*/
public class FrequencyMethod implements UpdateMethod {
  /** Set to false to suppress debug activity. */
  private final static boolean DEBUG = false;

  /** Utility counter: for used only for testing purposes */
  private int count = 0;

  /** Reference to entities. */
  protected Entities entities;

  //protected int clidindex;


  /** Constructor.
      @param entities Entities to calculate space map the locators. */
  public FrequencyMethod(Entities entities)  {
    this.entities = entities;
    //this.clidindex=clidindex;
   //System.out.println("I am in frequency method");
  }

  /** Get the locators based as a function of read frequency.
      @return Vector of locators.
      @see vista.object.EntityLocator */
  public Vector getLocators() {
    System.out.println(" I am in locators");
    Vector locators = new Vector( );
    Iterator iter = entities.getIterator();
     int size=entities.getSize();
     System.out.println("Size of item "+ size);
    for(int i= 0;i<entities.getSize();i++)
	    {

	  		Entity temp = iter.next();
	  	//	System.out.println(temp);
	  	    Long id = new Long(temp.getId());
	        //locators.add(new EntityLocator(new Cell(0,0),id.toString(),new Heat(3),temp.getEntry().getClass().getName()));
    }    // TODO: Search through the entities and calculate locators.
    // For now, however, just create test locators for demo purposes.
    /*if(DEBUG && count % 2 == 0) {
      locators.add(new EntityLocator(new Cell(0,0),"1",new Heat(3),"vista.TestEntry1"));
      locators.add(new EntityLocator(new Cell(0,1),"1",new Heat(2),"vista.TestEntry1"));
      locators.add(new EntityLocator(new Cell(0,2),"1",new Heat(2),"vista.TestEntry1"));
      locators.add(new EntityLocator(new Cell(1,0),"2",new Heat(3),"vista.TestEntry2"));
      locators.add(new EntityLocator(new Cell(1,1),"2",new Heat(2),"vista.TestEntry2"));
      locators.add(new EntityLocator(new Cell(2,0),"3",new Heat(7),"vista.TestEntry3"));
      locators.add(new EntityLocator(new Cell(2,1),"3",new Heat(5),"vista.TestEntry3"));
      locators.add(new EntityLocator(new Cell(3,0),"4",new Heat(7),"vista.TestEntry4"));
      locators.add(new EntityLocator(new Cell(3,1),"4",new Heat(7),"vista.TestEntry4"));
      locators.add(new EntityLocator(new Cell(3,2),"4",new Heat(5),"vista.TestEntry4"));
      locators.add(new EntityLocator(new Cell(4,0),"5",new Heat(10),"vista.TestEntry5"));
      locators.add(new EntityLocator(new Cell(4,1),"5",new Heat(8),"vista.TestEntry5"));
      locators.add(new EntityLocator(new Cell(4,2),"5",new Heat(7),"vista.TestEntry5"));
      locators.add(new EntityLocator(new Cell(4,3),"5",new Heat(5),"vista.TestEntry5"));
    } else if(DEBUG) {
      locators.add(new EntityLocator(new Cell(0,0),"25",new Heat(3),"vista.TestEntry6"));
      locators.add(new EntityLocator(new Cell(0,1),"25",new Heat(2),"vista.TestEntry6"));
      locators.add(new EntityLocator(new Cell(0,2),"25",new Heat(0),"vista.TestEntry6"));
      locators.add(new EntityLocator(new Cell(1,0),"49",new Heat(8),"vista.TestEntry13"));
      locators.add(new EntityLocator(new Cell(1,1),"49",new Heat(5),"vista.TestEntry13"));
      locators.add(new EntityLocator(new Cell(1,2),"49",new Heat(4),"vista.TestEntry13"));
      locators.add(new EntityLocator(new Cell(2,0),"51",new Heat(2),"vista.TestEntry17"));
      locators.add(new EntityLocator(new Cell(3,0),"128",new Heat(10),"vista.TestEntry19"));
      locators.add(new EntityLocator(new Cell(3,1),"128",new Heat(7),"vista.TestEntry19"));
      locators.add(new EntityLocator(new Cell(3,2),"128",new Heat(7),"vista.TestEntry19"));
      locators.add(new EntityLocator(new Cell(3,3),"128",new Heat(0),"vista.TestEntry19"));
      locators.add(new EntityLocator(new Cell(4,0),"1002",new Heat(5),"vista.TestEntry12"));
      locators.add(new EntityLocator(new Cell(4,1),"1002",new Heat(3),"vista.TestEntry12"));
      locators.add(new EntityLocator(new Cell(4,2),"1002",new Heat(2),"vista.TestEntry12"));
    }
*/
    ++count;

    return locators;
  }

}