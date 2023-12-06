package telemedicineApp.ifaces;

import java.util.ArrayList;

import telemedicineApp.pojos.MedicalHistory;
import telemedicineApp.pojos.Symptom;

public interface SymptomManager {

	public ArrayList<Symptom> getSymptomsFromMedHistId(Integer mh_id);
	public void uploadSymptomToMedicalHistory(MedicalHistory mh, Symptom s);
}
