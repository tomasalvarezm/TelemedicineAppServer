package connection;

import java.io.DataInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import telemedicineApp.jdbc.JDBCBitalinoSignalManager;
import telemedicineApp.jdbc.JDBCDoctorManager;
import telemedicineApp.jdbc.JDBCManager;
import telemedicineApp.jdbc.JDBCMedicalHistoryManager;
import telemedicineApp.jdbc.JDBCPatientManager;
import telemedicineApp.jdbc.JDBCSymptomManager;
import telemedicineApp.pojos.BitalinoSignal;
import telemedicineApp.pojos.Patient;
import telemedicineApp.pojos.Symptom;

public class ServerThreadsClient implements Runnable {

	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;

	private JDBCManager dataBaseManager;
	private JDBCPatientManager patientManager;
	private JDBCDoctorManager doctorManager;
	private JDBCSymptomManager symptomManager;
	private JDBCMedicalHistoryManager medicalHistoryManager;
	private JDBCBitalinoSignalManager bitalinoSignalManager;

	public ServerThreadsClient(Socket socket) {
		this.socket = socket;
		this.dataBaseManager = new JDBCManager();
		this.symptomManager = new JDBCSymptomManager(dataBaseManager);
		this.medicalHistoryManager = new JDBCMedicalHistoryManager(dataBaseManager, symptomManager);
		this.bitalinoSignalManager = new JDBCBitalinoSignalManager(dataBaseManager);
		this.doctorManager = new JDBCDoctorManager(dataBaseManager, medicalHistoryManager, bitalinoSignalManager);
		this.patientManager = new JDBCPatientManager(dataBaseManager, doctorManager, medicalHistoryManager,
				bitalinoSignalManager);
	}

	@Override
	public void run() {
		// server sends and receives info
		inputStream = null;
		outputStream = null;
		objectOutput = null;
		objectInput = null;

		try {

			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			objectInput = new ObjectInputStream(inputStream);
			objectOutput = new ObjectOutputStream(outputStream);

			switch (getRole(objectInput)) {

			// patient
			case 0:
				String function = getFunction(objectInput);

				if (function.equals("register")) {
					objectOutput.writeBoolean(registerPatient(objectInput));
				}
				if (function.equals("login")) {
					objectOutput.writeObject(getPatientFromID(objectInput));
					String function2 = getFunction(objectInput);

					if (function2.equals("modifysymptoms")) {
						objectOutput.writeBoolean(uploadSymptoms(objectInput));
					}
					if (function2.equals("uploadsignal")) {
						objectOutput.writeBoolean(saveBitalinoSignal(objectInput));
					}
				}
				break;

			// doctor
			case 1:

			}

		} catch (IOException | ClassNotFoundException ex) {
			Logger.getLogger(ServerThreadsClient.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			releaseResourcesClient(inputStream, socket);
		}
	}

	//CLOSE CONNECTION
	private static void releaseResourcesClient(InputStream inputStream, Socket socket) {
		try {
			inputStream.close();
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			socket.close();
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	//CLIENT ROLE 
	private int getRole(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String role = (String) objInput.readObject();
		if (role.equalsIgnoreCase("patient")) {
			return 0;
		} else
			return 1;
	}

	//CLIENT ACTION
	private String getFunction(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		return (String) objInput.readObject();
	}

	//PATIENT FUNCTIONALITIES
	private boolean registerPatient(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		Patient patient = (Patient) objInput.readObject();
		patientManager.insertPatient(patient);
		return true; // insertPatient(patient) should return true instead??
	}

	private Patient getPatientFromID(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String id = (String) objInput.readObject();
		Patient patient = patientManager.getPatientById(id);
		return patient;
	}
	
	private boolean saveBitalinoSignal(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		BitalinoSignal bitalinoSignal = (BitalinoSignal) objInput.readObject();
		bitalinoSignalManager.saveSignal(bitalinoSignal);
		return true;
	}
	
	private boolean uploadSymptoms(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		Integer medHistID = (Integer) objectInput.readObject();
		ArrayList<Symptom> symptoms = (ArrayList<Symptom>) objInput.readObject();
		symptomManager.uploadSymptomsToMedicalHistory(medHistID, symptoms);
		return true;
	}
	
	//DOCTOR FUNCITONALITIES

}
