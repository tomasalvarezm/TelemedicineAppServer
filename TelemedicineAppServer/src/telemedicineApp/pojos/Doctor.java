package telemedicineApp.pojos;

import java.io.Serializable;
import java.util.ArrayList;

public class Doctor implements Serializable{

	private static final long serialVersionUID = -4009178969875266980L;
	private String id;
	private String name;
	private Sex sex;
	private ArrayList<Patient> patientsList;
	

	public Doctor() {
		super();

	}

	public Doctor(String id, String name, Sex sex, ArrayList<Patient> patientsList) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.patientsList = patientsList;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Sex getSex() {
		return sex;
	}


	public void setSex(Sex sex) {
		this.sex = sex;
	}


	public ArrayList<Patient> getPatientsList() {
		return patientsList;
	}


	public void setPatientsList(ArrayList<Patient> patientsList) {
		this.patientsList = patientsList;
	}
	
	
}
