package tcpConectivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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

    private BufferedOutputStream buf;

    private ObjectOutputStream bufferSender;
    // flag used to stop the read operation
    private boolean mRun;
    // used to notify certain user actions like receiving a message or disconnect
    
    private UserManagerDelegate managerDelegate;

    public UserManager(Socket socket, UserManagerDelegate managerDelegate ) {
        this.user = new User();
        this.socket = socket;
        mRun = true;
        this.managerDelegate =  managerDelegate;
      
    }

    private final Thread mOutputListener = new Thread() {
        /** this Runnable is for writing DnnMessage objects to the output stream
         * it waits for output messages on the output queue and sends them
         */
    	
        @Override
        public void run() {
            while(mRun == true){
                try {
                    DnnMessage fromServer = getUser().getUserToSendMessage();
                    if(fromServer != null){
                    	sendMessage(fromServer);
                    	managerDelegate.messageSent(user.getUsername(), fromServer);
                    }
                	
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };
    
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
        	buf = new BufferedOutputStream(socket.getOutputStream());
            bufferSender = new ObjectOutputStream(buf);
            bufferSender.flush();
            mOutputListener.start();
            //read the message received from client
            ObjectInputStream oInputStream = new ObjectInputStream(
            		new BufferedInputStream(socket.getInputStream()));
            while (mRun) {

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
                        managerDelegate.messageReceived(user);
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

        mRun = false;

        

        try {
        	if (bufferSender != null) bufferSender.close();
        	if (buf != null) buf.close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("S: User " + user.getUsername() + " left the room.");
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
        		System.out.println("message sent\n");
				bufferSender.writeObject(message);

				bufferSender.flush();
				bufferSender.reset();

				
			} catch (IOException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
            
        }
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
        public void messageReceived(User fromUser);

		public void messageSent(String userName, DnnMessage message);

    }


}
