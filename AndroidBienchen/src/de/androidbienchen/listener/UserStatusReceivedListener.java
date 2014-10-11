package de.androidbienchen.listener;

import org.json.JSONObject;

public interface UserStatusReceivedListener {
	public void onUserStatusReceived(JSONObject object);
	public void onSocketIOConnected();
}
