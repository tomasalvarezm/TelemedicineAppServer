package telemedicineApp.ifaces;

import java.sql.SQLException;
import java.util.ArrayList;
import telemedicineApp.pojos.Symptom;

public interface SymptomManager {

	public ArrayList<Symptom> getSymptomsFromMedHistId(Integer mh_id) throws SQLException;
	public void uploadSymptomsToMedicalHistory(Integer medhist_id, ArrayList<Symptom> symptoms) throws SQLException;
}
