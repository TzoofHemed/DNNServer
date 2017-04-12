package tcpConectivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import dnnUtil.dnnMessage.DnnMessage;


/**
 * Manages a single user(client) read/write operations
 * <p/>
 */
public class UserManager extends Thread {

    // contains information about the current user
    private User user;
    // the socket that links the user(client) to this server
    private Socket socket;
    private ObjectOutputStream bufferSender;
    // flag used to stop the read operation
    private boolean running;
    // used to notify certain user actions like receiving a message or disconnect
    
    private UserManagerDelegate managerDelegate;

    public UserManager(Socket socket, UserManagerDelegate managerDelegate ) {
        this.user = new User();
        this.socket = socket;
        running = true;
        this.managerDelegate =  managerDelegate;
      
    }

    public User getUser() {
        return user;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        super.run();

        System.out.println("S: Receiving...");

        try {

            //sends the message to the client
            bufferSender = new ObjectOutputStream(socket.getOutputStream());

            //read the message received from client
            ObjectInputStream oInputStream = new ObjectInputStream(socket.getInputStream());
            while (running) {

	            	Object messageObject = null;
	                try {
	                	messageObject = oInputStream.readObject();
	                } catch (IOException e) {
	                	if(e.getMessage() != null){
	                		System.out.println("Error reading message: " + e.getMessage());
	                	}
	                }
	
	                if(!(messageObject instanceof DnnMessage)){
	                	continue;
//	                	throw new Exception("Wrong message type received from client");
	                }
                    if (messageObject != null && managerDelegate != null) {
                        user.setMessage((DnnMessage)messageObject);
                        user.setUsername(((DnnMessage)messageObject).getSenderName());
                        // notify message received action
                        managerDelegate.messageReceived(user, null);
                    }
                    DnnMessage fromServer = getUserOutputMessage();
                    if(fromServer != null){
                    	sendMessage(fromServer);
                    }
	                
//	                mInputHandler.newMessageInput(socket.getInetAddress().getHostName(),(DnnMessage)messageObject);
	                
            }

        } catch (Exception e) {
            System.out.println("ServerError: " + e.getMessage());
            e.printStackTrace();
        }
    }
        
    /**
     * Close the server
     */
    public void close() {

        running = false;

        if (bufferSender != null) {
            bufferSender = null;
        }

        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("S: User " + user.getUsername() + " leaved the room.");
        socket = null;

        //todo close all user connections

    }

    /**
     * Method to send the messages from server to client
     *
     * @param message the message sent by the server
     */
    public void sendMessage(DnnMessage message) {
        if (bufferSender != null ) {
        	try {
				bufferSender.writeObject(message);
			} catch (IOException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
            
        }
    }
    
    public DnnMessage getUserOutputMessage(){
    	return this.getUser().getUserToSendMessage();
    }

    public void setUserOutputMessage(DnnMessage message){
    	this.getUser().setUserToSendMessage(message);
    }

    /**
     * Used to talk with the TcpServer class or whoever wants to receive notifications from this manager
     */
    public interface UserManagerDelegate {

        /**
         * Called whenever a user is connected to the server
         *
         * @param connectedUser the connected user
         */
        public void userConnected(User connectedUser);

        /**
         * Called when a user is disconnected from the server
         *
         * @param userManager the manager of the disconnected user
         */
        public void userDisconnected(UserManager userManager);

        /**
         * Called when the manager receives a new message from the client
         *
         * @param fromUser the user that sent the message
         * @param toUser   the user that should receive the message
         */
        public void messageReceived(User fromUser, User toUser);

    }


}
