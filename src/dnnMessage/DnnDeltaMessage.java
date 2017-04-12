package dnnMessage;

import dnnModel.DnnModelDelta;

public class DnnDeltaMessage extends DnnMessage{
	
	private static final long serialVersionUID = 1L;
	
	public DnnDeltaMessage(String senderName, DnnModelDelta messageContent) {
		mMessageType = MessageType.MODEL;
		mMessageContent = messageContent;
		mSenderName = senderName;
	}
	private DnnModelDelta mMessageContent;
	
	@Override
	public DnnModelDelta getContent() {
		return mMessageContent;
	}

}
