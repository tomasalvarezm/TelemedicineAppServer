package telemedicineApp.ifaces;


import java.sql.SQLException;
import java.util.ArrayList;

import telemedicineApp.pojos.Doctor;
import telemedicineApp.pojos.Patient;


public interface DoctorManager {
	
	public Doctor getDoctorById(String doctor_id) throws SQLException; 
	public void insertDoctor(Doctor d) throws SQLException;
	public ArrayList<Patient> listPatientsByDoctorId(String doctor_id) throws SQLException; 

}
