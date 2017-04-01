package dnn.message;

public class DnnTestMessage extends DnnMessage {
	static final long serialVersionUID = 1L;
	
	private String mMessageContent;
	
	public DnnTestMessage(String senderName, String messageContent) {

		mMessageType = MessageType.TEST;
		mMessageContent = messageContent;
		mSenderName = senderName;
	}
	
	@Override
	public String getContent() {

		return mMessageContent;
	}

}
