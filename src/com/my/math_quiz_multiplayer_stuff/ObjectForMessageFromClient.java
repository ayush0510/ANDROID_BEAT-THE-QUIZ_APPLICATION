
package com.my.math_quiz_multiplayer_stuff;

public class ObjectForMessageFromClient {
	int clientId;
	String text;
	public ObjectForMessageFromClient(int clientId,String text){
		this.clientId=clientId;
		this.text=text;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
