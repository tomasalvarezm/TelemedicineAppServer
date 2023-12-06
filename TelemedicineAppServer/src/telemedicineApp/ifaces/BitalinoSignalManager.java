package telemedicineApp.ifaces;

import java.util.ArrayList;

import telemedicineApp.pojos.BitalinoSignal;

public interface BitalinoSignalManager {
	
	public ArrayList<BitalinoSignal> getSignalsByPatientId(String patient_id);
	public void uploadSignal (BitalinoSignal signal);

}
