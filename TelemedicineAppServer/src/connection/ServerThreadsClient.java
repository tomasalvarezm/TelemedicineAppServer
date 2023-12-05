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


public class ServerThreadsClient implements Runnable {

	private Socket socket;
	private int byteRead;

	public ServerThreadsClient(Socket socket) {
		this.socket = socket;
	}
	
	
//TODO add lo referente a bitalino

//TODO revisar bien que hace esto. Nuestro server: manda patient, recibe patient y doctor, manda bitalino, recibe bitalino.
	
	@Override
	public void run() {
		
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		try {
			inputStream = socket.getInputStream();
			//outputStream = socket.getOutputStream();
			
			ArrayList<Integer> physioParamFromPatient = readDataFromBitalino(inputStream);
			System.out.println(physioParamFromPatient);
			
		} catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerThreadsClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            releaseResourcesClient(inputStream, socket);
        }
		
		//OutputStream os=null;
		
		/*public Patient readPatients() {
			
			Patient patient=null;
				
			Object object=null;
			try {
				object = objectInput.readObject();
				//TODO revisar excepciones
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//instanceof lo utilizamos para comprobar si el objeto recibido 
			//es de la clase que se indica posteriormente.
			if(object instanceof Patient) {
				patient= (Patient) object;
			}
			}
			return patient;
		}
		
		// TODO Auto-generated method stub
		//cuando server recibe y cuando manda
		try {
			
		}
		catch (Exception ex) {
			
		}*/
		
	}
	
	
	private ArrayList<Integer> readDataFromBitalino(InputStream inputStream) throws IOException, ClassNotFoundException{
		/*ArrayList<Integer> values = new ArrayList<Integer>();
		DataInputStream dataInput = new DataInputStream(inputStream);
		while((byteRead = inputStream.read()) != -1) {
			Integer value = (Integer) byteRead;
			values.add(value);
		}
		return values;*/
		ObjectInputStream objectInput = new ObjectInputStream(inputStream);
		ArrayList<Integer> data = (ArrayList<Integer>) objectInput.readObject();
		return data;
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
	
	/*private static void releaseResourcesClient(ObjectOutputStream objectOutput, ObjectInputStream objectInput, Socket socket) {
        try {
            objectInput.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            objectOutput.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}


