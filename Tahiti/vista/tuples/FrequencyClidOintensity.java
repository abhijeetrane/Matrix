//Team: Tahiti
//Date: 2005/05/27
//Vers: 1.0
//File: FrequencyClidOintensity.java



package vista.tuples;
import vista.util.*;
import vista.object.*;

import java.util.*;
import java.lang.*;
/** This class implement the Frequency Clid Organize by Intensity algorithm.

@author Tahiti.
*/
public class FrequencyClidOintensity implements UpdateMethod{
	private Vector vector = null;
    Entities entities = null; 
	


	/**
	 * Constructor
	 *
	 */
	public FrequencyClidOintensity(Entities entities){
   this.entities=entities;
	
 	
 	}
    public Vector getLocators(){
    	synchronized(entities){
    	int i=0;
    	int sizeClid ,sizeEntities;
    	int count=0;
    	Vector locators = new Vector( );
        IdIntensity clidintensity[] = new IdIntensity[entities.getSize()];




    

    	
    	sizeEntities = entities.getSize();

    	



        for(int nr=0;nr<entities.getSize();nr++){
		clidintensity[nr] = new IdIntensity(new Integer(nr),new Double(nr),new Long((long)nr));
		
		}



         long X[]=new long[entities.getSize()];
         Integer Id;
        Iterator iterentities = entities.getIterator();

      	for( i= 0;i<entities.getSize();i++){
       	Entity tempentity =iterentities.next();
        clidintensity[i].id=Entities.clidtab.get(tempentity.getEntry()).getId();
        
        clidintensity[i].spid=new Long(tempentity.getId());
        
  

       		X[i]=tempentity.getReads();
       	  count++;
       	 
}

       

        

        long R[]=new long[count];
        
        if(count>0){

        R= IdIntensity.getIntensity(X,count);

        

        for(int t=0;t<count;t++){
			 
			 clidintensity[t].intensity=new Double((double)R[t]);
             
        }
}









        Arrays.sort(clidintensity,new Comparator(){
        	public int compare(Object o1, Object o2)
            {
        		double id1=((IdIntensity) o1).intensity.doubleValue();
        		double id2=((IdIntensity) o2).intensity.doubleValue();
        		
        		if(id1 > id2)
        			return -1;
        		else if(id1 <id2)
        			return 1;
        		else return (((IdIntensity) o1).id.intValue()- ((IdIntensity) o2).id.intValue());
            }		 
                
            
        });
    
		 
		        int row=0,col=-1;
		           
		           
		        
		        for(i=0;i<count;i++){
		        	Double intensity =null;
		        	if(i==0)
		            intensity=clidintensity[0].intensity;
		        	else
		        	intensity=clidintensity[i-1].intensity;	
		        /*	if(col>=19) {
		        		row++;
		        	    col=-1;
		        	}*/ 
		        	if((clidintensity[i].intensity.doubleValue()==intensity.doubleValue())&& (col < 19)){
		        		 col++;
		        		 Entity localentity = entities.lookup(clidintensity[i].spid.longValue());
		           
		        		 locators.add(new EntityLocator(localentity,new Cell(row,col),clidintensity[i].id.toString(),new Heat((int)clidintensity[i].intensity.doubleValue()),localentity.getEntry().getClass().getName()));
		        	 
		        	}
		        	else{
		        		row++;
		        		col=0;
		        	 Entity localentity = entities.lookup(clidintensity[i].spid.longValue());
		        	 locators.add(new EntityLocator(localentity,new Cell(row,col),clidintensity[i].id.toString(),new Heat((int)clidintensity[i].intensity.doubleValue()),localentity.getEntry().getClass().getName()));
		        	  
		        	}
		        	if(row >=  20)
		        		break;
		        }
			

    	 return locators;
    	}
    }



 
}