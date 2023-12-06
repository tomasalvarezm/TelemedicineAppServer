package telemedicineApp.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import telemedicineApp.ifaces.BitalinoSignalManager;
import telemedicineApp.pojos.BitalinoSignal;

public class JDBCBitalinoSignalManager implements BitalinoSignalManager {

	private JDBCManager manager;
	
	
	public ArrayList<BitalinoSignal> getSignalsByPatientId(String patient_id) {
		ArrayList<BitalinoSignal> signals = new ArrayList<BitalinoSignal>();

		try {
			String sql = "SELECT * FROM BitalinoSignal WHERE patient_id = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, patient_id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String signal_duration = rs.getString("signal_duration");
				LocalDate date_signal = rs.getDate("date_signal").toLocalDate();
				String filePath = rs.getString("filePath");
				BitalinoSignal signal = new BitalinoSignal (id, patient_id, signal_duration,date_signal, filePath);
				signals.add(signal);
			}
			rs.close();
			prep.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return signals;
	}

	public void uploadSignal (BitalinoSignal signal) {
		try {
			String sql = "INSERT INTO BitalinoSignal (id, patient_id, signal_duration, date_signal, filePath) VALUES (?,?,?,?,?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setInt(1,signal.getId());
			prep.setString(2, signal.getPatient_id());
			prep.setString(3, signal.getSignal_duration() );
			prep.setDate(4, Date.valueOf(signal.getDateSignal()));
			prep.setString(5, signal.getFilePath());
			prep.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}
