package messagesSwitch;

import dnnUtil.dnnMessage.*;
import dnnUtil.dnnModel.DnnModelDelta;
import dnnUtil.dnnStatistics.DnnStatistics;

public class InputHandler {
	
	private MessagesSwitch mMessageSwitch;
	public InputHandler(MessagesSwitch messageSwitch){
		mMessageSwitch = messageSwitch;
	}

	
	public void newMessageInput(String clientName, DnnMessage newMessage){
        switch(newMessage.getMessageType()){
        case TEST:
        	System.out.println("TEST user: "+ clientName +" is connected TEST\n");
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
        	//print to console for debug:
        	System.out.println("user: "+ clientName +" is connected\n");
        	if(clientName != null){
        		mMessageSwitch.setUserOutputMessage(clientName, new DnnTestMessage("TAKAS Demon","mother of all fork bombs"));
//        		mMessageSwitch.getController().assignClient(clientName);
        	}        	
        	break;
		default:
			break;
        }
	}
	
	
}
