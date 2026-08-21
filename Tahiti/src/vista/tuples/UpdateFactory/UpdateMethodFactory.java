//Team: Tahiti
//Date: 2005/05/27
//Vers: 1.0
//File: UpdateMethodFactory.java
package vista.tuples.UpdateFactory;

import java.util.Vector;
import java.util.*;
import vista.object.*;
import vista.tuples.*;
import vista.util.Clid;
import vista.util.ClidTable;

/**
 * This class selects the appropriate method for map panel.
 * @author Tahiti
 *
 * 
 */
public class UpdateMethodFactory{

	/** Reference to entities. */
	  private Entities entities;
      	  
	  /** Constructor.
	  */
	  public UpdateMethodFactory() {
	    
	  };

	 /** Method returns one of 12 algorithms
	  * 
	  * @param entities LiveEntities
	  * @param Update by Frequency, MRU, Age
	  * @param View by Clid or Spid
	  * @param Organize by Intensity or not
	  * @return UpdateMethod updatemethod instance
	  */ 
     public static UpdateMethod getMethod(Entities entities, int updateby ,int viewby ,int organizeby){
             	
     	
             	if (updateby == 0 && viewby == 0  && organizeby==0)
         		{
         		
             		return new FrequencyClidIntensity(entities);
         		}
             	
             	if (updateby == 0 && viewby == 0  && organizeby==1)
         		{
         		
             		return new FrequencyClidOintensity(entities);
   
         		}
             	
             	if (updateby == 0 && viewby == 1  && organizeby==0)
         		{
         		
             		return new FrequencySpidIntensity(entities);
         		}
             	
             	if (updateby == 0 && viewby == 1  && organizeby==1)
         		{
         		
             		return new FrequencySpidOintensity(entities);
         		}
             	if (updateby == 1 && viewby == 0  && organizeby== 0)
         		{
         		
             		return new AgeClidIntensity(entities);
         		}
             	
             	if (updateby == 1 && viewby == 0  && organizeby== 1)
         		{
         		
             		return new AgeClidOintensity(entities);
         		}
             	
             	if (updateby == 1 && viewby == 1  && organizeby== 0)
         		{
         		
             		return new AgeSpidIntensity(entities);
         		}
             	
             	if (updateby == 1 && viewby == 1  && organizeby== 1)
         		{
         		
             		return new AgeSpidOintensity(entities);
         		}
             	
             	if (updateby == 2 && viewby == 0  && organizeby== 0)
         		{
         		
             		return new MRUClidIntensity(entities);
         		}
             	
             	
             	if (updateby == 2 && viewby == 0  && organizeby== 1)
         		{
         		
             		return new MRUClidOintensity(entities);
         		}
             	
             	if (updateby == 2 && viewby == 1  && organizeby== 0)
         		{
         		
             		return new MRUSpidIntensity(entities);
         		}
             	
             	if (updateby == 2 && viewby == 1  && organizeby== 1)
         		{
         		
             		return new MRUSpidOintensity(entities);
         		}
             	
             	
             	
             		return null;
     }
   
}
