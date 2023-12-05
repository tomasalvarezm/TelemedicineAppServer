package telemedicineApp.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import telemedicineApp.ifaces.DoctorManager;
import telemedicineApp.pojos.*;

public class JDBCDoctorManager implements DoctorManager{

	private JDBCManager manager;
	private JDBCSymptomManager sman;


	public JDBCDoctorManager(JDBCManager m) {
		this.manager = m;
	}
	//TODO probar
	public ArrayList<Patient> listPatientsByDoctorId(String id) {
		ArrayList<Patient> patients = new ArrayList<Patient>();
		Sex sexo=null;
		Medication med=null;
		try {
			Statement stat = manager.getConnection().createStatement();
			String sql = "SELECT * FROM Patient WHERE doctor_id=?" ;
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				String pat_id = rs.getString("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Integer age = rs.getInt("age");
				String sex = rs.getString("sex");
				String meds=rs.getString("medication");
				
				if (sex=="MALE") {
					 sexo=Sex.MALE;
				}else {
					sexo=Sex.FEMALE;
				}
				Integer phoneNumber = rs.getInt("phoneNumber");
				
				if (meds=="LEVODOPA") {
					 med=Medication.LEVODOPA;
				}else {
					med=Medication.PRAMIPEXOL;
				}
				//TODO revisar aquí
				ArrayList <Symptom> symps = sman.getSymptomsFromPatientId(id);
				
				Patient p = new Patient(pat_id, name, email, age, sexo, phoneNumber, symps, med);
				patients.add(p);
			}
			rs.close();
			stat.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return patients;
	}

	public void insertDoctor(Doctor d) {
		
		try {
			String sql = "INSERT INTO Doctor (id) VALUES (?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, d.getId());
			prep.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//TODO Probar si funciona
	public Doctor getDoctorById(String id) {
		Doctor doc=null;
		Patient patient = null;
		Sex sexo=null;
		try {
			String sql = "SELECT * FROM Doctor WHERE id = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, id);
			ResultSet rs = prep.executeQuery();
			String name = rs.getString("name");
			ArrayList <Patient> patientsList = listPatientsByDoctorId(id);
			doc= new Doctor(id,patientsList);	
			rs.close();
			prep.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return doc;
	}
	

}
