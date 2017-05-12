package messagesSwitch;

import java.util.ArrayList;

import dnnProcessingUnit.DnnController;
import dnnUtil.dnnMessage.DnnMessage;
import dnnUtil.dnnMessage.DnnWeightsMessage;
import dnnUtil.dnnModel.DnnWeightsData;
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
			if(userManager.getUser().getUsername() == userName){
				userManager.setUserOutputMessage(message);
			}
		}
	}
	
	public void updateOutOfDateClients(){
		ArrayList<String> clientNames = mClientManager.getOutOfDateClient();
		DnnWeightsData messageContent = mController.getModel().getWeightsData();
		for (String clientName : clientNames) {
			setUserOutputMessage(clientName, new DnnWeightsMessage("",messageContent));
			mClientManager.updateClientStatus(clientName,ClientStatus.Initial);
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
		DnnMessage outputMessage;
		for(String clientName : clientNames){
			outputMessage = getClientManager().getClientLastOutputMessage(clientName);
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

	
}
