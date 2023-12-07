package telemedicineApp.pojos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ServerThreadsClient;

public class Prueba {

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


}



