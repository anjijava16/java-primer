package org.book;


/**
 * @author borqs<br>
 * 
 * 	M sip:wap.fetion.com.cn SIP-C/2.0<li>
 *	S: 1580101xxxx<li>
 *	R: 1889020xxxx<li>
 *	L: 21<li>
 *	Hello World!<li>
 */
public class SmsObject {
	private String sender;// 短信发送者
	private String receiver;// 短信接受者
	private String message;// 短信内容

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
