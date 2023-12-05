package telemedicineApp.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import telemedicineApp.ifaces.SymptomManager;
import telemedicineApp.pojos.*;

public class JDBCSymptomManager implements SymptomManager{
	
	private JDBCManager manager;

	public JDBCSymptomManager(JDBCManager m) {
		this.manager = m;
	}
	
	public void insertSymptom(Symptom s){
        try {
        String sql = "INSERT INTO Symptom " + "(name,date) VALUES (?,?)";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setString(1, s.getName());
        prep.setDate(2, Date.valueOf(s.getDate()));
        prep.executeUpdate();
        
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
	//TODO mirar mas tarde. Funciona como tal
	public Symptom getSymptomByName(String name) {
		Symptom symp = null;
		try {
			String sql = "SELECT * FROM Symptom WHERE name = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, name);
			ResultSet rs = prep.executeQuery();
			Date date = rs.getDate("date");
			symp = new Symptom(name,date.toLocalDate());
			rs.close();
			prep.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return symp;
	}
	//TODO probar este metodo. Hay que darle una vuelta
	public ArrayList<Symptom> getSymptomsFromPatientId(String id) {
		ArrayList<Symptom> syms = new ArrayList<Symptom>();

		try {
			String sql = "SELECT symptom_name FROM PatientHasSymptoms WHERE patient_id = ?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, id);
			ResultSet rs = prep.executeQuery();
			while(rs.next()) {
			String name = rs.getString("symptom_name");
			Symptom symp = this.getSymptomByName(name);
			syms.add(symp);
			}
			rs.close();
			prep.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return syms;
	}
	
	
	

}
