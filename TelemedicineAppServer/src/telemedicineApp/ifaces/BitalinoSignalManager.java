package telemedicineApp.ifaces;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import telemedicineApp.pojos.BitalinoSignal;

public interface BitalinoSignalManager {
	
	public BitalinoSignal getBitalinoSignal(String patient_id, LocalDate dateSignal) throws SQLException;
	public ArrayList<BitalinoSignal> getSignalsByPatientId(String patient_id) throws SQLException;
	public void saveSignal (BitalinoSignal bs) throws SQLException;
	public BitalinoSignal exportSignal (String id) throws SQLException;
}
