package telemedicineApp.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import telemedicineApp.ifaces.PatientManager;
import telemedicineApp.pojos.*;


public class JDBCPatientManager implements PatientManager{
	private JDBCManager manager;
	private JDBCSymptomManager sman;
	private JDBCDoctorManager dm;
	
	//TODO probar este método. Terminarlo
	public Patient getPatientById(String id) {
		Patient patient = null;
		Sex sexo=null;
		Medication med=null;

		try {
			String sql = "SELECT * FROM Patient WHERE id = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, id);
			ResultSet rs = prep.executeQuery();

			String name = rs.getString("name");
			String email = rs.getString("email");
			Integer age = rs.getInt("age");
			String sex = rs.getString("sex");
			Integer phoneNumber = rs.getInt("phoneNumber");
			ArrayList <Symptom> symps = sman.getSymptomsFromPatientId(id);
			String meds = rs.getString("medication");
			if (sex=="MALE") {
				 sexo=Sex.MALE;
			}else {
				sexo=Sex.FEMALE;
			}
			if (meds=="LEVODOPA") {
				 med=Medication.LEVODOPA;
			}else {
				med=Medication.PRAMIPEXOL;
			}
			//TODO ESTE METODO Y AÑADIR ESTA LINEA Doctor doc = dm.getDoctorFromId(id);
			patient = new Patient(id,name, email, age, sexo, phoneNumber, symps, med);
						
			rs.close();
			prep.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return patient;
		
	}
	
	public String getPatientNameById(String id) {
		String patient_name=null;
		try {
			String sql = "SELECT name FROM Patient WHERE id = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, id);
			ResultSet rs = prep.executeQuery();
			patient_name=rs.getString("name");
			rs.close();
			prep.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return patient_name;
	}

	public void insertPatient(Patient p) {
		try {
			String sql = "INSERT INTO Patient (id, name, email, age, sex, phoneNumber, doctor_id) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, p.getId());
			prep.setString(2,p.getName());
			prep.setString(3,p.getEmail());
			prep.setInt(4, p.getAge());
			prep.setString(5, p.getSex().toString());
			prep.setInt(6, p.getPhoneNumber());
			prep.setString(7,p.getDoctor().getId());
			prep.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void uploadSymptomToPatient(Patient p, Symptom s) {
		try {
			String sql = "INSERT INTO PatientHasSymptoms (patient_id, symptom_name) VALUES (?,?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, p.getId());
			prep.setString(2, s.getName());
			prep.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

}
