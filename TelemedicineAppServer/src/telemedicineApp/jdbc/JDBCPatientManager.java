package telemedicineApp.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import telemedicineApp.ifaces.PatientManager;
import telemedicineApp.pojos.*;


public class JDBCPatientManager implements PatientManager{
	private JDBCManager manager;
	private JDBCDoctorManager dm;
	private JDBCMedicalHistoryManager mhm;
	private JDBCBitalinoSignalManager bsm;
	
	
	public JDBCPatientManager(JDBCManager manager, JDBCDoctorManager dm, JDBCMedicalHistoryManager mhm,
			JDBCBitalinoSignalManager bsm) {
		super();
		this.manager = manager;
		this.dm = dm;
		this.mhm = mhm;
		this.bsm = bsm;
	}

	public Patient getPatientById(String patient_id) {
		Patient patient = null;
		Sex sexo=null;

		try {
			String sql = "SELECT * FROM Patient WHERE id = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, patient_id);
			ResultSet rs = prep.executeQuery();
			
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
			Integer phoneNumber = rs.getInt("phoneNumber");
			ArrayList<MedicalHistory> medhists = mhm.getMedHistoriesByPatientId(patient_id);
			ArrayList<BitalinoSignal> signals = bsm.getSignalsByPatientId(patient_id);
			String doctor_id=rs.getString("doctor_id");
			Doctor doctor= dm.getDoctorById(doctor_id);
			
			patient = new Patient(patient_id,name, email, dob, age, sexo, phoneNumber, medhists, signals, doctor);
						
			rs.close();
			prep.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return patient;
		
	}
	
	public void insertPatient(Patient p) {
		try {
			String sql = "INSERT INTO Patient (id, name, email, dob, age, sex, phoneNumber, doctor_id) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, p.getId());
			prep.setString(2,p.getName());
			prep.setString(3,p.getEmail());
			prep.setString(4, p.getDob().toString());
			prep.setInt(5, p.getAge());
			prep.setString(6, p.getSex().toString());
			prep.setInt(7, p.getPhoneNumber());
			prep.setString(8,p.getDoctor().getId());
			prep.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
