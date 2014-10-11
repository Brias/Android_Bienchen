package de.androidbienchen.listener;

import org.json.JSONObject;

public interface MessageReceivedListener {
	public void onMessageReceived(JSONObject object);
	public void onSocketIOConnected();
}
