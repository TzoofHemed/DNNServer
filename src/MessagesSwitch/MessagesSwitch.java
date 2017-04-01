package MessagesSwitch;

public class MessagesSwitch {
	
	private ClientManager mClientManager;
	
	public MessagesSwitch(){
		mClientManager = new ClientManager();
	}
	
	public ClientManager getClientManager(){
		return mClientManager;
	}

}
