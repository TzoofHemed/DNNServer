package messagesSwitch;

import dnnUtil.dnnMessage.DnnMessage;
import dnnUtil.dnnMessage.DnnModelMessage;
import dnnUtil.dnnMessage.DnnTrainMessage;
import dnnUtil.dnnModel.DnnBundle;
import dnnUtil.dnnModel.DnnDeltaData;
import dnnUtil.dnnModel.DnnIndex;
import dnnUtil.dnnModel.DnnModelDescriptor;
import dnnUtil.dnnStatistics.DnnStatistics;
import dnnUtil.dnnStatistics.DnnValidationResult;
import messagesSwitch.ClientConstants.ClientStatus;

public class InputHandler {

	private MessagesSwitch mMessageSwitch;
	public InputHandler(MessagesSwitch messageSwitch){
		mMessageSwitch = messageSwitch;
	}


	public void newMessageInput(String clientName, DnnMessage newMessage){
		switch(newMessage.getMessageType()){
		case TEST:{
			System.out.println("TEST user: "+ clientName +" is connected TEST\n");
			//            if (newMessage != null && managerDelegate != null) {
			//            user.setMessage(message);
			//            // notify message received action
			//            managerDelegate.messageReceived(user, null);
			break;
		}case READY:{
//			DnnIndex trainingIndex = null;
//
//			try {
//				trainingIndex = mMessageSwitch.getController().getNextTrainingIndex();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			//			DnnTrainingData trainingData = null;
//			//			try {
//
//			//				trainingData = mMessageSwitch.getController().getNextTrainingData();
//			//				mMessageSwitch.getController().trainingDataQueueFiller();
//			//			} catch (InterruptedException e) {
//			//				e.printStackTrace();
//			//			}
//			//			DnnTrainingDescriptor trainingDescriptor = mMessageSwitch.getController().getNextTrainingDescriptor();
//			//			DnnTrainingData trainingData = mMessageSwitch.getController().getModel().getTrainingData(trainingDescriptor);
//			//			mMessageSwitch.setUserOutputMessage(clientName, new DnnTrainingDataMessage(trainingData)); 
//			mMessageSwitch.setUserOutputMessage(clientName, new DnnTrainMessage(trainingIndex));
//			mMessageSwitch.getClientManager().updateClientStatus(clientName, ClientStatus.Busy);

			break;
		}case DELTA:{
			DnnDeltaData messageContent = (DnnDeltaData)newMessage.getContent();
			mMessageSwitch.getController().getModelUpdater().addDelta(messageContent);
			mMessageSwitch.getClientManager().updateClientStatus(clientName, ClientStatus.Ready);
			
//			DnnBundle trainingBundle = null;
//			DnnModelDescriptor modelDescriptor = mMessageSwitch.getController().getModel().getModelDescriptor();
			DnnMessage message = mMessageSwitch.getController().getNextBundleMessage();
//			mMessageSwitch.getController().getModelUpdater().weightChecker(messageContent);	
//			if(mMessageSwitch.getController().getModelVersion() >= mMessageSwitch.getClientManager().getClientModelVersion(clientName) 
//					+ mMessageSwitch.getController().getUpdateInterval()){
//				
//				DnnModelMessage message = new DnnModelMessage("",modelDescriptor);
//				mMessageSwitch.setUserOutputMessage(clientName, message);
//			}

			mMessageSwitch.setUserOutputMessage(clientName, message);
			mMessageSwitch.getClientManager().updateClientStatus(clientName, ClientStatus.Busy);

			break;
		}case WEIGHTS:{
			//			DnnWeightsData messageContent = (DnnWeightsData)newMessage.getContent();
			//			
			//			//TODO update statistics, update clientDataManager
			//			mMessageSwitch.getClientManager().updateClientStatus(clientName, ClientStatus.Ready);
			//			mMessageSwitch.getController().getModelUpdater().weightChecker(messageContent);	
			//			if(mMessageSwitch.getController().getModelVersion() == mMessageSwitch.getClientManager().getClientModelVersion(clientName)){
			//				DnnTrainingData trainingData = null;
			//				try {
			//					trainingData = mMessageSwitch.getController().getNextTrainingData();
			//					mMessageSwitch.getController().trainingDataQueueFiller();
			//				} catch (InterruptedException e) {
			//					e.printStackTrace();
			//				}
			////				DnnTrainingDescriptor trainingDescriptor = mMessageSwitch.getController().getNextTrainingDescriptor();
			////				DnnTrainingData trainingData = mMessageSwitch.getController().getModel().getTrainingData(trainingDescriptor);
			//				mMessageSwitch.setUserOutputMessage(clientName, new DnnTrainingDataMessage(trainingData)); 
			//				mMessageSwitch.getClientManager().updateClientStatus(clientName, ClientStatus.Busy);
			//			}
			break;
		}case STRING:{
			break;
		}case STATISTICS:
			mMessageSwitch.getController().addStatistics((DnnStatistics)newMessage.getContent());
			break;
		case HELLO:{
			System.out.println("user: "+ clientName +" is connected!\n");
			if(clientName != null){
				
				DnnMessage message = mMessageSwitch.getController().getNextBundleMessage();				
//				DnnModelDescriptor modelDescriptor = mMessageSwitch.getController().getModel().getModelDescriptor();
//				DnnModelMessage message = new DnnModelMessage("",modelDescriptor);
				mMessageSwitch.getClientManager().addClient(clientName, message);
				mMessageSwitch.setUserOutputMessage(clientName, message);
				mMessageSwitch.getClientManager().updateClientStatus(clientName, ClientStatus.Initial);
//				mMessageSwitch.getClientManager().setClientModelVersion(clientName, mMessageSwitch.getController().getModelVersion());
			}
			break;
		}case VALIDATIONRESULT:{
			mMessageSwitch.getController().addValidationResult((DnnValidationResult)newMessage.getContent());
		}default:
			break;
		}
	}


}
