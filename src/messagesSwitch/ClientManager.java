package messagesSwitch;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import dnnUtil.dnnMessage.*;


public class ClientManager {
	private Map<String,Client> clientList;
	
	public ClientManager(){
		clientList = new LinkedHashMap<>();
	}
	
	public void addClient(String ClientName, DnnMessage message){
		try{
			this.clientList.put(ClientName,new Client(ClientName, message, new InputHandler()));
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
	
	public DnnMessage getClientLastOutputMessage(String ClientName){
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
	
	public void addMessageToClient(String clientName, DnnMessage message){
		if(!this.clientList.containsKey(clientName)){
			addClient(clientName, message);
		}
		this.clientList.get(clientName).addInputMessage(message);
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
}
