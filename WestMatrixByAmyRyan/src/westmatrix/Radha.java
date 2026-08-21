package westmatrix;


//Base public class
public class Radha extends InnerShakti {
	
	private Radha saraswati;
	private Radha laxmi;
	private Radha parvati;
	
	
	public Radha() {
	  super();	
	}
	
	public Radha(String name) {
		super.setName(name);
	}
	
	public String getShaktiName() {
		return super.getName();
	}

	public void createTridevi() {
		
		saraswati = new Radha("Pallavi Mujumdar");
		laxmi = new Radha("Sujata Muleye");
		parvati = new Radha("Amy Ryan");		
		
	}
	
}


