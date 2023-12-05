package telemedicineApp.pojos;

import java.time.LocalDate;

public class Symptom {
	
	private String name;
	private LocalDate date;

	//CONSTRUCTOR
	public Symptom(String name, LocalDate date) {
		super();
		this.name = name;
		this.date=date;
	}

	//GETTERS AND SETTERS
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	

}
