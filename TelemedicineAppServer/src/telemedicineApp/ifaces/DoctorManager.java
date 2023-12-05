package telemedicineApp.ifaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import telemedicineApp.pojos.Doctor;
import telemedicineApp.pojos.Medication;
import telemedicineApp.pojos.Patient;
import telemedicineApp.pojos.Sex;
import telemedicineApp.pojos.Symptom;

public interface DoctorManager {
	public ArrayList<Patient> listPatientsByDoctorId(String id);
	public void insertDoctor(Doctor d);
	public Doctor getDoctorById(String id);
}
