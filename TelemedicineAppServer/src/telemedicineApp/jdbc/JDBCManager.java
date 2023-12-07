package telemedicineApp.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JCheckBox;

public class JDBCManager {

	private Connection c = null;
	
	public JDBCManager() {
		try {
			// Open database connection
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:./db/telemedicine.db");
			c.createStatement().execute("PRAGMA foreign_keys=ON");
			System.out.println("Database connection opened.");

			// Create tables
			this.createTables();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Libraries not loaded");
		}
	}

	public void disconnect() {
		try {
			c.close();
			System.out.println("Database connection closed.");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public Connection getConnection() {
		return c;
	}

	private void createTables() {
		// Create Tables
		try {
			Statement stm = c.createStatement();
			
			String sql = "CREATE TABLE Doctor" + "(id TEXT UNIQUE, name TEXT, sex TEXT, PRIMARY KEY(id))";
			stm.executeUpdate(sql);
			
			sql = "CREATE TABLE Patient" + "(id TEXT UNIQUE, name TEXT, email TEXT UNIQUE,"
					+ "dob DATE, age INTEGER, sex TEXT, phoneNumber INTEGER,  "
					+ "doctor_id TEXT REFERENCES Doctor(id), PRIMARY KEY(id))";
			stm.executeUpdate(sql);
		
			sql = "CREATE TABLE Symptom" + "(name TEXT UNIQUE, PRIMARY KEY(name))";
			stm.executeUpdate(sql);
			
			sql = "CREATE TABLE MedicalHistory" + "(id INTEGER UNIQUE, medication TEXT, date_medhist DATE, patient_id TEXT REFERENCES Patient(id), PRIMARY KEY(id AUTOINCREMENT))";
			stm.executeUpdate(sql);
			
			sql = "CREATE TABLE MedicalHistoryHasSymptoms" + "(medhist_id TEXT REFERENCES MedicalHistory(id),"
					+ " symptom_name TEXT REFERENCES Symptom(name), PRIMARY KEY (medhist_id, symptom_name))";
			stm.executeUpdate(sql);
			
			sql = "CREATE TABLE BitalinoSignal" + "(id INTEGER UNIQUE, patient_id TEXT REFERENCES Patient(id), signal_duration TEXT,"
					+ " date_signal DATE, filePath TEXT, PRIMARY KEY(id AUTOINCREMENT))";
			stm.executeUpdate(sql);
			
			//FOR TESTING
			sql = "INSERT INTO Doctor " + "(id, name, sex) VALUES (?,?,?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, "323563245T");
			prep.setString(2, "Elena");
			prep.setString(3, "FEMALE");
			prep.executeUpdate();
			
			//Insert symptoms
			sql = "INSERT INTO Symptom (name) VALUES (?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "Drowsiness");
			prep.executeUpdate();
			
			sql = "INSERT INTO Symptom (name) VALUES (?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "Nausea");
			prep.executeUpdate();
			
			sql = "INSERT INTO Symptom (name) VALUES (?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "Swelling");
			prep.executeUpdate();
			
			sql = "INSERT INTO Symptom (name) VALUES (?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "Dizziness");
			prep.executeUpdate();

			sql = "INSERT INTO Symptom (name) VALUES (?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "Alucinations");
			prep.executeUpdate();

			sql = "INSERT INTO Symptom (name) VALUES (?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "Lack of appetite");
			prep.executeUpdate();


		} catch (SQLException e) {
			// Do not complain if tables already exist
			if (!e.getMessage().contains("already exists")) { 
				e.printStackTrace();
			}
		} 
		
	}
	
	
}
