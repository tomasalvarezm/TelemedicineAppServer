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

public class JDBCDoctorManager implements DoctorManager {

	private JDBCManager manager;
	private JDBCMedicalHistoryManager mhm;
	private JDBCBitalinoSignalManager bsm;

	public JDBCDoctorManager(JDBCManager manager, JDBCMedicalHistoryManager mhm, JDBCBitalinoSignalManager bsm) {
		super();
		this.manager = manager;
		this.mhm = mhm;
		this.bsm = bsm;
	}

	public Doctor getDoctorById(String doctor_id) throws SQLException {
		Doctor doc = null;
		Sex sexo = null;

		String sql = "SELECT * FROM Doctor WHERE id = ?";
		PreparedStatement prep = manager.getConnection().prepareStatement(sql);
		prep.setString(1, doctor_id);
		ResultSet rs = prep.executeQuery();
		String name = rs.getString("name");
		String sex = rs.getString("sex");
		if (sex == "MALE") {
			sexo = Sex.MALE;
		} else {
			sexo = Sex.FEMALE;
		}

		doc = new Doctor(doctor_id, name, sexo);

		rs.close();
		prep.close();

		return doc;
	}
	
	public Doctor getDoctorByName(String name) throws SQLException {
		Doctor doc = null;
		Sex sexo = null;

		String sql = "SELECT * FROM Doctor WHERE name = ?";
		PreparedStatement prep = manager.getConnection().prepareStatement(sql);
		prep.setString(1, name);
		ResultSet rs = prep.executeQuery();
		String id = rs.getString("id");
		String sex = rs.getString("sex");
		if (sex == "MALE") {
			sexo = Sex.MALE;
		} else {
			sexo = Sex.FEMALE;
		}

		doc = new Doctor(id, name, sexo);

		rs.close();
		prep.close();

		return doc;
	}

	public ArrayList<Doctor> getAllDoctors() throws SQLException {
		ArrayList<Doctor> doctors = new ArrayList<Doctor>();
		Sex sex = null;

		Statement stat = manager.getConnection().createStatement();
		String sql = "SELECT * FROM Doctor";
		ResultSet rs = stat.executeQuery(sql);
		while (rs.next()) {
			String id = rs.getString("id");
			String name = rs.getString("name");
			String sexo = rs.getString("sex");
			if (sexo == "MALE") {
				sex = Sex.MALE;
			} else {
				sex = Sex.FEMALE;
			}
			Doctor d = new Doctor(id, name, sex);
			doctors.add(d);
		}
		rs.close();
		stat.close();

		return doctors;
	}

	public ArrayList<Patient> listPatientsByDoctorId(String doctor_id) throws SQLException {
		ArrayList<Patient> patients = new ArrayList<Patient>();
		Sex sexo = null;

		String sql = "SELECT * FROM Patient WHERE doctor_id = ?";
		PreparedStatement prep = manager.getConnection().prepareStatement(sql);
		prep.setString(1, doctor_id);
		ResultSet rs = prep.executeQuery();
		while (rs.next()) {
			String patient_id = rs.getString("id");
			String name = rs.getString("name");
			String email = rs.getString("email");
			LocalDate dob = rs.getDate("dob").toLocalDate();
			Integer age = rs.getInt("age");
			String sex = rs.getString("sex");
			if (sex == "MALE") {
				sexo = Sex.MALE;
			} else {
				sexo = Sex.FEMALE;
			}
			Integer phoneNumber = rs.getInt("phoneNumber");

			Patient p = new Patient(patient_id, name, email, dob, age, sexo, phoneNumber);
			patients.add(p);
		}
		rs.close();
		prep.close();

		return patients;
	}

	public void insertDoctor(Doctor d) throws SQLException {

		String sql = "INSERT INTO Doctor (id,name,sex) VALUES (?,?,?)";
		PreparedStatement prep = manager.getConnection().prepareStatement(sql);
		prep.setString(1, d.getId());
		prep.setString(2, d.getName());
		prep.setString(3, d.getSex().toString());
		prep.executeUpdate();

	}

}
