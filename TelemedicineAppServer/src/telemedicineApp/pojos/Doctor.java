package telemedicineApp.pojos;

import java.util.ArrayList;

public class Doctor {

	private String id;
	private ArrayList<Patient> patientsList;
	

	public Doctor(String id, ArrayList<Patient> patientsList) {
		super();
		this.id = id;
		this.patientsList = patientsList;
	}

	public ArrayList<Patient> getPatientsList() {
		return patientsList;
	}

	public void setPatientsList(ArrayList<Patient> patientsList) {
		this.patientsList = patientsList;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
