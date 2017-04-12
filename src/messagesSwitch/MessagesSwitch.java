package messagesSwitch;

import dnnMessage.DnnMessage;
import tcpConectivity.TCPServer;
import tcpConectivity.UserManager;

//TODO add mutex for connected userManger array

public class MessagesSwitch {
	
	private ClientManager mClientManager;
	private TCPServer mServer;
	
	public MessagesSwitch(TCPServer server){
		mClientManager = new ClientManager();
		setmServer(server);

	}
	
	public ClientManager getClientManager(){
		return mClientManager;
	}
	//TODO add interface from and to TCPServer for getting connected array
	public void setUserOutputMessage(String userName, DnnMessage message){
		for(UserManager userManager : getmServer().getConnectedUserManagers()){
			if(userManager.getUser().getUsername() == userName){
				userManager.setUserOutputMessage(message);
			}
		}
	}

	public TCPServer getmServer() {
		return mServer;
	}

	public void setmServer(TCPServer mServer) {
		this.mServer = mServer;
	}
	
}
