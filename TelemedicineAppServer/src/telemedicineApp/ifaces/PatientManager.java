package telemedicineApp.ifaces;

import telemedicineApp.pojos.Patient;

public interface PatientManager {
	
	public Patient getPatientById(String patient_id);
	public void insertPatient(Patient p);


}
