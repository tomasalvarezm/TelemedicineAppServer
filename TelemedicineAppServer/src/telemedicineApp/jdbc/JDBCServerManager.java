package telemedicineApp.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class JDBCServerManager {
	private JDBCManager m;
	private JDBCPatientManager pm;
//TODO probar este metodo
	public void saveData (int[] data, String patient_id, String signal_duration, LocalDate date) throws SQLException {
		int value=0;
		int i=0;
		//TODO ver si da error
		String sql= "INSERT INTO SignalRecord (patient_id,patient_name,signal_duration,date,data) VALUES (?,?,?,?,?)";
		PreparedStatement p = m.getConnection().prepareStatement(sql);
		p.setString(1, patient_id);
		p.setString(2, pm.getPatientNameById(patient_id));
		p.setString(3, signal_duration);
		p.setDate(4, Date.valueOf(date));
		while (i<data.length) {
			value = data[i];
			p.setInt(5, value);
			i++;
		}
		p.executeUpdate();		
		}
		
	}

