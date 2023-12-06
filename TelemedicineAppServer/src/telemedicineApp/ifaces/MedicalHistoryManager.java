package telemedicineApp.ifaces;

import java.util.ArrayList;
import telemedicineApp.pojos.MedicalHistory;

public interface MedicalHistoryManager {
	
	public ArrayList<MedicalHistory> getMedHistoriesByPatientId (String patient_id);
	public void uploadMedicalHistory (MedicalHistory mh);
	
}
