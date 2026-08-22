package matrix;


//Base public class
public class Shakti extends InnerShakti {
	
	private Shakti saraswati;
	private Shakti laxmi;
	private Shakti parvati;
	
	
	public Shakti() {
	  super();	
	}
	
	public Shakti(String name) {
		super.setName(name);
	}
	
	public String getShaktiName() {
		return super.getName();
	}

	public void createTridevi() {
		
		saraswati = new Shakti("Pallavi Mujumdar");
		laxmi = new Shakti("Sujata Muleye");
		parvati = new Shakti("Amy Ryan");		
		
	}

	

  
   public void createNewReligion(int nesting) {
		
	  
	   
	   System.out.println("Message to Abraham");	 

       if(nesting == 3) {   	   
    	   
     	    System.out.println("Message to Moses - Create new Religion Judaism");
     	    
     	    God god = new God("Evelyn Fernandes's husband","Judaism","God");
     	    
     	    Goddess goddess = new Goddess("Evelyn Fernandes","Judaism","Holy Spirit");
     	    
     	    Angel angel = new Angel("Evelyn Fernandes's husband","Judaism","Michael");
     	    
     	    Demon demon = new Demon("Evelyn Fernandes","Judaism","Gabriel");
     	    
     	    Satan satan = new Satan("Aatif Momin","Judaism","Satan");
			
     	    nesting--;
     	    
			System.out.println("Message to Prophet or Messenger - No new Religion required");
			
			createNewReligion(nesting);
			
		}

       if(nesting == 2) {
    	   
    	    System.out.println("Message to Jesus - Create new Religion Christianity");

    	    
    	    God god = new God("Evonne Gonsalves's husband","Christianity","God");
     	    
     	    Goddess goddess = new Goddess("Evonne Gonsalves","Christianity","Holy Spirit");
 
     	    Angel angel = new Angel("Evonne Gonsalves's husband","Christianity","Michael");
    	    
    	    Demon demon = new Demon("Evonne Gonsalves","Christianity","Gabriel");     	    
    	    
    	    Satan satan = new Satan("Aatif Momin","Christianity","Lucifer");
    	    
    	    nesting--;
    	    
    	    System.out.println("Message to Prophet or Messenger - No new Religion required");		
    	    createNewReligion(nesting);
		}
		
       if(nesting == 1) {
    	   
    	   System.out.println("Message to Mohammed - Create new Religion Islam");
			
    	   
    	   God god = new God("Abdul Munaf Mulla","Islam","Allah");
    	    
    	   Goddess goddess = new Goddess("Abdul Munaf Mulla's wife","Islam","Ruh-Al-Qudus");
    	    
    	   Angel angel = new Angel("Abdul Munaf Mulla","Islam","Michael");
    	    
    	   Demon demon = new Demon("Abdul Munaf Mulla's wife","Islam","Gabriel");	    
    	   
    	   Satan satan = new Satan("Aatif Momin","Islam","Shaitan");
    	   
   	       nesting--;
   	    
   	       System.out.println("Message to Prophet or Messenger - No new Religion required");		
   	       createNewReligion(nesting); 
		}	 

       if(nesting == 0) {
			
			System.out.println("Message to Prophet or Messenger - No new Religion required");		
			
			return;
		}
		
       return;
	 }
   
	
}


class Satan{
	
	String name;
		
	String nameOfReligion;
	
	String nameInReligion;
	
    
	 public Satan(String name,String nameOfReligion,String nameInReligion ) {
		 
	    this.name = name;
	    this.nameOfReligion = nameOfReligion;
	    this.nameInReligion = nameInReligion;
		 
	 }
	
	
}

class God{
	
	String name;
	
	String nameOfReligion;
	
	String nameInReligion;
	
	public God(String name,String nameOfReligion,String nameInReligion ) {
		 
	    this.name = name;
	    this.nameOfReligion = nameOfReligion;
	    this.nameInReligion = nameInReligion;
		 
	 }
}	

class Goddess{
		
		String name;
		
		String nameOfReligion;
		
		String nameInReligion;
		
		public Goddess(String name,String nameOfReligion,String nameInReligion ) {
			 
		    this.name = name;
		    this.nameOfReligion = nameOfReligion;
		    this.nameInReligion = nameInReligion;
			 
		 }	
	
}

class Angel{
	
	String name;
	
	String nameOfReligion;
	
	String nameInReligion;
	
	public Angel(String name,String nameOfReligion,String nameInReligion ) {
		 
	    this.name = name;
	    this.nameOfReligion = nameOfReligion;
	    this.nameInReligion = nameInReligion;
		 
	 }
}

class Demon{
	
	String name;
	
	String nameOfReligion;
	
	String nameInReligion;
	
	public Demon(String name,String nameOfReligion,String nameInReligion ) {
		 
	    this.name = name;
	    this.nameOfReligion = nameOfReligion;
	    this.nameInReligion = nameInReligion;
		 
	 }
}



