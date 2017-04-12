package messagesSwitch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import dnnUtil.dnnMessage.*;


public class Client {

    private String Clientname;
    private int ClientID;
    private final BlockingQueue<DnnMessage> clientInputMessages;
    private final BlockingQueue<DnnMessage> clientOutputMessages;
    private InputHandler mInputHandler;
    
    public Client(String Clientname, DnnMessage message, InputHandler inputHandler) {
        this.clientInputMessages = new LinkedBlockingQueue<>();
        this.clientOutputMessages = new LinkedBlockingQueue<>();
    	this.Clientname = Clientname;
        this.clientInputMessages.add(message);
        mInputHandler = inputHandler;
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
	
	public DnnMessage getLastInputMessage(){
		return this.clientInputMessages.remove();
	}
	
	public DnnMessage getLastOutputMessage(){
		return this.clientOutputMessages.remove();
	}
	
	public void addInputMessage(DnnMessage message){
			this.clientInputMessages.add(message);
	}
	
	public void addOutputMessage(DnnMessage message){
		this.clientOutputMessages.add(message);

	}
	
}
