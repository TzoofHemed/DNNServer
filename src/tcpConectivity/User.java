package tcpConectivity;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import dnnUtil.dnnMessage.DnnMessage;

/**
 * Encapsulates a user entity, having its port and username
 * <p/>
 */
public class User {

    private String username;
    private final BlockingQueue<DnnMessage> userInputMessages;
    private final BlockingQueue<DnnMessage> userOutputMessages;

    private int userID;

    public User(String username, DnnMessage message) {
        this.username = username;
        userInputMessages = new LinkedBlockingQueue<>();
        userOutputMessages = new LinkedBlockingQueue<>();
        userInputMessages.add(message);
    }

    public User() {
        userInputMessages = new LinkedBlockingQueue<>();
        userOutputMessages = new LinkedBlockingQueue<>();
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public DnnMessage getMessage() throws InterruptedException {
    		return userInputMessages.take();
    }

    public void setMessage(DnnMessage message) {
    	userInputMessages.add(message);
    }

    public DnnMessage getUserToSendMessage() throws InterruptedException {
    		return userOutputMessages.take();
    }

    public void setUserToSendMessage(DnnMessage userToSendMessage) {
   		userOutputMessages.add(userToSendMessage);
    }
    
    public DnnMessage peekMessage(){
    	return userInputMessages.peek();
    }
}
