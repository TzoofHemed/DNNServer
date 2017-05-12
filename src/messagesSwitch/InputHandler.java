package messagesSwitch;

import dnnUtil.dnnMessage.*;
import dnnUtil.dnnModel.DnnModelDescriptor;
import dnnUtil.dnnModel.DnnTrainingData;
import dnnUtil.dnnModel.DnnTrainingDescriptor;
import dnnUtil.dnnModel.DnnWeightsData;
import dnnUtil.dnnStatistics.DnnStatistics;
import messagesSwitch.ClientConstants.ClientStatus;

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
        case READY:
    		DnnTrainingDescriptor trainingDescriptor = mMessageSwitch.getController().getNextTrainingDescriptor();
    		DnnTrainingData trainingData = mMessageSwitch.getController().getModel().getTrainingData(trainingDescriptor);
    		mMessageSwitch.setUserOutputMessage(clientName, new DnnTrainingDataMessage(trainingData)); 
    		mMessageSwitch.getClientManager().updateClientStatus(clientName, ClientStatus.Busy);
        	break;
        case WEIGHTS:
        	DnnWeightsData messageContent = (DnnWeightsData)newMessage.getContent();
        	//TODO update statistics, update clientDataManager
        	mMessageSwitch.getClientManager().updateClientStatus(clientName, ClientStatus.Ready);
        	mMessageSwitch.getController().getModelUpdater().weightChecker(messageContent);	
        	break;
        case STRING:
        	break;
        case STATISTICS:
        		mMessageSwitch.getController().addStatistics((DnnStatistics)newMessage.getContent());
        	break;
        case HELLO:
        	System.out.println("user: "+ clientName +" is connected!\n");
        	if(clientName != null){
        		DnnModelDescriptor modelDescriptor = mMessageSwitch.getController().getModel().getModelDescriptor();
        		DnnModelMessage message = new DnnModelMessage("",modelDescriptor);
        		mMessageSwitch.getClientManager().addClient(clientName, message);
        		mMessageSwitch.setUserOutputMessage(clientName, message);
        		mMessageSwitch.getClientManager().updateClientStatus(clientName, ClientStatus.Initial);
        	}
        	break;
		default:
			break;
        }
	}
	
	
}
