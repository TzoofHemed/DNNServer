package dnn.message;

import java.io.Serializable;

public abstract class DnnMessage implements Serializable {
	static final long serialVersionUID = 1L;
	// defines the message type
	public static enum MessageType{
		MODEL, DELTA, TEST, STRING, STATISTICS, HELLO
	}
	
	protected String mSenderName;
	
	protected MessageType mMessageType;
	
	//@returns the type of the message
	public MessageType getMessageType(){
		return this.mMessageType;
	}
	
	//@returns the content of the message
	public abstract Object getContent();
	
	public String getSenderName(){
		return mSenderName;
	}
}
