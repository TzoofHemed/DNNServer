package MessagesSwitch;

import dnn.message.*;

public class InputHandler {
	private String mClientName;

	
	public InputHandler(String ClientName){
		this.setmClientName(ClientName);
	}


	public String getmClientName() {
		return mClientName;
	}


	public void setmClientName(String mClientName) {
		this.mClientName = mClientName;
	}
	
	public void newMessageInput(DnnMessage newMessage){
        switch(newMessage.getMessageType()){
        case TEST:
//            if (message != null && managerDelegate != null) {
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
		default:
			break;
        }
	}
	
	
}
