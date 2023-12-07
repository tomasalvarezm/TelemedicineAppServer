package telemedicineApp.ifaces;

import java.sql.SQLException;

import telemedicineApp.pojos.Patient;

public interface PatientManager {
	
	public Patient getPatientById(String patient_id) throws SQLException;
	public void insertPatient(Patient p) throws SQLException;


}
