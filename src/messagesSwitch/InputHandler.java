package messagesSwitch;

import dnnMessage.*;
import tcpConectivity.User;

public class InputHandler {
	
	
	public InputHandler(){

	}

	
	public void newMessageInput(User user, DnnMessage newMessage){
        switch(newMessage.getMessageType()){
        case TEST:
//            if (newMessage != null && managerDelegate != null) {
//            user.setMessage(message);
//            // notify message received action
//            managerDelegate.messageReceived(user, null);
        	break;
        case MODEL:
        	break;
        case DELTA:
        	break;
        case STRING:
        	break;
        case STATISTICS:
        	break;
        case HELLO:

        	user.setMessage(newMessage);
        	user.setUsername(newMessage.getSenderName());
        	//for debug:
        	System.out.println("user: "+user.getUserID() +" is connected");
        	break;
		default:
			break;
        }
	}
	
	
}
