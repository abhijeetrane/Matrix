package westmatrix;


//Aethiest and Agnostic (Do not believe in God or Goddess or Ardhanareshwar(Third Gender). Do not follow any religion)

/*
 * 
 * Author: Shakti (Krishna) 
 * 
 * 
 */



public class Earth{
	
	private Krishna shiv;
	private Radha shakti;
	
	
	
	//Brahma
	public void create() {
		
		shiv = new Krishna();
		
		shakti = new Radha(shiv.getInnerShaktiName());
		
		  
		 
		  System.out.println("Name of Krishna/Neo  is "+shiv.getInnerShivaName());
		  System.out.println("Name of Radha/Trinity is "+ shakti.getName());
		  
		  	  
		  
		  try {
			shakti.createTridevi();  
			shiv.createTridev();		
			
			Thread.sleep(60000);
			System.out.println("Creation complete");
			
		  } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
     
		
		
	}
	
	//Vishnu
	public void maintain() {
		
		try {
			Thread.sleep(60000);
			System.out.println("Maintainence complete");
			
		  } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		
		
	}
	
   //Mahesh	
   public void delete() {
		
		try {
			Thread.sleep(60000);
			System.out.println("Deletion complete");
			
		  } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		
		
	}
	
	public static void main(String args[]) {

     System.out.println("Program to create , maintain and delete  Multiverse.");		
		
	 Earth earth = new Earth();	
		
	 earth.create();
	 earth.maintain();
	 earth.delete();
	
     System.out.println("Program to create , maintain and delete Multiverse completed successfully.");
		
	}
	
	
}