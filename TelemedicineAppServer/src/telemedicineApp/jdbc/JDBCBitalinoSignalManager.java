package telemedicineApp.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import telemedicineApp.ifaces.BitalinoSignalManager;
import telemedicineApp.pojos.BitalinoSignal;

public class JDBCBitalinoSignalManager implements BitalinoSignalManager {

	private JDBCManager manager;
	

	
	public JDBCBitalinoSignalManager(JDBCManager manager) {
		this.manager = manager;
	}

	public BitalinoSignal getBitalinoSignal(String patient_id, LocalDate dateSignal) throws SQLException {
			BitalinoSignal signal = null;

			String sql = "SELECT * FROM BitalinoSignal WHERE patient_id = ? AND date_signal=?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, patient_id);
			prep.setDate(2,Date.valueOf(dateSignal));
			ResultSet rs = prep.executeQuery();
			Integer id = rs.getInt("id");
			String signal_duration = rs.getString("signal_duration");
			String filePath = rs.getString("filePath");
			signal = new BitalinoSignal (id, patient_id, signal_duration,dateSignal, filePath);
			rs.close();
			prep.close();

		
		return signal;
	}
	public ArrayList<BitalinoSignal> getSignalsByPatientId(String patient_id) throws SQLException {
		ArrayList<BitalinoSignal> signals = new ArrayList<BitalinoSignal>();

	
			String sql = "SELECT * FROM BitalinoSignal WHERE patient_id = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, patient_id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String signal_duration = rs.getString("signal_duration");
				LocalDate dateSignal = rs.getDate("date_signal").toLocalDate();
				String filePath = rs.getString("filePath");
				BitalinoSignal signal = new BitalinoSignal (id, patient_id, signal_duration,dateSignal, filePath);
				signals.add(signal);
			}
			rs.close();
			prep.close();

		
		return signals;
	}
	public void saveSignal (BitalinoSignal bs) throws SQLException{
	
		String sql= "INSERT INTO BitalinoSignal (patient_id,signal_duration,date_signal,filePath) VALUES (?,?,?,?)";
		PreparedStatement p= manager.getConnection().prepareStatement(sql);
		p.setString(1, bs.getPatient_id());
		p.setString(2, bs.getSignal_duration());
		p.setDate(3, Date.valueOf(bs.getDateSignal()));
		p.setString(4, bs.getFilePath());

		p.executeUpdate();	
		
		
	}
	
	public BitalinoSignal exportSignal (String id) throws SQLException {
		BitalinoSignal bs= new BitalinoSignal();
		
			String sql = "SELECT * FROM BitalinoSignal WHERE id = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, id);
			ResultSet rs = prep.executeQuery();
			bs.setPatient_id(rs.getString("patient_id"));
			bs.setSignal_duration(rs.getString("signal_duration"));
			bs.setDateSignal(Date.valueOf(rs.getString("date_signal")).toLocalDate());
			bs.setFilePath(rs.getString("filePath"));

			rs.close();
			prep.close();
		
		return bs;
	}



}
