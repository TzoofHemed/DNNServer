package tcpConectivity;

import dnnMessage.DnnMessage;

/**
 * Encapsulates a user entity, having its port and username
 * <p/>
 */
public class User {

    private String username;
    private DnnMessage message;
    private DnnMessage userToSendMessage;
    private int userID;

    public User(String username, DnnMessage message) {
        this.username = username;
        this.message = message;
    }

    public User() {

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

    public DnnMessage getMessage() {
        return message;
    }

    public void setMessage(DnnMessage message) {
        this.message = message;
    }

    public DnnMessage getUserToSendMessage() {
        return userToSendMessage;
    }

    public void setUserToSendMessage(DnnMessage userToSendMessage) {
        this.userToSendMessage = userToSendMessage;
    }
}
