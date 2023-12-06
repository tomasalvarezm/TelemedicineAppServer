package telemedicineApp.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import telemedicineApp.ifaces.DoctorManager;
import telemedicineApp.pojos.Doctor;
import telemedicineApp.pojos.Patient;
import telemedicineApp.pojos.Sex;

public class JDBCDoctorManager implements DoctorManager{

	private JDBCManager manager;
	private JDBCPatientManager pm;


	public JDBCDoctorManager(JDBCManager m,JDBCPatientManager pm) {
		this.manager = m;
		this.pm = pm;
		
	}
	
	public Doctor getDoctorById(String doctor_id) {
		Doctor doc=null;
		Sex sexo=null;
		
		try {
			String sql = "SELECT * FROM Doctor WHERE id = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, doctor_id);
			ResultSet rs = prep.executeQuery();
			String name = rs.getString("name");
			String sex = rs.getString("sex");
			if(sex=="MALE") {
				sexo=Sex.MALE;
			}
			else {
				sexo=Sex.FEMALE;
			}
			ArrayList <Patient> patientsList = listPatientsByDoctorId(doctor_id);
			doc= new Doctor(doctor_id,name,sexo,patientsList);	
			
			rs.close();
			prep.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return doc;
	}

	public ArrayList<Patient> listPatientsByDoctorId(String doctor_id) {
		ArrayList<Patient> patients = new ArrayList<Patient>();
		try {
			Statement stat = manager.getConnection().createStatement();
			String sql = "SELECT * FROM Patient WHERE doctor_id= ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, doctor_id);
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				String patient_id = rs.getString("id");
				Patient p = pm.getPatientById(patient_id);
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
			String sql = "INSERT INTO Doctor (id,name,sex) VALUES (?,?,?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, d.getId());
			prep.setString(2, d.getName());
			prep.setString(3,d.getSex().toString());
			prep.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	


}
