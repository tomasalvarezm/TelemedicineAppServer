package telemedicineApp.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import telemedicineApp.ifaces.SymptomManager;
import telemedicineApp.pojos.*;

public class JDBCSymptomManager implements SymptomManager{
	
	private JDBCManager manager;

	public JDBCSymptomManager(JDBCManager m) {
		this.manager = m;
	}
	
	public ArrayList<Symptom> getSymptomsFromMedHistId(Integer mh_id) throws SQLException {
		ArrayList<Symptom> symptoms = new ArrayList<Symptom>();

		
			String sql = "SELECT symptom_name FROM MedicalHistoryHasSymptoms WHERE medhist_id = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setInt(1, mh_id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				String name = rs.getString("symptom_name");
				Symptom symp = new Symptom (name);
				symptoms.add(symp);
			}
			rs.close();
			prep.close();

		
		return symptoms;
	}
	

	public void uploadSymptomsToMedicalHistory(Integer medhist_id, ArrayList<Symptom> symptoms) throws SQLException {
		for(Symptom s : symptoms) {
		
				String sql = "INSERT INTO MedicalHistoryHasSymptoms (medhist_id, symptom_name) VALUES (?,?)";
				PreparedStatement prep = manager.getConnection().prepareStatement(sql);
				prep.setInt(1, medhist_id);
				prep.setString(2, s.getName());
				prep.executeUpdate();

	
		}
	}

}
