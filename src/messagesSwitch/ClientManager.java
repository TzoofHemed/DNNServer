package messagesSwitch;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import dnnUtil.dnnMessage.*;
import messagesSwitch.ClientConstants.ClientStatus;


public class ClientManager {
	private ConcurrentMap<String,Client> clientList;
	private MessagesSwitch mMessagesSwitch;
	public ClientManager(MessagesSwitch messagesSwitch){
		clientList = new ConcurrentHashMap<>();
		mMessagesSwitch = messagesSwitch;
		
	}
	
	public void addClient(String ClientName, DnnMessage message){
		try{
			this.clientList.put(ClientName,new Client(ClientName, message, new InputHandler(mMessagesSwitch)));
		}catch (Exception e) {
			System.out.println("ClientManager: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
	}
	
	public void removeClient(String ClientName){
		try{
			if(!this.clientList.containsKey(ClientName)){
				throw new Exception("Client: " + ClientName + " doesn't exist");
			}
			this.clientList.remove(ClientName);
		}catch (Exception e) {
            System.out.println("ClientManager: " + e.getMessage());
            e.printStackTrace();
        }
	}
	
	public void updateClientStatus(String clientName, ClientStatus status){
		clientList.get(clientName).setStatus(status);
	}
	
	public ArrayList<String> getOutOfDateClient(){
		ArrayList<String> outOfDateNames =new ArrayList<>();
		ArrayList<String> clientNames = getClientNames();
		for (String clientName: clientNames) {
			if(clientList.get(clientName).getStatus() == ClientStatus.OutOfDate){
				outOfDateNames.add(clientName);
			}
		}
		return outOfDateNames;
	}
	
	public String getReadyClient(){
		ArrayList<String> clientNames = getClientNames();
		for (String clientName: clientNames) {
			if(clientList.get(clientName).getStatus() == ClientStatus.Ready){
				changeClientStatus(clientName, ClientStatus.Busy);
				return clientName;
			}
		}
		return "";
	}
	
	public DnnMessage getClientLastInputMessage(String ClientName){
		try{
			if(!this.clientList.containsKey(ClientName)){
				return null;
			}
			return this.clientList.get(ClientName).getLastInputMessage();
		}catch (Exception e) {
            System.out.println("ClientManager: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }		
	}
	
	public DnnMessage getClientLastOutputMessage(String ClientName) throws Exception{
		try{
			if(!this.clientList.containsKey(ClientName)){
				return null;
			}
			return this.clientList.get(ClientName).getLastOutputMessage();
		}catch (Exception e) {
            System.out.println("ClientManager: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }		
	}
	
	public void addInputMessageToClient(String clientName, DnnMessage message){
		if(!this.clientList.containsKey(clientName)){
			addClient(clientName, message);
		}else{
			this.clientList.get(clientName).addInputMessage(message);
		}
	}
	
	public void addMessageToClient(String clientName, DnnMessage message){
		if(!this.clientList.containsKey(clientName)){
			addClient(clientName, message);
		}else{
			this.clientList.get(clientName).addOutputMessage(message);
		}
	}

	public ArrayList<String> getClientNames(){
		String key;
		ArrayList<String> clientNames = new ArrayList<>();
		for(Map.Entry<String, Client> entry : clientList.entrySet()){
			key = entry.getKey();
			clientNames.add(key);
		}
		return clientNames;
	}
	public void changeClientStatus(String clientName, ClientStatus status){
		this.clientList.get(clientName).setStatus(status);
	}
	public int getClientModelVersion(String clientName){
		return clientList.get(clientName).getModelVersion();
	}
	public void setClientModelVersion(String clientName,int modelVer){
		clientList.get(clientName).setModelVersion(modelVer);
	}
	
}
