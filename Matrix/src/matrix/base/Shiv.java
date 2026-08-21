package matrix;

// File: InnerShiva.java

// Abstract class with no implementation
abstract class ParaShiva {
}

abstract class ParaShakti {
}


//Base public class
class InnerShakti extends ParaShakti {
	
	private String name;
	
	// Private objects
    private InnerShiva innerShiva;
    
 // Public constructor
    public InnerShakti() {
        this(true);
    }

    // Private constructor used to control recursion
    private InnerShakti(boolean createChild) {

        // Always create InnerShakti
        innerShiva = new InnerShiva(true,false);        

        //System.out.println("InnerShakti created.");
    }

    
    public String getName() {
    	return this.name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
	
}

// Base pu/blic class
class InnerShiva extends ParaShiva {

    private String name;
	
	// Private objects
    private InnerShiva innerShiva;
    private InnerShakti innerShakti;

    // Public constructor
    public InnerShiva() {
        this(true);
    }

    // Private constructor used to control recursion
    private InnerShiva(boolean createChild) {

        // Always create InnerShakti
        innerShakti = new InnerShakti();

        // Create only one child InnerShiva
        if (createChild) {
            innerShiva = new InnerShiva(false);
        }

        //System.out.println("InnerShiva created.");
    }

    // Public constructor used to control recursion
    public InnerShiva(boolean createChild, boolean createShakti) {

       if(createShakti) {
    	   innerShakti = new InnerShakti();    	   
       }
    	
        // Create only one child InnerShiva
        if (createChild) {
            innerShiva = new InnerShiva(false,false);
        }

        //System.out.println("InnerShiva created.");
    }
   

    public String getName() {
    	return this.name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
}

public class Shiv  extends InnerShiva{
	
	private InnerShiva abhijeetRane = new InnerShiva();
	private InnerShakti amyRyan = new InnerShakti();
	
	
	
	
	private Shiv brahma;
	private Shiv vishnu;
	private Shiv mahesh;
	
	
		
	public Shiv(String name) {
		 super.setName(name);
	}
		
	public String getShivName() {
			return super.getName();
	}
		
	
	public String getInnerShivaName(){
		 return this.abhijeetRane.getName();
	}
	
	public void setInnerShivaName(String name){
		  this.abhijeetRane.setName(name);
	}
	
	
	public String getInnerShaktiName(){
		return this.amyRyan.getName();
	}
	
	public void setInnerShaktiName(String name){
		  this.amyRyan.setName(name);
	}
	
	
	
    public Shiv() {

        // Object named abhijeetRane
        
        abhijeetRane.setName("Sadanand Rane");       
        
        
        amyRyan.setName("Mangala Sadanand Savant Rane");
        
   //        System.out.println("Program to create Ardhanareshwar.");
        
   //     System.out.println("Name of InnerShiva in this man is "+abhijeetRane.getName());
   //     System.out.println("Name of InnerShakti in this man is "+amyRyan.getName());
        

  //      System.out.println("Program to create Ardhanareshwar completed successfully.");
    }
    
     public void createTridev() {
		
		brahma = new Shiv("Mangesh Mujumdar");
		vishnu = new Shiv("Dillip Yavagal");
		mahesh = new Shiv("Abhijeet Rane");	
		
		//brahma.setInnerShivaName("Mangesh Mujumdar");
		brahma.setInnerShaktiName("Pallavi Mujumdar");
		
		//vishnu.setInnerShivaName("Dillip Yavagal");
		vishnu.setInnerShaktiName("Sujata Muleye");
		
		//mahesh.setInnerShivaName("Abhijeet Rane");
		mahesh.setInnerShaktiName("Amy Ryan");
		
				
	}
    
     public void createNonAbrahamicReligion() {    	 
    	 
        System.out.println("create Non-Abrahamic Religion");

        ShivaCreatingReligions shivaCreatingReligions = new ShivaCreatingReligions();
		
		shivaCreatingReligions.start();
	 	
        
  	   }
     
     
}    
	
class ShivaCreatingReligions extends Thread{

	 public void run() {
		 
		 System.out.println("Tribals or Adivasi way of life ");
		 
		 System.out.println("Tribal or Adivasi man becane Rudra");
		 
		 System.out.println("Rudra becane Shiva");
		
		 System.out.println("Hinduism , Zorashtraism or Parsi or Irani and Greek religion created");

		 System.out.println("Jainism religion created");
		 
		 System.out.println("Buddhism religion created");
		 
		 System.out.println("Sikhism religion created");
	 
	 
	 }
		 
		}

