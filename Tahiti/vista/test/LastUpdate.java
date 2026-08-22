//Team: Tahiti
//Date: 2005/05/27
//Vers: 1.0
//File: LastUpdate.java

package vista.test;
/**
 * This class does the calculations for the last update value
 * @author Tahiti.
 *
 * 
 *  */
public class LastUpdate {
	 public	long hours=0;
	 public long minutes=0;
	 public long seconds=0;
	 public long ms=0;

	   public void calculate(long mst) {



	if(mst>=1000) {

	      ms=mst/1000;

	      hours = ms / 3600;

	      ms = ms - (hours * 3600);

	      minutes = ms / 60;

	      ms = ms - (minutes * 60);

	      seconds = ms;
	  }


	      
	   }


	}