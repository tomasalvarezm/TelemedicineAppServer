package connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.time.LocalDate;
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
import telemedicineApp.pojos.Doctor;
import telemedicineApp.pojos.MedicalHistory;
import telemedicineApp.pojos.Patient;
import telemedicineApp.pojos.Sex;

public class ServerThreadsClient implements Runnable {

	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;

	private String role;
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

			// PATIENT
			case 0:
				while (connection) {
					switch (getFunction(objectInput)) {

					// register
					case 0:
						
						// doctors name shown in JComboBox
						try {
							objectOutput.writeObject(doctorManager.getAllDoctors());
							objectOutput.flush();
						} catch (SQLException e1) {
							objectOutput.writeObject(null);
							objectOutput.flush();
						}
						
						// checks if the patient press back button
						if(goesBack(objectInput)) {
							break;
						}
						
						// doctor chosen
						try {
							objectOutput.writeObject(getDoctorByName(objectInput));
							objectOutput.flush();
						} catch (SQLException e1) {
							objectOutput.writeObject(null);
							objectOutput.flush();
						}
						
						// insert patient to database
						objectOutput.writeBoolean(registerPatient(objectInput));
						objectOutput.flush();
						break;

					// login
					case 1:
						
						// checking the patient in the database
						try {
							objectOutput.writeObject(getPatientFromID(objectInput));
							objectOutput.flush();
						} catch (SQLException e) {
							objectOutput.writeObject(null);
							objectOutput.flush();
							break;
						}

						boolean login = true;
						while (login) {
							switch (getPatientFunction(objectInput)) {

							// new medical history
							case 0:
								objectOutput.writeBoolean(uploadSymptoms(objectInput));
								objectOutput.flush();
								break;

							// new BITalino signal
							case 1:
								objectOutput.writeBoolean(saveBitalinoSignal(objectInput));
								objectOutput.flush();
								break;

							// logout
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

			// DOCTOR
			case 1:
				while (connection) {
					switch (getFunction(objectInput)) {

					// register
					case 0:
						
						// checks if the doctor press back button
						if(goesBack(objectInput)) {
							break;
						}
						
						// insert doctor into database
						objectOutput.writeBoolean(registerDoctor(objectInput));
						objectOutput.flush();
						break;

					// login
					case 1:
						
						// checking the doctor in the database
						try {
							objectOutput.writeObject(getDoctorFromID(objectInput));
							objectOutput.flush();
						} catch (SQLException e) {
							objectOutput.writeObject(null);
							objectOutput.flush();
							break;
						}

						// patients shown in table
						try {
							objectOutput.writeObject(getPatients(objectInput));
							objectOutput.flush();
						} catch (SQLException e1) {
							e1.printStackTrace();
							objectOutput.writeObject(null);
							objectOutput.flush();
						}

						boolean login = true;
						while (login) {
							switch (getDoctorFunction(objectInput)) {

							// see medical history
							case 0:

								// patient chosen
								try {
									objectOutput.writeObject(getPatientFromID(objectInput));
									objectOutput.flush();
								} catch (SQLException e1) {
									objectOutput.writeObject(null);
									objectOutput.flush();
								}

								// all medical history of selected patient
								try {
									objectOutput.writeObject(getAllMedicalHistory(objectInput));
									objectOutput.flush();
								} catch (SQLException e1) {
									objectOutput.writeObject(null);
									objectOutput.flush();
								}
								while (isSamePatient(objectInput)) {
									
									//sends the medical history
									try {
										objectOutput.writeObject(getMedicalHistory(objectInput));
										objectOutput.flush();
									} catch (SQLException e) {
										objectOutput.writeObject(null);
										objectOutput.flush();
									}
								}
								break;

							// see BITalino signal
							case 1:

								// patient chosen
								try {
									objectOutput.writeObject(getPatientFromID(objectInput));
									objectOutput.flush();
								} catch (SQLException e1) {
									objectOutput.writeObject(null);
									objectOutput.flush();
								}

								// all BITalino signals of selected patient
								try {
									objectOutput.writeObject(getBitalinoSignals(objectInput));
									objectOutput.flush();
								} catch (SQLException e1) {
									objectOutput.writeObject(null);
									objectOutput.flush();
								}
								while (isSamePatient(objectInput)) {
									
									//sends the BITalino signal
									try {
										objectOutput.writeObject(getBitalinoSignal(objectInput));
										objectOutput.flush();
									} catch (SQLException e) {
										objectOutput.writeObject(null);
										objectOutput.flush();
									}
								}
								break;

							// logout
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
			}

		} catch (SocketException | EOFException e) {
			System.out.println("Client has closed connection");
		} catch (IOException | ClassNotFoundException ex){
			Logger.getLogger(ServerThreadsClient.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			Server.removeConnectedClient(this);
			releaseResourcesClient(inputStream, objectInput, outputStream, objectOutput, socket);
			releaseResourcesDataBase(dataBaseManager);
			System.out.println("Server has closed this client streams");
		}
	}

	// CLOSE CONNECTION
	private static void releaseResourcesClient(InputStream inputStream, ObjectInputStream objectInput,
			OutputStream outputStream, ObjectOutputStream objectOutput, Socket socket) {
		try {
			inputStream.close();
			objectInput.close();

			outputStream.close();
			objectOutput.close();
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			socket.close();
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void releaseResourcesDataBase(JDBCManager dataBaseManager) {
		dataBaseManager.disconnect();
	}

	// CLIENT ROLE
	private int getRole(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String role = (String) objInput.readObject();
		this.role = role;
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

	private int getDoctorFunction(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String function = (String) objInput.readObject();
		if (function.equalsIgnoreCase("medicalhistory")) {
			return 0;
		} else if (function.equalsIgnoreCase("signal")) {
			return 1;
		} else
			return 2;
	}

	private boolean goesBack(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String back = (String) objInput.readObject();
		if (back.equalsIgnoreCase("back")) {
			return true;
		} else {
			return false;
		}
	}

	// PATIENT FUNCTIONALITIES
	private boolean registerPatient(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		Patient patient = (Patient) objInput.readObject();
		try {
			patientManager.insertPatient(patient);
		} catch (SQLException ex) {
			return false;
		}
		return true;
	}

	private Patient getPatientFromID(ObjectInputStream objInput)
			throws ClassNotFoundException, IOException, SQLException {
		String id = (String) objInput.readObject();
		Patient patient = patientManager.getPatientById(id);
		return patient;
	}
	
	private Doctor getDoctorByName(ObjectInputStream objInput) throws ClassNotFoundException, IOException, SQLException {
		String name = (String) objInput.readObject();
		Doctor doctor = doctorManager.getDoctorByName(name);
		return doctor;
	}

	private boolean saveBitalinoSignal(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		BitalinoSignal bitalinoSignal = (BitalinoSignal) objInput.readObject();
		String pathname = "files\\" + bitalinoSignal.getPatient_id() + "_" + bitalinoSignal.getDateSignal() + ".txt";
		bitalinoSignal.setFilePath(pathname);
		File file = new File(bitalinoSignal.getFilePath());
		PrintWriter printWriter = new PrintWriter(file);
		BufferedWriter bw = new BufferedWriter(printWriter);
		for (Integer value : bitalinoSignal.getData()) {
			bw.write(value.toString());
			bw.write("\n");
		}
		bw.close();
		printWriter.close();
		try {
			bitalinoSignalManager.saveSignal(bitalinoSignal);
		} catch (SQLException ex) {
			return false;
		}
		return true;
	}

	private boolean uploadSymptoms(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		MedicalHistory medicalHistory = (MedicalHistory) objectInput.readObject();
		try {
			medicalHistoryManager.uploadMedicalHistory(medicalHistory);
		} catch (SQLException ex) {
			return false;
		}
		return true;
	}

	// DOCTOR FUNCITONALITIES
	private boolean registerDoctor(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		Doctor doctor = (Doctor) objInput.readObject();
		try {
			doctorManager.insertDoctor(doctor);
		} catch (SQLException ex) {
			return false;
		}
		return true;
	}

	private Doctor getDoctorFromID(ObjectInputStream objInput)
			throws ClassNotFoundException, IOException, SQLException {
		String id = (String) objInput.readObject();
		Doctor doctor = doctorManager.getDoctorById(id);
		return doctor;
	}

	private ArrayList<Patient> getPatients(ObjectInputStream objInput)
			throws ClassNotFoundException, IOException, SQLException {
		String doctorID = (String) objInput.readObject();
		ArrayList<Patient> patients = doctorManager.listPatientsByDoctorId(doctorID);
		return patients;
	}

	private ArrayList<MedicalHistory> getAllMedicalHistory(ObjectInputStream objInput)
			throws ClassNotFoundException, IOException, SQLException {
		String patientID = (String) objInput.readObject();
		ArrayList<MedicalHistory> allMedicalHistory = medicalHistoryManager.getAllMedicalHistoryByPatientId(patientID);
		return allMedicalHistory;
	}

	private ArrayList<BitalinoSignal> getBitalinoSignals(ObjectInputStream objInput)
			throws ClassNotFoundException, IOException, SQLException {
		String patientID = (String) objInput.readObject();
		ArrayList<BitalinoSignal> bitalinoSignals = bitalinoSignalManager.getSignalsByPatientId(patientID);
		return bitalinoSignals;
	}

	private boolean isSamePatient(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String function = (String) objInput.readObject();
		if (function.equalsIgnoreCase("samepatient")) {
			return true;
		} else
			return false;
	}

	private MedicalHistory getMedicalHistory(ObjectInputStream objInput)
			throws ClassNotFoundException, IOException, SQLException {
		String patientID = (String) objInput.readObject();
		LocalDate date = (LocalDate) objInput.readObject();
		MedicalHistory medicalHistory = medicalHistoryManager.getMedicalHistory(patientID, date);
		return medicalHistory;
	}

	private BitalinoSignal getBitalinoSignal(ObjectInputStream objInput)
			throws ClassNotFoundException, IOException, SQLException {
		String patientID = (String) objInput.readObject();
		LocalDate date = (LocalDate) objInput.readObject();
		BitalinoSignal bitalinoSignal = bitalinoSignalManager.getBitalinoSignal(patientID, date);

		File file = new File(bitalinoSignal.getFilePath());
		FileReader fileReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fileReader);
		String line;
		while ((line = reader.readLine()) != null) {
			bitalinoSignal.getData().add(Integer.parseInt(line));
		}
		reader.close();
		return bitalinoSignal;
	}

	
	
	// GETTERS
	public Socket getSocket() {
		return socket;
	}

	public String getRole() {
		return role;
	}
	
	
}
