package telemedicineApp.ifaces;

import java.time.LocalDate;
import java.util.ArrayList;

import telemedicineApp.pojos.Symptom;

public interface SymptomManager {
	public void insertSymptom(Symptom s);
	public Symptom getSymptomByName(String name);
	public ArrayList<Symptom> getSymptomsFromPatientId(String id);
	
}
