package telemedicineApp.ifaces;

import java.sql.SQLException;
import java.util.ArrayList;
import telemedicineApp.pojos.MedicalHistory;

public interface MedicalHistoryManager {
	
	public ArrayList<MedicalHistory> getMedHistoriesByPatientId (String patient_id) throws SQLException;
	public void uploadMedicalHistory (MedicalHistory mh) throws SQLException;
	
}
