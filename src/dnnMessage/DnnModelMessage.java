package dnnMessage;

import dnnModel.DnnModel;

public class DnnModelMessage extends DnnMessage{
	static final long serialVersionUID = 1L;

	public DnnModelMessage(String senderName, DnnModel messageContent){
		mMessageType = MessageType.DELTA;
		mMessageContent = messageContent;
		mSenderName = senderName;

	}
	private DnnModel mMessageContent;
	@Override
	public DnnModel getContent() {
		return mMessageContent;
	}
}




