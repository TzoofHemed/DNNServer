package tcpConectivity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import dnnUtil.dnnMessage.DnnHelloMessage;
import dnnUtil.dnnMessage.DnnMessage;
import messagesSwitch.MessagesSwitch;


//import javax.swing.JFrame;

public class TCPServer extends Thread implements UserManager.UserManagerDelegate {

    public static final int SERVERPORT = 2828;
    // while this is true the server will run
    private boolean mRun = false;
    // callback used to notify new messages received
    private OnMessageReceived messageListener;
    private ServerSocket serverSocket;
    private ArrayList<UserManager> connectedUsers;
    private MessagesSwitch mMessagesSwitch;
    private OnMessageSent messageSentListner;  
    
    


    /**
     * Constructor of the class
     *
     * @param onMessageReceived listens for the messages
     */
    public TCPServer(TCPServer.OnMessageReceived onMessageReceived,TCPServer.OnMessageSent onMessageSent) {
        this.messageListener = onMessageReceived;
        this.messageSentListner = onMessageSent;

        connectedUsers = new ArrayList<UserManager>();
        mMessagesSwitch = new MessagesSwitch(this);
    }
    
    public MessagesSwitch getMessageSwitch(){
    	return mMessagesSwitch;
    }
    
    
    public static void main(String[] args) {
    	System.out.println(System.getProperty("sun.arch.data.model"));
        //opens the window where the messages will be received and sent
    	SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final serverGUI.MainScreen frame = new serverGUI.MainScreen();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
    	});

    }

    public ArrayList<UserManager>getConnectedUserManagers(){
    	return this.connectedUsers;
    }
//    public static void main(String[] args) {
//
//        //opens the window where the messages will be received and sent
//        MainScreen frame = new MainScreen();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//
//    }

    /**
     * Close the server
     */
    public void close() {

        mRun = false;

        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("S: Done.");
        serverSocket = null;

    }

    /**
     * 
     * @param user a user object containing the message to be sent and also its username
     */
//    public void sendMessage() {
//
//        if (connectedUsers != null) {
//        	for(UserManager userManager : connectedUsers ){
//        		DnnMessage message = userManager.getUserOutputMessage();
//        		if(message != null){
//        			userManager.sendMessage(message);
//        		}
//        	}
//        }
//    }
    
    


    /**
     * Builds a new server connection
     */
    private void runServer() {
        mRun = true;
        UserManager userManager = null;
        Socket client =null;
        try {
            //create a server socket. A server socket waits for requests to come in over the network.
            serverSocket = new ServerSocket(SERVERPORT);
            
            while (mRun) {
                // create a loop and get all the incoming connections and create users with them
            	
                System.out.println("S: Waiting for a client ...");

                //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
                client = serverSocket.accept();

                userManager = new UserManager(client, this);
                // add the new user to the stack of users
                connectedUsers.add(userManager);

                // start reading messages from the client
                userManager.start();

                System.out.println("S: New client connected ...");
                
//                sendMessage();
            }

        }catch(SocketException se){
        	try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        	System.out.println("S: userManager is disconnected ...");
        }
        catch (Exception e) {
        	try {
        		connectedUsers.remove(userManager);
				client = serverSocket.accept();
                userManager = new UserManager(client, this);
                connectedUsers.add(userManager);
                userManager.start();
                System.out.println("S: New client connected ...");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            System.out.println("S: Error");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();

        runServer();

    }

    @Override
    public void userConnected(User connectedUser) {

    	DnnMessage message = new DnnHelloMessage(connectedUser.getUsername(), "");
    	
        messageListener.messageReceived(message);
        //for debug - TODO remove:
        System.out.println("user: " + connectedUser.getUsername() + " is connected. user Id: " + connectedUser.getUserID());
        mMessagesSwitch.getClientManager().addMessageToClient(connectedUser.getUsername(), message);
    }

    @Override
    public void userDisconnected(UserManager userManager) {
    	
        // remove the user from the list of connected users
        connectedUsers.remove(userManager);
        userManager.close();

    }

    @Override
    public void messageReceived(User fromUser) {
    	DnnMessage message = null;
		try {
			message = fromUser.getMessage();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	messageListener.messageReceived(message);
    	mMessagesSwitch.getClientManager().addInputMessageToClient(fromUser.getUsername(),message);
//    	System.out.println(fromUser.getUsername()+ ":  " + fromUser.getMessage().getContent());
//        messageListener.messageReceived("User " + fromUser.getUsername() + " says: " + fromUser.getMessage() + " to user: " + (toUser == null ? "ALL" : toUser.getUsername()));
        // send the message to the other clients

    }
    
    @Override
    public void messageSent(String userName, DnnMessage message){
    	messageSentListner.messageSent(userName, message);
    }

    //Declare the interface. The method messageReceived(String message) will/must be implemented in the ServerBoard
    //class at on startServer button click
    public interface OnMessageReceived {
        public void messageReceived(DnnMessage message);
        
    }
    public interface OnMessageSent{
    	public void messageSent(String userName, DnnMessage message);
    }

}
