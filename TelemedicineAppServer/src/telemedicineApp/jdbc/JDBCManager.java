package telemedicineApp.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;


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
			System.out.println("Database connection closed for this client.");
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
			
			//Insert symptoms
			sql = "INSERT INTO Symptom (name) VALUES (?)";
			PreparedStatement prep = c.prepareStatement(sql);
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


			sql = "CREATE TABLE MedicalHistory" + "(id INTEGER UNIQUE, medication TEXT, date_medhist DATE, patient_id TEXT REFERENCES Patient(id), PRIMARY KEY(id AUTOINCREMENT))";
			stm.executeUpdate(sql);
			
			sql = "CREATE TABLE MedicalHistoryHasSymptoms" + "(medhist_id TEXT REFERENCES MedicalHistory(id),"
					+ " symptom_name TEXT REFERENCES Symptom(name), PRIMARY KEY (medhist_id, symptom_name))";
			stm.executeUpdate(sql);
			
			sql = "CREATE TABLE BitalinoSignal" + "(id INTEGER UNIQUE, patient_id TEXT REFERENCES Patient(id), signal_duration TEXT,"
					+ " date_signal DATE, filePath TEXT, PRIMARY KEY(id AUTOINCREMENT))";
			stm.executeUpdate(sql);
			
			//DEFAULT USERS TO DEMONSTRATE APP FUNCTIONALITY
			sql = "INSERT INTO Doctor " + "(id, name, sex) VALUES (?,?,?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "32356324T");
			prep.setString(2, "Maria Guzman");
			prep.setString(3, "FEMALE");
			prep.executeUpdate();
			
			sql = "INSERT INTO Doctor " + "(id, name, sex) VALUES (?,?,?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "32354875A");
			prep.setString(2, "Fernando Saenz");
			prep.setString(3, "MALE");
			prep.executeUpdate();
			
			sql = "INSERT INTO Patient " + "(id, name, email, dob, age, sex, phoneNumber, doctor_id) VALUES (?,?,?,?,?,?,?,?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "01236793Z");
			prep.setString(2, "Alejandro Perez");
			prep.setString(3, "alexperez@gmail.com");
			prep.setDate(4, Date.valueOf(LocalDate.of(1997, 11, 14)));
			prep.setInt(5,26);
			prep.setString(6, "MALE");
			prep.setInt(7, 678934506);
			prep.setString(8,"32356324T");
			prep.executeUpdate();
			
			sql = "INSERT INTO MedicalHistory " + "(medication, date_medhist,patient_id) VALUES (?,?,?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "LEVODOPA");
			prep.setDate(2, Date.valueOf(LocalDate.of(2023, 10, 25)));
			prep.setString(3, "01236793Z");
			prep.executeUpdate();
			
			sql = "INSERT INTO MedicalHistoryHasSymptoms " + "(medhist_id,symptom_name) VALUES (?,?)";
			prep = c.prepareStatement(sql);
			prep.setInt(1, 1);
			prep.setString(2, "Drowsiness");
			prep.executeUpdate();
			
			sql = "INSERT INTO MedicalHistoryHasSymptoms " + "(medhist_id,symptom_name) VALUES (?,?)";
			prep = c.prepareStatement(sql);
			prep.setInt(1, 1);
			prep.setString(2, "Nausea");
			prep.executeUpdate();
			
			sql = "INSERT INTO MedicalHistory " + "(medication, date_medhist,patient_id) VALUES (?,?,?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "LEVODOPA");
			prep.setDate(2, Date.valueOf(LocalDate.of(2023, 11, 10)));
			prep.setString(3, "01236793Z");
			prep.executeUpdate();
			
			sql = "INSERT INTO MedicalHistoryHasSymptoms " + "(medhist_id,symptom_name) VALUES (?,?)";
			prep = c.prepareStatement(sql);
			prep.setInt(1, 2);
			prep.setString(2, "Swelling");
			prep.executeUpdate();
			
			sql = "INSERT INTO MedicalHistoryHasSymptoms " + "(medhist_id,symptom_name) VALUES (?,?)";
			prep = c.prepareStatement(sql);
			prep.setInt(1, 2);
			prep.setString(2, "Nausea");
			prep.executeUpdate();
			
			sql = "INSERT INTO BitalinoSignal " + "(patient_id, signal_duration, date_signal, filePath) VALUES (?,?,?,?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, "01236793Z");
			prep.setString(2, "5 minutes 23 seconds");
			prep.setDate(3, Date.valueOf(LocalDate.of(2023, 11, 30)));
			prep.setString(4,"files\\01236793Z_2023-11-30.txt");
			prep.executeUpdate();
			
		} catch (SQLException e) {
			// Do not complain if tables already exist
			if (!e.getMessage().contains("already exists")) { 
				e.printStackTrace();
			}
		} 
		
	}
	
	
}
