package de.androidbienchen.socketiohelper;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONObject;

import android.util.Log;

import de.androidbienchen.chathelper.ChatListItem;
import de.androidbienchen.data.AppConfig;
import de.androidbienchen.listener.MessageReceivedListener;
import de.androidbienchen.listener.UserStatusReceivedListener;
import de.androidbienchen.presencestatushelper.PresenceStatusItem;

public class SocketIOHelper implements IOCallback {
	
	private static final String MESSAGE_EVENT = "user message";
	private static final String PRESENCE_EVENT = "user present";
	
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
		socket.emit(MESSAGE_EVENT, obj);
		test(obj);
	}
	
	void test(JSONObject obj){
		messageListener.onMessageReceived(obj);
	}
	
	void testStatus(JSONObject obj){
		statusListener.onUserStatusReceived(obj);
	}
	
	public void sendPresenceStatus(PresenceStatusItem status){
		JSONObject obj = status.getPresenceItemAsJSONObject();
		socket.emit(PRESENCE_EVENT, obj);
		testStatus(obj);
	}
	
	@Override
	public void on(String arg0, IOAcknowledge arg1, Object... arg2) {
		// TODO Auto-generated method stub
		Log.d("On", "TRUE");
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
		Log.d("OnConnect", "TRUE");
		
	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub
		Log.d("OnDisconnect", "TRUE");
		
	}

	@Override
	public void onError(SocketIOException arg0) {
		// TODO Auto-generated method stub
		Log.d("OnError", ""+arg0);
		
	}

	@Override
	public void onMessage(String arg0, IOAcknowledge arg1) {
		// TODO Auto-generated method stub
		Log.d("OnMessage", "TRUE");
	}

	@Override
	public void onMessage(JSONObject arg0, IOAcknowledge arg1) {
		// TODO Auto-generated method stub
		Log.d("OnMessage", "TRUE");
	}
}
