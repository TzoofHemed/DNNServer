package MessagesSwitch;

import java.util.HashMap;
import java.util.Map;

import dnn.message.*;


public class ClientManager {
	private Map<String,Client> clientList;
	
	public ClientManager(){
		clientList = new HashMap<>();
	}
	
	public void addClient(String ClientName, DnnMessage message){
		try{
			this.clientList.put(ClientName,new Client(ClientName, message));
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
	
	public DnnMessage getClientLastMessage(String ClientName){
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

}
