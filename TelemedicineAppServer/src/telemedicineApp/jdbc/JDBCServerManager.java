package telemedicineApp.jdbc;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import telemedicineApp.ifaces.ServerManager;
import telemedicineApp.pojos.BitalinoSignal;

public class JDBCServerManager implements ServerManager {
	private JDBCManager m;
	
	public JDBCServerManager(JDBCManager m) {
		super();
		this.m = m;
	}

	
	public void saveSignal (BitalinoSignal bs) {
		try {
		String sql= "INSERT INTO BitalinoSignal (patient_id,signal_duration,date_signal,filePath) VALUES (?,?,?,?)";
		PreparedStatement p= m.getConnection().prepareStatement(sql);
		p.setString(1, bs.getPatient_id());
		p.setString(2, bs.getSignal_duration());
		p.setDate(3, Date.valueOf(bs.getDateSignal()));
		p.setString(4, bs.getFilePath());

		p.executeUpdate();	
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public BitalinoSignal exportSignal (String id) {
		BitalinoSignal bs= new BitalinoSignal();
		try {
			String sql = "SELECT * FROM BitalinoSignal WHERE id = ?";
			PreparedStatement prep = m.getConnection().prepareStatement(sql);
			prep.setString(1, id);
			ResultSet rs = prep.executeQuery();
			bs.setPatient_id(rs.getString("patient_id"));
			bs.setSignal_duration(rs.getString("signal_duration"));
			bs.setDateSignal(Date.valueOf(rs.getString("date_signal")).toLocalDate());
			bs.setFilePath(rs.getString("filePath"));

			rs.close();
			prep.close();
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		return bs;
	}

}

