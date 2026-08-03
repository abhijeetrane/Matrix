package westmatrix;

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


// Vittal is Krishna
public class Krishna{
	
	private InnerShiva yashodhanRane = new InnerShiva();
	private InnerShakti smiritiGupta = new InnerShakti();
	
	
	private Krishna brahma;
	private Krishna vishnu;
	private Krishna mahesh;
	
		
	
	public String getInnerShivaName(){
		 return this.yashodhanRane.getName();
	}
	
	public void setInnerShivaName(String name){
		  this.yashodhanRane.setName(name);
	}
	
	
	public String getInnerShaktiName(){
		return this.smiritiGupta.getName();
	}
	
	public void setInnerShaktiName(String name){
		  this.smiritiGupta.setName(name);
	}
	
	
	
    public Krishna() {

        // Object named abhijeetRane
        
    	yashodhanRane.setName("Yashodhan Rane");       
        
        
        smiritiGupta.setName("Smiriti Gupta");
        
   //        System.out.println("Program to create Ardhanareshwar.");
        
   //     System.out.println("Name of InnerShiva in this man is "+abhijeetRane.getName());
   //     System.out.println("Name of InnerShakti in this man is "+amyRyan.getName());
        

  //      System.out.println("Program to create Ardhanareshwar completed successfully.");
    }
    
     public void createTridev() {
		
		brahma = new Krishna();
		vishnu = new Krishna();
		mahesh = new Krishna();	
		
		brahma.setInnerShivaName("Ronald Coleman");
		brahma.setInnerShaktiName("Ronald Coleman's wife");
		
		vishnu.setInnerShivaName("Dillip Yavagal");
		vishnu.setInnerShaktiName("Sujata Muleye");
		
		mahesh.setInnerShivaName("Abhijeet Rane");
		mahesh.setInnerShaktiName("Amy Ryan");
		
				
	}
    
    
}    
	

