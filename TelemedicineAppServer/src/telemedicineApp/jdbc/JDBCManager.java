package telemedicineApp.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


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
			String sql = "CREATE TABLE Patient" + "(id TEXT UNIQUE, name TEXT, email TEXT UNIQUE,"
					+ " age INTEGER, sex TEXT, phoneNumber INTEGER, medication TEXT, doctor_id TEXT REFERENCES Doctor(id), PRIMARY KEY(id))";
			stm.executeUpdate(sql);
			
			sql = "CREATE TABLE Symptom" + "(name TEXT,date DATE, PRIMARY KEY(name))";
			stm.executeUpdate(sql);
			
			sql = "CREATE TABLE PatientHasSymptoms" + "(patient_id TEXT REFERENCES Patient(id),"
					+ " symptom_name TEXT REFERENCES Symptom(name), PRIMARY KEY (patient_id, symptom_name))";
			stm.executeUpdate(sql);
			
			
			sql = "CREATE TABLE SignalRecord" + "(signal_id INTEGER UNIQUE, patient_id TEXT, patient_name TEXT, signal_duration TEXT,"
					+ " date DATE, data BLOB, PRIMARY KEY(signal_id AUTOINCREMENT))";
			stm.executeUpdate(sql);
			
			sql = "CREATE TABLE Doctor" + "(id TEXT UNIQUE, PRIMARY KEY(id))";
			stm.executeUpdate(sql);
			


		} catch (SQLException e) {
			// Do not complain if tables already exist
			if (!e.getMessage().contains("already exists")) { 
				e.printStackTrace();
			}
		}
		
	}
	
	
}
