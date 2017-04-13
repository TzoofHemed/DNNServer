package messagesSwitch;

import dnnUtil.dnnMessage.*;
import dnnUtil.dnnModel.DnnModelDelta;
import dnnUtil.dnnStatistics.DnnStatistics;
import tcpConectivity.User;

public class InputHandler {
	
	private MessagesSwitch mMessageSwitch;
	public InputHandler(MessagesSwitch messageSwitch){
		mMessageSwitch = messageSwitch;
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
        	if(newMessage.getContent() instanceof DnnModelDelta){
        		mMessageSwitch.getController().getModelUpdater().deltaChecker((DnnModelDelta)newMessage.getContent());
        	}
        	break;
        case STRING:
        	break;
        case STATISTICS:
        	if(newMessage.getContent() instanceof DnnStatistics){
        		mMessageSwitch.getController().addStatistics((DnnStatistics)newMessage.getContent());
        	}
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
