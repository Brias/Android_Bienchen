package de.androidbienchen.socketiohelper;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.androidbienchen.chathelper.ChatListItem;
import de.androidbienchen.data.AppConfig;
import de.androidbienchen.listener.MessageReceivedListener;
import de.androidbienchen.listener.UserStatusReceivedListener;
import de.androidbienchen.presencestatushelper.PresenceStatusItem;

public class SocketIOHelper implements IOCallback {

	private static final String MESSAGE_EVENT = "user_message";
	private static final String PRESENCE_EVENT = "user_status";
	private static final String MESSAGE_HISTORY = "message_history";
	private static final String CURRENT_STATUSES = "current_statuses";

	private SocketIO socket;
	private MessageReceivedListener messageListener;
	private UserStatusReceivedListener statusListener;

	private boolean messageHistoryReceived;
	private boolean statusesReceived;

	public SocketIOHelper() {
		try {
			messageHistoryReceived = false;
			statusesReceived = false;
			socket = new SocketIO(AppConfig.server.CHAT_URL);
			socket.connect(this);
			socket.send("Hello Server");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setMessageReceivedListener(MessageReceivedListener listener) {
		this.messageListener = listener;
	}

	public void setUserStatusReceivedListener(
			UserStatusReceivedListener listener) {
		this.statusListener = listener;
	}

	public void getMessageHistory() {
		if (socketConnected()) {
			socket.emit(MESSAGE_HISTORY);
		}
	}

	public void getCurrentStatuses() {
		if (socketConnected()) {
			socket.emit(CURRENT_STATUSES);
		}
	}

	public void sendMessage(ChatListItem message) {
		JSONObject obj = message.getItemAsJSONObject();

		socket.emit(MESSAGE_EVENT, obj);
		addMessageToChat(obj);
	}

	public boolean socketConnected() {
		return socket.isConnected();
	}

	public void sendPresenceStatus(PresenceStatusItem status) {
		JSONObject obj = status.getPresenceItemAsJSONObject();

		socket.emit(PRESENCE_EVENT, obj);
	}

	void addMessageToChat(JSONObject obj) {
		messageListener.onMessageReceived(obj);
	}

	void addStatusToList(JSONObject obj) {
		statusListener.onUserStatusReceived(obj);
	}

	void processFetchedData(Object fetchedData, String eventName) {
		JSONObject obj = createJSONOfFetched(fetchedData);
		if (obj != null) {
			checkEventName(eventName, obj);
		}
	}

	private JSONObject createJSONOfFetched(Object fetchedData) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(fetchedData.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	void checkEventName(String eventName, JSONObject fetchedData) {
		if (eventName.equals(MESSAGE_EVENT)
				|| eventName.equals(MESSAGE_HISTORY)) {
			addMessageToChat(fetchedData);
		} else if (eventName.equals(PRESENCE_EVENT)
				|| eventName.equals(CURRENT_STATUSES)) {
			addStatusToList(fetchedData);
		}
	}

	void processMessageHistory(String eventName, Object obj) {
		try {
			JSONArray jsonArray = new JSONArray(obj.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				checkEventName(eventName, jsonObject);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void on(String arg0, IOAcknowledge arg1, Object... arg2) {
		// TODO Auto-generated method stub
		Object[] obj = arg2;
		String eventName = arg0;

		if (eventName.equals(CURRENT_STATUSES) && !statusesReceived) {
			processMessageHistory(eventName, obj[0]);
			statusesReceived = true;
		} else if (eventName.equals(MESSAGE_HISTORY) && !messageHistoryReceived) {
			processMessageHistory(eventName, obj[0]);
			messageHistoryReceived = true;
		} else if (eventName.equals(MESSAGE_EVENT)
				|| eventName.equals(PRESENCE_EVENT)) {
			processFetchedData(obj[0], eventName);
		}
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
		try {
			if (!messageHistoryReceived) {
				messageListener.onSocketIOConnected();
			}
			if (!statusesReceived) {
				statusListener.onSocketIOConnected();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
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
