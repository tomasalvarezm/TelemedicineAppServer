package connection;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
	
	private ServerSocket serverSocket;
    //port assignation
    //TODO el port assignation no se usa en verdad (????)
    private final int port = 9000;
    
    
    //CONSTRUCTOR
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    //function for starting the server
    /*public void startServer() {
        try {
        	
        	//while the server socket is not closed, it is going to wait for clients and accept them.
            while (!this.serverSocket.isClosed()) {
                System.out.println("Waiting for clients :( ");
                Socket socket = this.serverSocket.accept();
                System.out.println("New connection :)");
                
                new Thread(new ServerThreadsClient(socket)).start(); //.start() invoca al metodo run de ServerThreadsClient
            }
        } catch (IOException ex) {
			//TODO revisar esto
        	ex.printStackTrace();
		} finally {
        	closeServerSockets(serverSocket);
        }

    }*/
    //TODO funcion para cerrar el servidor si se manda x o lo que sea 
    //ver ServerReceiveChaeractervia network


	
    //function for closing sockets
    public static void releaseResourcesServer(ServerSocket serverSocket) {
    
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) throws IOException {
    	
    	ServerSocket serverSocket = new ServerSocket(9000);
    	
    	try {
    		Socket clientSocket = serverSocket.accept();
    		Thread clientHandlerThread = new Thread(new ServerThreadsClient(clientSocket));
    		clientHandlerThread.start();
    	} finally {
    		releaseResourcesServer(serverSocket);
    	}
    	
    }
	
}
	
	


