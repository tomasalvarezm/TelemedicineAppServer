package telemedicineApp.ifaces;


import java.util.ArrayList;

import telemedicineApp.pojos.Doctor;
import telemedicineApp.pojos.Patient;


public interface DoctorManager {
	
	public Doctor getDoctorById(String doctor_id);
	public void insertDoctor(Doctor d);
	public ArrayList<Patient> listPatientsByDoctorId(String doctor_id);

}
