package messagesSwitch;

import java.util.ArrayList;

import dnnProcessingUnit.DnnController;
import dnnUtil.dnnMessage.DnnCloseMessage;
import dnnUtil.dnnMessage.DnnMessage;
import dnnUtil.dnnMessage.DnnModelMessage;
import dnnUtil.dnnModel.DnnModelDescriptor;
import messagesSwitch.ClientConstants.ClientStatus;
import tcpConectivity.TCPServer;
import tcpConectivity.UserManager;


public class MessagesSwitch {
	
	private ClientManager mClientManager;
	private TCPServer mServer;
	private DnnController mController;
	
	public MessagesSwitch(TCPServer server){
		setServer(server);
		mClientManager = new ClientManager(this);
	}
	
	public ClientManager getClientManager(){
		return mClientManager;
	}
	
	public void setAlltoOutOfDate(){
		ArrayList<String> clientNames = mClientManager.getClientNames();
		for (String clientName : clientNames) {
			mClientManager.updateClientStatus(clientName,ClientStatus.OutOfDate);
		}
	}

	public void setUserOutputMessage(String userName, DnnMessage message){
		for(UserManager userManager : getServer().getConnectedUserManagers()){
			if(userManager.getUser().getUsername().equals(userName)){
				userManager.setUserOutputMessage(message);
			}
		}
	}
	
	public void updateOutOfDateClients(){
		ArrayList<String> clientNames = mClientManager.getOutOfDateClient();
		
		DnnModelDescriptor modelDescriptor = getController().getModel().getModelDescriptor();
//		DnnModelMessage messageContent = new DnnModelMessage("",modelDescriptor);
		
//		DnnWeightsData messageContent = mController.getDnnWeightsData();
		for (String clientName : clientNames) {
			setUserOutputMessage(clientName, new DnnModelMessage("",modelDescriptor));
			mClientManager.updateClientStatus(clientName,ClientStatus.Initial);
			mClientManager.setClientModelVersion(clientName, getController().getModelVersion());
		}
	}
	
	public String assignClient(){
		return mClientManager.getReadyClient();
	}

	
	public TCPServer getServer() {
		return mServer;
	}

	public void setServer(TCPServer mServer) {
		this.mServer = mServer;
	}
	
	public void setController(DnnController controller){
		mController = controller;
	}

	public void updateOutputMessages(){
		ArrayList<String> clientNames = getClientManager().getClientNames();
		DnnMessage outputMessage = null;
		for(String clientName : clientNames){
			try {
				outputMessage = getClientManager().getClientLastOutputMessage(clientName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(outputMessage != null){
				setUserOutputMessage(clientName,outputMessage);
			}
		}
	}
	
	public DnnController getController(){
		return mController;
	}
	
	public String getClientCount(){
		String count ="";
		int number = 0;
		for (String clientName : mClientManager.getClientNames()){
			number++;
			count += number+".  " + clientName + "\n";
		}
		return count;		
	}

	public void closeAll() {
		ArrayList<String> clientNames = mClientManager.getClientNames();
		
		DnnCloseMessage closeMessage = new DnnCloseMessage("sarbar","close");

		for (String clientName : clientNames) {
			setUserOutputMessage(clientName, closeMessage);
			mClientManager.updateClientStatus(clientName,ClientStatus.DeadToMe);
		}
	}

	
}
