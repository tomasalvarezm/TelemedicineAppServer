package telemedicineApp.ifaces;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import telemedicineApp.pojos.MedicalHistory;

public interface MedicalHistoryManager {
	
	public ArrayList<MedicalHistory> getMedHistoriesByPatientId (String patient_id) throws SQLException;
	public void uploadMedicalHistory (MedicalHistory mh) throws SQLException;
	public int getMedicalHistoryID(MedicalHistory medicalHistory) throws SQLException;
	public MedicalHistory getMedicalHistory (String patient_id, LocalDate medhist_date) throws SQLException;

}
