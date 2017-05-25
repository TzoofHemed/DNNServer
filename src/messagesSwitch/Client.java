package messagesSwitch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import dnnUtil.dnnMessage.*;
import messagesSwitch.ClientConstants.ClientStatus;


public class Client {

    private String Clientname;
    private int ClientID;
    private final BlockingQueue<DnnMessage> clientInputMessages;
    private final BlockingQueue<DnnMessage> clientOutputMessages;
    private InputHandler mInputHandler;
    private ClientStatus mStatus;
    private int mModelVersion;
    
    public Client(String Clientname, DnnMessage message, InputHandler inputHandler) {
        this.clientInputMessages = new LinkedBlockingQueue<>();
        this.clientOutputMessages = new LinkedBlockingQueue<>();
    	this.Clientname = Clientname;
        mInputHandler = inputHandler;
        mInputHandler.newMessageInput(this.Clientname, message);
        setStatus(ClientStatus.Initial);
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
	
	public DnnMessage getLastOutputMessage() throws InterruptedException{
		return this.clientOutputMessages.take();
	}
	
	public void addInputMessage(DnnMessage message){
//			this.clientInputMessages.add(message);
			this.mInputHandler.newMessageInput(this.Clientname, message);
	}
	
	public void addOutputMessage(DnnMessage message){
		this.clientOutputMessages.add(message);

	}

	public ClientStatus getStatus() {
		return mStatus;
	}

	public void setStatus(ClientStatus mStatus) {
		this.mStatus = mStatus;
	}

	public int getModelVersion() {
		return mModelVersion;
	}

	public void setModelVersion(int mModelVersion) {
		this.mModelVersion = mModelVersion;
	}
	
}
