package telemedicineApp.pojos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class MedicalHistory implements Serializable {
	
	private ArrayList<Symptom> symptoms;
	private LocalDate date_medhist;
	private Medication medication;
	private String patient_id;
	
	
	public MedicalHistory() {
		super();
	}
	
	public MedicalHistory(ArrayList<Symptom> symptoms, LocalDate date_medhist, Medication medication,
			String patient_id) {
		super();
		this.symptoms = symptoms;
		this.date_medhist = date_medhist;
		this.medication = medication;
		this.patient_id = patient_id;
	}


	public ArrayList<Symptom> getSymptoms() {
		return symptoms;
	}
	
	public void setSymptoms(ArrayList<Symptom> symptoms) {
		this.symptoms = symptoms;
	}

	public Medication getMedication() {
		return medication;
	}
	
	public void setMedication(Medication medication) {
		this.medication = medication;
	}
	
	public String getPatient_id() {
		return patient_id;
	}
	
	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}

	public LocalDate getDate_medhist() {
		return date_medhist;
	}

	public void setDate_medhist(LocalDate date_medhist) {
		this.date_medhist = date_medhist;
	}
	
	

}