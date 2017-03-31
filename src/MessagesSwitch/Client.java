package MessagesSwitch;

import java.util.List;
import dnn.message.*;


public class Client {

    private String Clientname;
    private int ClientID;
    private List<DnnMessage> clientInputMessages;
    private List<DnnMessage> clientOutputMessages;
    
    public Client(String Clientname, DnnMessage message) {
        this.Clientname = Clientname;
        this.clientInputMessages.add(message);
    }

    public Client() {

    }

    public int getClientID() {
        return ClientID;
    }

    public void setClientID(int ClientID) {
        this.ClientID = ClientID;
    }

    public String getClientname() {
        return Clientname;
    }

    public void setClientname(String Clientname) {
        this.Clientname = Clientname;
    }


	public List<DnnMessage> getClientOutputMessages() {
		return clientOutputMessages;
	}

	public void setClientOutputMessages(List<DnnMessage> clientOutputMessages) {
		this.clientOutputMessages = clientOutputMessages;
	}
	
	public List<DnnMessage> getClientInputMessages() {
		return this.clientOutputMessages;
	}

	public void setClientInputMessages(List<DnnMessage> clientInputMessages) {
		this.clientInputMessages = clientInputMessages;
	}
	
	public DnnMessage getLastInputMessage(){
		return this.clientInputMessages.get(this.clientInputMessages.size()-1);
	}
}
