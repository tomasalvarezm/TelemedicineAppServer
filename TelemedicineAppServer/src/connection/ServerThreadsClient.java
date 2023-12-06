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

import telemedicineApp.jdbc.JDBCManager;
import telemedicineApp.jdbc.JDBCPatientManager;
import telemedicineApp.pojos.Patient;

public class ServerThreadsClient implements Runnable {

	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;

	private JDBCManager dataBaseManager;
	private JDBCPatientManager patientManager;

	public ServerThreadsClient(Socket socket) {
		this.socket = socket;
		this.dataBaseManager = new JDBCManager();
		this.patientManager = new JDBCPatientManager(dataBaseManager);
	}

	@Override
	public void run() {
		//server sends and receives info
		inputStream = null;
		outputStream = null;
		objectOutput = null;
        objectInput = null;
		
		try {
			
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			objectInput = new ObjectInputStream(inputStream);
			objectOutput = new ObjectOutputStream(outputStream);
			
            switch(getRole(objectInput)){

            	//patient
                case 0: 
                	String function = getFunction(objectInput);
                	if (function.equals("register")) {
                		
                	}
                	if (function.equals("login")){
                		objectOutput.writeObject(getPatientFromID(objectInput));
                	}
                    break;
                
                //doctor     
                case 1:	
                	
            }
			
		} catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerThreadsClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            releaseResourcesClient(inputStream, socket);
        }
	}

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

	private int getRole(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String role = (String) objInput.readObject();
		if (role.equalsIgnoreCase("patient")) {
			return 0;
		} else
			return 1;

	}

	private String getFunction(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		return (String) objInput.readObject();
	}

	private Patient getPatientFromID(ObjectInputStream objInput) throws ClassNotFoundException, IOException {
		String id = (String) objInput.readObject();
		Patient patient = patientManager.getPatientById(id);
		return patient;
	}

}
