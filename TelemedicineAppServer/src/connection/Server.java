package connection;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
	

   
    
    public static void main(String[] args) throws IOException {
    	
    	ServerSocket serverSocket = new ServerSocket(9000);
    	
    	try {
    		while (true) {
    			
    			Socket socket = serverSocket.accept();
    			new Thread(new ServerThreadsClient(socket)).start();

    			
    		}
    	} finally {
    		releaseResourcesServer(serverSocket);
    	}
    	
    }
	


    public static void releaseResourcesServer(ServerSocket serverSocket) {
        
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
	
	


