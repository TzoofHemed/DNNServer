package messagesSwitch;

import java.util.ArrayList;
import java.util.List;

import dnnUtil.dnnMessage.*;


public class Client {

    private String Clientname;
    private int ClientID;
    private List<DnnMessage> clientInputMessages;
    private List<DnnMessage> clientOutputMessages;
    private InputHandler mInputHandler;
    
    public Client(String Clientname, DnnMessage message, InputHandler inputHandler) {
        this.clientInputMessages = new ArrayList<>();
        this.clientOutputMessages = new ArrayList<>();
    	this.Clientname = Clientname;
        this.clientInputMessages.add(message);
        mInputHandler = inputHandler;
    }

    public Client() {

    }
    
    public InputHandler getInputHandler(){
    	return mInputHandler;
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
	public DnnMessage getLastOutputMessage(){
		return this.clientOutputMessages.get(this.getClientOutputMessages().size()-1);
	}
	public void addInputMessage(DnnMessage message){
		this.clientInputMessages.add(message);
	}
	public void addOutputMessage(DnnMessage message){
		this.clientOutputMessages.add(message);
	}
}
