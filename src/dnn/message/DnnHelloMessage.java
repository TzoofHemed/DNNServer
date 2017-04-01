package dnn.message;

public class DnnHelloMessage extends DnnMessage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mMessageContent;
	
	public DnnHelloMessage(String senderName, String messageContent){
		mMessageContent = messageContent;
		mMessageType = MessageType.HELLO;
		mSenderName = senderName;
	}
	@Override
	public String getContent() {

		return mMessageContent;
	}
	


}
