package de.androidbienchen.socketiohelper;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import de.androidbienchen.chathelper.ChatListItem;
import de.androidbienchen.data.AppConfig;
import de.androidbienchen.listener.MessageReceivedListener;
import de.androidbienchen.listener.UserStatusReceivedListener;
import de.androidbienchen.presencestatushelper.PresenceStatusItem;

public class SocketIOHelper implements IOCallback {
	
	private static final String MESSAGE_EVENT = "user_message";
	private static final String PRESENCE_EVENT = "user_status";
	
	private SocketIO socket;
	private MessageReceivedListener messageListener;
	private UserStatusReceivedListener statusListener;
	
	public SocketIOHelper(){
		try {
			socket = new SocketIO(AppConfig.server.CHAT_URL);
			socket.connect(this);
			socket.send("Hello Server");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setMessageReceivedListener(MessageReceivedListener listener){
		this.messageListener = listener;
	}
	
	public void setUserStatusReceivedListener(UserStatusReceivedListener listener){
		this.statusListener = listener;
	}

	public void sendMessage(ChatListItem message){
		JSONObject obj = message.getItemAsJSONObject();
		if(socket.isConnected()){
			socket.emit(MESSAGE_EVENT, obj);
			addMessageToChat(obj);
		}
	}
	
	public void sendPresenceStatus(PresenceStatusItem status){
		JSONObject obj = status.getPresenceItemAsJSONObject();
		if(socket.isConnected()){
			socket.emit(PRESENCE_EVENT, obj);
			addStatusToList(obj);
		}
	}
	
	void addMessageToChat(JSONObject obj){
		messageListener.onMessageReceived(obj);
	}
	
	void addStatusToList(JSONObject obj){
		statusListener.onUserStatusReceived(obj);
	}
	
	
	void processFetchedData(Object fetchedData, String eventName){
		checkEventName(eventName, createJSONOfFetched(fetchedData));
	}
	
	private JSONObject createJSONOfFetched(Object fetchedData){
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(fetchedData.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return jsonObject;
	}
	
	void checkEventName(String eventName, JSONObject fetchedData){
		if(eventName.equals(MESSAGE_EVENT)){
			addMessageToChat(fetchedData);
		}
		else if(eventName.equals(PRESENCE_EVENT)){
			addStatusToList(fetchedData);
			Log.d("CHECKEVENTNAME", "PRESENCE_EVENT");
		}
	}
	
	@Override
	public void on(String arg0, IOAcknowledge arg1, Object... arg2) {
		// TODO Auto-generated method stub
		Object[] obj = arg2;
		String eventName = arg0;
		for(int i = 0; i < obj.length; i++){
			processFetchedData(obj[i], eventName);
		}
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(SocketIOException arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMessage(String arg0, IOAcknowledge arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMessage(JSONObject arg0, IOAcknowledge arg1) {
		// TODO Auto-generated method stub
	}
}
