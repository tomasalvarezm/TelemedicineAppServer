package telemedicineApp.pojos;

import java.io.Serializable;

public class Symptom implements Serializable {
	private static final long serialVersionUID = -5556145030186387855L;
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
