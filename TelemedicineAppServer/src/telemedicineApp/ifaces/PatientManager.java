package telemedicineApp.ifaces;

import java.util.ArrayList;

import telemedicineApp.pojos.Patient;

public interface PatientManager {
	
	public Patient getPatientById(String patient_id);
	public void insertPatient(Patient p);


}
