package messagesSwitch;

import dnnUtil.dnnMessage.*;
import dnnUtil.dnnModel.DnnModelDelta;
import dnnUtil.dnnModel.DnnModelDescriptor;
import dnnUtil.dnnModel.DnnTrainingData;
import dnnUtil.dnnModel.DnnTrainingDescriptor;
import dnnUtil.dnnModel.DnnTrainingPackage;
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
        case MODEL:
        	break;
        case DELTA:
        	if(newMessage.getContent() instanceof DnnModelDelta){
        		mMessageSwitch.getController().getModelUpdater().deltaChecker((DnnModelDelta)newMessage.getContent());
        		mMessageSwitch.getClientManager().changeClientStatus(newMessage.getSenderName(), ClientStatus.Free);
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
        	System.out.println("user: "+ clientName +" is connected!\n");
        	if(clientName != null){
//        		mMessageSwitch.setUserOutputMessage(clientName, new DnnTestMessage("TAKAS Demon","mother of all fork bombs"));
        		DnnModelDescriptor modelDescriptor = mMessageSwitch.getController().getModel().getModelDescriptor();
        		DnnTrainingDescriptor trainingDescriptor = mMessageSwitch.getController().getNextTrainingDescriptor();
        		DnnTrainingData trainingData = mMessageSwitch.getController().getModel().getTrainingData(trainingDescriptor);
        		mMessageSwitch.setUserOutputMessage(clientName, new DnnTrainingPackageMessage(new DnnTrainingPackage(modelDescriptor, trainingData)));

//        		mMessageSwitch.getController().assignClient(clientName);
//        		mMessageSwitch.setUserOutputMessage(clientName, mMessageSwitch.getClientManager().getClientLastOutputMessage(clientName));
        	}        	
        	break;
		default:
			break;
        }
	}
	
	
}
