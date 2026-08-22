// Team: Ron Coleman
// Date: 2005/03/09
// Vers: 1.0
// File: MapUpdate.java

package vista.test;

import vista.tuples.*;
import vista.tuples.UpdateFactory.*;
import vista.ui.MapPanel;


/** This class tests the map update.

    @author Ron Coleman, Ph.D.
*/
public class MapUpdate implements Runnable {
  /** Reference MapPanel */
  private MapPanel mp;
  private LiveEntities entities;
  private int clidindex;
  /** Constructor.
      @param mp MapPanel. */
  public MapUpdate(MapPanel mp,LiveEntities entities,int clidindex ) {
    this.mp = mp;
    this.entities= entities;
    this.clidindex=clidindex;
  }

  /** Invoked by Thread.start to start the thread */
  public void run( ) {
    try {
    	System.out.println("MapUpdate Run Method Begin");
    	 
      
      while(true) {
      	UpdateMethod method = UpdateMethodFactory.getMethod(entities,MapPanel.BY_FRE_AGE_MRU,MapPanel.BY_CLID_SPID,MapPanel.BY_INTENSITY);    
      	if(!MapPanel.freezeflag)
      	mp.update(method);
      	System.out.println("MapUpdate Run Method End");
        Thread.sleep(4000);
      }
      
      

    } catch(Exception e) {
      e.printStackTrace( );
    }
  
  }
  
  
}