package netty.chatroom;

public class ChatMessage {

	private String sendDate;
	private String message;
	private String senderId;
	
	public ChatMessage(String sendDate, String message, String senderId) {
		super();
		this.sendDate = sendDate;
		this.message = message;
		this.senderId = senderId;
	}
	
	public String getSendDate() {
		return sendDate;
	}
	public String getMessage() {
		return message;
	}
	public String getSenderId() {
		return senderId;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	@Override
	public String toString() {
		return String.format("%s  -  %s: %s", sendDate, senderId, message);
	}
}
