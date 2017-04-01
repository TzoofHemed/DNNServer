package TCPConectivity;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;

import MessagesSwitch.MessagesSwitch;
import dnn.message.DnnHelloMessage;
import dnn.message.DnnMessage;


//import javax.swing.JFrame;

public class TCPServer extends Thread implements UserManager.UserManagerDelegate {

    public static final int SERVERPORT = 2828;
    // while this is true the server will run
    private boolean running = false;
    // callback used to notify new messages received
    private OnMessageReceived messageListener;
    private ServerSocket serverSocket;
    private ArrayList<UserManager> connectedUsers;
    private MessagesSwitch mMessagesSwitch;

    /**
     * Constructor of the class
     *
     * @param onMessageReceived listens for the messages
     */
    public TCPServer(TCPServer.OnMessageReceived onMessageReceived, MessagesSwitch messagesSwitch) {
        this.messageListener = onMessageReceived;
        connectedUsers = new ArrayList<UserManager>();
        mMessagesSwitch = messagesSwitch;
    }
    

    public static void main(String[] args) {

        //opens the window where the messages will be received and sent
        ServerGUI.MainScreen frame = new ServerGUI.MainScreen();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

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

        running = false;

        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("S: Done.");
        serverSocket = null;

    }

    /**
     * Method to send the messages from server to all clients
     *
     * @param user a user object containing the message to be sent and also its username
     */
    public void sendMessage(User user) {	//TODO implement this message for a real DNN SERVER!

        if (connectedUsers != null) {

            for (UserManager userManager : connectedUsers) {
                if (userManager.getUser().getUserID() != user.getUserID()) {
                    userManager.sendMessage(user.getUsername() + " says: " + user.getMessage());
                }
            }

        }

    }

    /**
     * Builds a new server connection
     */
    private void runServer() {
        running = true;

        try {
            //create a server socket. A server socket waits for requests to come in over the network.
            serverSocket = new ServerSocket(SERVERPORT);

            while (running) {
                // create a loop and get all the incoming connections and create users with them

                System.out.println("S: Waiting for a client ...");

                //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
                Socket client = serverSocket.accept();

                UserManager userManager = new UserManager(client, this);
                // add the new user to the stack of users
                connectedUsers.add(userManager);

                // start reading messages from the client
                userManager.start();

                System.out.println("S: New client connected ...");
            }

        } catch (Exception e) {
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

    }

    @Override
    public void messageReceived(User fromUser, User toUser) {
    	mMessagesSwitch.getClientManager().addMessageToClient(fromUser.getUsername(),fromUser.getMessage());
    	System.out.println("user: " + fromUser.getMessage().getContent());
//        messageListener.messageReceived("User " + fromUser.getUsername() + " says: " + fromUser.getMessage() + " to user: " + (toUser == null ? "ALL" : toUser.getUsername()));
        // send the message to the other clients
    	

    }

    //Declare the interface. The method messageReceived(String message) will/must be implemented in the ServerBoard
    //class at on startServer button click
    public interface OnMessageReceived {
        public void messageReceived(DnnMessage message);
    }

}
