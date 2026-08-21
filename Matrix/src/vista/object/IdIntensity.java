//Team: Tahiti
//Date: 2005/05/27
//Vers: 1.0
//File: IdIntensity.java


package vista.object;
import java.util.*;

import vista.tuples.FrequencyClidIntensity;
/**
 * This class stores the mapping of the clid,spid and intensity.
 * @author Tahiti.
 *
 * 
 */
public class IdIntensity {
	 public Integer id;
	 public Double intensity;
	 public Long spid;
/**
	 Constructor
	 */
public IdIntensity(Integer id , Double intensity,Long spid){
		this.id =id;
		this.intensity=intensity;
		this.spid=spid;
	}
 public static  long[] getIntensity(long X[],int count){
	   	int N= 10;
	   	long g;
	   	long R[] =new long[count];
	   	int i =0;
	   	long  sortedarray[]=new long[count];
	    long oldX[] = new long[count];
	    for (i=0;i<count;i++){
		    	oldX[i]=X[i];
	}
	    sortedarray=bubblesort(X,count);
//	calculate least value i.e min
//	calculate Rarray
	   for (i=0;i<count;i++){
	     R[i]=oldX[i]-sortedarray[0]+1;
	     }
//	calculate log R array
	     long logR[] = new long[count];
 
	    for (i=0;i<count;i++){
	     	logR[i]=Math.round(Math.log((double)R[i]));

	      }
    	long  sortedlogR[]=new long[count];
	    long oldlogR[] = new long[count];
	    for (i=0;i<count;i++){
		    	oldlogR[i]=logR[i];
	}
	sortedlogR=bubblesort(logR,count);
	if(sortedlogR[count-1]!=0){
	     g=N/sortedlogR[count-1];
	}
	 else
	    g=-1;//constant put to avoid divide by zero error
	   	 int k =0;
	     long sortreturn[] =new long[count];
	   	 while(k<count){
	   	 sortreturn[k]=g*oldlogR[k];
	   	 k++;
	 }
	 return sortreturn;
	   	 }

 public static  long[] bubblesort(long sortarray[],int count){
		 	int numSorted=0;
		 	int index;
		 	int n=count;
		 	while(numSorted < n){
		 		for(index=1;index <(n-numSorted);index++){
		 			if(sortarray[index] < sortarray[index-1]){
		 				long temp;
		 				temp=sortarray[index];
		 				sortarray[index]=sortarray[index-1];
		 				sortarray[index-1]=temp;
		 			}
		          }
		 		numSorted++;
		 	}
		 		return sortarray;

	}

	}
 
