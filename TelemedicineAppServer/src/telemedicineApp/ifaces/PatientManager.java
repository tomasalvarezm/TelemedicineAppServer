package telemedicineApp.ifaces;

import telemedicineApp.pojos.Patient;
import telemedicineApp.pojos.Symptom;

public interface PatientManager {
	public void insertPatient(Patient p);
	public void uploadSymptomToPatient(Patient p, Symptom s);
	public Patient getPatientById(String id);
	public String getPatientNameById(String id);

}
