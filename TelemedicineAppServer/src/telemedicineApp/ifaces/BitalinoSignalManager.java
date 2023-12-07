package telemedicineApp.ifaces;

import java.sql.SQLException;
import java.util.ArrayList;

import telemedicineApp.pojos.BitalinoSignal;

public interface BitalinoSignalManager {
	
	public ArrayList<BitalinoSignal> getSignalsByPatientId(String patient_id) throws SQLException;
	public void saveSignal (BitalinoSignal bs) throws SQLException;
	public BitalinoSignal exportSignal (String id) throws SQLException;
}
