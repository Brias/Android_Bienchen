package de.androidbienchen.socketiohelper;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONObject;

import de.androidbienchen.chathelper.ChatListItem;
import de.androidbienchen.data.AppConfig;
import de.androidbienchen.presencestatushelper.PresenceStatusItem;

public class SocketIOHelper implements IOCallback {
	
	private static final String MESSAGE_EVENT = "user message";
	private static final String PRESENCE_EVENT = "user present";
	
	private static SocketIO socket;
	
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

	public static void sendMessage(ChatListItem message){
		JSONObject obj = message.getItemAsJSONObject();
		socket.emit(MESSAGE_EVENT, obj);
	}
	
	public static void sendPresenceStatus(PresenceStatusItem status){
		JSONObject obj = status.getPresenceItemAsJSONObject();
		socket.emit(PRESENCE_EVENT, obj);
	}
	
	@Override
	public void on(String arg0, IOAcknowledge arg1, Object... arg2) {
		// TODO Auto-generated method stub
		
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
