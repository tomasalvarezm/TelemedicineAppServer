package telemedicineApp.pojos;

public class Symptom {
	private String name;	
	
	public Symptom() {
		super();
	}
	
	public Symptom(String name) {
		this.name = name;
		
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
