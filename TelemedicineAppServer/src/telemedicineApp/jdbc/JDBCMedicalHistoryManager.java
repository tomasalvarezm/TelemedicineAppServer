package telemedicineApp.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import telemedicineApp.ifaces.MedicalHistoryManager;
import telemedicineApp.pojos.MedicalHistory;
import telemedicineApp.pojos.Medication;
import telemedicineApp.pojos.Symptom;



public class JDBCMedicalHistoryManager implements MedicalHistoryManager {

	private JDBCManager manager;
	private JDBCSymptomManager sm;

	
	
	public JDBCMedicalHistoryManager(JDBCManager manager, JDBCSymptomManager sm) {
		super();
		this.manager = manager;
		this.sm = sm;
	}

	public ArrayList<MedicalHistory> getMedHistoriesByPatientId (String patient_id) throws SQLException{
		ArrayList<MedicalHistory> medhists	= new ArrayList <MedicalHistory>();
		Medication medication=null;
		
		try {
		String sql = "SELECT * FROM MedicalHistory WHERE patient_id = ?";
		PreparedStatement prep = manager.getConnection().prepareStatement(sql);
		prep.setString(1, patient_id);
		ResultSet rs = prep.executeQuery();
		while (rs.next()) {
			Integer mh_id  = rs.getInt("id");
			ArrayList<Symptom> symps = sm.getSymptomsFromMedHistId(mh_id);
			String med = rs.getString("medication");
			if (med=="LEVODOPA") {
				 medication=Medication.LEVODOPA;
			}else {
				medication=Medication.PRAMIPEXOL;
			}
			LocalDate date_medhist= rs.getDate("date_medhist").toLocalDate();			
			MedicalHistory mh = new MedicalHistory(symps, date_medhist, medication,patient_id);
			medhists.add(mh);
		}
		rs.close();
		prep.close();
		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return medhists;
	}
	
	public void uploadMedicalHistory (MedicalHistory mh) throws SQLException {
		try {
			String sql = "INSERT INTO MedicalHistory (medication, date_medhist, patient_id) VALUES (?,?,?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, mh.getMedication().toString());
			prep.setDate(2, Date.valueOf(mh.getDate_medhist()));
			prep.setString(3, mh.getPatient_id());
			
			prep.executeUpdate();
			
			int medicalHistoryID = getMedicalHistoryID(mh);
			sm.uploadSymptomsToMedicalHistory(medicalHistoryID, mh.getSymptoms());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private int getMedicalHistoryID(MedicalHistory medicalHistory) throws SQLException {
		Integer medHistID = null;
		try {
			String sql = "SELECT id FROM MedicalHistory WHERE patient_id = ? AND date_medhist = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, medicalHistory.getPatient_id());
			prep.setDate(2, Date.valueOf(medicalHistory.getDate_medhist()));
			ResultSet rs = prep.executeQuery();
			medHistID = rs.getInt("id");
			
			rs.close();
			prep.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return medHistID;
	}
}
