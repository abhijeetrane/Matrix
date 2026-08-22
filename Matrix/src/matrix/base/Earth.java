package matrix.base;

import vista.ui.VistaUI;

//Thiest and Gnostic (Believe in God or Goddess or Ardhanareshwar(Third Gender). Follow some religion)

/*
 * 
 * Author:Shiva (Mahesh)
 * 
 * 
 */
public class Earth{
	
	private Shiv shiv;
	private Shakti shakti;
	
	
	
	//Brahma
	public void create(int numberOfNewReligions) {
		
		shiv = new Shiv();
		
		shakti = new Shakti(shiv.getInnerShaktiName());
		
		  
		 
		  System.out.println("Name of Shiv/Neo  is "+shiv.getInnerShivaName());
		  System.out.println("Name of Shakti/Trinity is "+ shakti.getName());
		  
		  	  
		  
		  try {
			shakti.createTridevi();  
	
			Thread.sleep(1000);
			
			shiv.createTridev();
			
			Thread.sleep(1000);
			
			shiv.createNonAbrahamicReligion();
		 	
			Thread.sleep(1000);
		
			
			shakti.createNewReligion(numberOfNewReligions);  
			
			Thread.sleep(1000);
			
			System.out.println("Creation complete");
			
		  } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
     
		
		
	}
	
	//Vishnu
	public void maintain() {
		
		try {
			Thread.sleep(1000);
			System.out.println("Maintainence complete");
			
		  } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		
		
	}
	
   //Mahesh	
   public void delete() {
		
		try {
			Thread.sleep(1000);
			System.out.println("Deletion complete");
			
		  } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		
		
	}
	
	public static void main(String args[]) {

     System.out.println("Program to create , maintain and delete  Multiverse.");		
		
	 Earth earth = new Earth();	
		
	 int numberOfAbrahamicReligions = 3;
	 
	 earth.create(numberOfAbrahamicReligions);
	 earth.maintain();
	 earth.delete();
	
     System.out.println("Program to create , maintain and delete Multiverse completed successfully.");
	
     String SPACEPLACE = "rmi://localhost/space";
     
     VistaUI vistaUI = new VistaUI(SPACEPLACE);
     
     vistaUI.setVisible(true);
     
	}
	
	
			
		
	}
