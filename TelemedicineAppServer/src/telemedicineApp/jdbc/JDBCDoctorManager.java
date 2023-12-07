package telemedicineApp.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;


import telemedicineApp.ifaces.DoctorManager;
import telemedicineApp.pojos.BitalinoSignal;
import telemedicineApp.pojos.Doctor;
import telemedicineApp.pojos.MedicalHistory;
import telemedicineApp.pojos.Medication;
import telemedicineApp.pojos.Patient;
import telemedicineApp.pojos.Sex;

public class JDBCDoctorManager implements DoctorManager{

	private JDBCManager manager;
	private JDBCMedicalHistoryManager mhm;
	private JDBCBitalinoSignalManager bsm;

	
	
	public JDBCDoctorManager(JDBCManager manager, JDBCMedicalHistoryManager mhm, JDBCBitalinoSignalManager bsm) {
		super();
		this.manager = manager;
		this.mhm = mhm;
		this.bsm = bsm;
	}
	

	public Doctor getDoctorById(String doctor_id) throws SQLException{
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

	public ArrayList<Patient> listPatientsByDoctorId(String doctor_id) throws SQLException {
		ArrayList<Patient> patients = new ArrayList<Patient>();
		Medication med=null;
		Sex sexo=null;
		Patient p=null;
		try {
			Statement stat = manager.getConnection().createStatement();
			String sql = "SELECT * FROM Patient WHERE doctor_id= ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, doctor_id);
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				String patient_id = rs.getString("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				LocalDate dob= rs.getDate("dob").toLocalDate();
				Integer age = rs.getInt("age");
				String sex = rs.getString("sex");
				if (sex=="MALE") {
					 sexo=Sex.MALE;
				}else {
					sexo=Sex.FEMALE;
				}
				String meds=rs.getString("medication");
				if (meds=="LEVODOPA") {
					 med=Medication.LEVODOPA;
				}else {
					med=Medication.PRAMIPEXOL;
				}
				Integer phoneNumber = rs.getInt("phoneNumber");
				//medhist ,bit signals y doctor
				
				ArrayList<MedicalHistory> medhists = mhm.getMedHistoriesByPatientId(patient_id);
				ArrayList<BitalinoSignal> signals = bsm.getSignalsByPatientId(patient_id);
				Doctor doctor= getDoctorById(doctor_id);
				
				p = new Patient(patient_id,name, email, dob, age, sexo, phoneNumber, medhists, signals, doctor);
							
				patients.add(p);
			}
			rs.close();
			stat.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return patients;
	}

	
	public void insertDoctor(Doctor d) throws SQLException {
		
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
