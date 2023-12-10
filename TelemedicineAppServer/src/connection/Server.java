package connection;


import java.awt.Color;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

public class Server extends JFrame{
	
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JScrollPane scrollPane;
	private JButton stop;
	private JPasswordField passwordField;
	
	private static ArrayList<ServerThreadsClient> connectedClients;
	private static ServerSocket serverSocket;
	private static Server frame;
	
	private final String adminPassword = "vamosaaprobar"; 
	private static boolean acceptingConnections = true;
	
    
    public static void main(String[] args) throws IOException {
    	
    	serverSocket = new ServerSocket(9000);
    	frame = new Server();
    	frame.setVisible(true);
    	
    	try {
    		while (acceptingConnections) {
    			Socket socket = serverSocket.accept();
    			
    			ServerThreadsClient serverThreadsClient = new ServerThreadsClient(socket);
    			new Thread(serverThreadsClient).start();
    			
    			connectedClients.add(serverThreadsClient);
    			
    		}
    	} catch(SocketException e){
    		if( !acceptingConnections) {
    			System.out.println("Server has closed");
    		} else {
    			e.printStackTrace();
    		}
    	} finally {
    		System.exit(0);
    	}
    }
	

    public static void releaseResourcesServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public Server() {
    	setTitle("Administrator");
		connectedClients = new ArrayList<ServerThreadsClient>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton refresh = new JButton("Refresh");
		refresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				model.setRowCount(0);
				for(int i = 0; i < connectedClients.size(); i++) {
					Object[] datos = new Object[] { connectedClients.get(i).getRole(), 
							connectedClients.get(i).getSocket().getLocalAddress(), 
							connectedClients.get(i).getSocket().getLocalPort()};
					model.addRow(datos);
				}
			}
		});
		refresh.setBounds(154, 33, 114, 23);
		contentPane.add(refresh);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 67, 362, 119);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		model = new DefaultTableModel() {
			public boolean isCellEditable(int fil, int col) {
				return false;
			}
		};
		model.addColumn("Role");
		model.addColumn("IP address");
		model.addColumn("Port");
		table.setModel(model);
		
		stop = new JButton("Stop server");
		stop.setToolTipText("Requires password");
		stop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(passwordField.getText().equals(adminPassword)) {
					
					acceptingConnections = false;
		    		releaseResourcesServer(serverSocket);
		    		frame.setVisible(false);
		    		
				} else {
					JOptionPane.showMessageDialog(Server.this, "Wrong password", "Message",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		stop.setBounds(260, 227, 114, 23);
		contentPane.add(stop);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(67, 250, 180, 2);
		contentPane.add(separator);
		
		passwordField = new JPasswordField();
		passwordField.setHorizontalAlignment(SwingConstants.RIGHT);
		passwordField.setToolTipText("Administrator password");
		passwordField.setBackground(new Color(240, 240, 240));
		passwordField.setBorder(null);
		passwordField.setBounds(67, 230, 180, 20);
		contentPane.add(passwordField);
		
		
		
		// CLOSING CONNECTION WHEN CLOSING FRAME
		WindowListener exitListener = (WindowListener) new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				if(passwordField.getText().equals(adminPassword)) {
					
		    		releaseResourcesServer(serverSocket);
		    		
				} else {
					JOptionPane.showMessageDialog(Server.this, "Server not closed !!", "Message",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		};
		this.addWindowListener(exitListener);
		
    }

    
    public static void removeConnectedClient(ServerThreadsClient serverThreadsClient) {
    	connectedClients.remove(serverThreadsClient);
    }
    
}
	
	


