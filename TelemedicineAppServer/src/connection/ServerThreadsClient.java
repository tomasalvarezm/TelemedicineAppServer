package connection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import telemedicineApp.jdbc.JDBCBitalinoSignalManager;
import telemedicineApp.jdbc.JDBCDoctorManager;
import telemedicineApp.jdbc.JDBCManager;
import telemedicineApp.jdbc.JDBCMedicalHistoryManager;
import telemedicineApp.jdbc.JDBCPatientManager;
import telemedicineApp.jdbc.JDBCSymptomManager;
import telemedicineApp.pojos.BitalinoSignal;
import telemedicineApp.pojos.MedicalHistory;
import telemedicineApp.pojos.Patient;

public class ServerThreadsClient implements Runnable {

	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;
	
	private boolean connection;

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
		this.connection = true;
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
				while(connection) {
					switch (getFunction(objectInput)) {
					
					//register
					case 0:
						objectOutput.writeBoolean(registerPatient(objectInput));
						break;
					
					//login
					case 1:
						objectOutput.writeObject(getPatientFromID(objectInput));
						boolean login = true;
						while(login) {
							switch(getPatientFunction(objectInput)) {
							
							//new medical history
							case 0:
								objectOutput.writeBoolean(uploadSymptoms(objectInput));
								break;
								
							//new BITalino signal
							case 1:
								objectOutput.writeBoolean(saveBitalinoSignal(objectInput));
								break;
								
							//logout
							case 2:
								login = false;
								connection = false;
								break;
							}
						}
						break;
					}
				}
				break;	
				
			// doctor
			case 1:
				
				break;
			}
			//releaseResources();

		} catch (IOException | ClassNotFoundException ex) {
			Logger.getLogger(ServerThreadsClient.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			releaseResourcesClient(inputStream, socket);
		}
	}

	// CLOSE CONNECTION
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

	// CLIENT ROLE
	private int getRole(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String role = (String) objInput.readObject();
		if (role.equalsIgnoreCase("patient")) {
			return 0;
		} else
			return 1;
	}

	// CLIENT ACTION
	private int getFunction(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String function = (String) objInput.readObject();
		if (function.equalsIgnoreCase("register")) {
			return 0;
		} else
			return 1;
	}

	private int getPatientFunction(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String function = (String) objInput.readObject();
		if (function.equalsIgnoreCase("modifysymptoms")) {
			return 0;
		} else if (function.equalsIgnoreCase("uploadsignal")) {
			return 1;
		} else
			return 2;

	}

	// PATIENT FUNCTIONALITIES
	private boolean registerPatient(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		Patient patient = (Patient) objInput.readObject();
		patientManager.insertPatient(patient);
		return true; // method insertPatient(patient) should return true instead??
	}

	private Patient getPatientFromID(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String id = (String) objInput.readObject();
		Patient patient = patientManager.getPatientById(id);
		return patient;
	}

	private boolean saveBitalinoSignal(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		BitalinoSignal bitalinoSignal = (BitalinoSignal) objInput.readObject();
		String pathname = "/Users/elenamacarron/Desktop/TelemedicineProject/TelemedicineAppServer/"
				+ bitalinoSignal.getPatient_id() + bitalinoSignal.getDateSignal();
		bitalinoSignal.setFilePath(pathname);
		File file = new File(bitalinoSignal.getFilePath());
		FileWriter fileWriter = new FileWriter(file);
		for (Integer value : bitalinoSignal.getData()) {
			fileWriter.write(value);
		}
		fileWriter.close();
		bitalinoSignalManager.saveSignal(bitalinoSignal);
		return true;
	}

	private boolean uploadSymptoms(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		MedicalHistory medicalHistory = (MedicalHistory) objectInput.readObject();
		medicalHistoryManager.uploadMedicalHistory(medicalHistory);
		return true;
	}

	// DOCTOR FUNCITONALITIES

}
