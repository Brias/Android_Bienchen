package de.androidbienchen.activities;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import de.androidbienchen.R;
import de.androidbienchen.chathelper.ChatListAdapter;
import de.androidbienchen.chathelper.ChatListItem;
import de.androidbienchen.data.LocationDatabase;
import de.androidbienchen.data.UpdateDialogHelper;
import de.androidbienchen.listener.MessageReceivedListener;
import de.androidbienchen.socketiohelper.SocketIOHelper;

public class ChatActivity extends Fragment implements MessageReceivedListener,
		Runnable {

	private ArrayList<ChatListItem> chatList;
	private ArrayAdapter<ChatListItem> chatListAdapter;
	private LocationDatabase db;
	private SocketIOHelper socketIOhelper;
	private String id;

	public ChatActivity(SocketIOHelper socketIOhelper) {
		this.socketIOhelper = socketIOhelper;
		setListener();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.chat_input, container, false);

		initDatabase();
		setAndroidId();
		initChatList();
		initUI(rootView);

		return rootView;
	}

	void requestMessageHistory() {
		socketIOhelper.getMessageHistory();
	}

	void setListener() {
		socketIOhelper.setMessageReceivedListener(this);
	}

	void initDatabase() {
		db = new LocationDatabase(getActivity());
	}

	void setAndroidId() {
		db.open();
		this.id = db.getUserIdentification().getAndroidId();
		db.close();
	}

	void initChatList() {
		chatList = new ArrayList<ChatListItem>();
	}

	void initUI(View rootView) {
		initTaskButton(rootView);
		initListAdapter(rootView);
	}

	private void initTaskButton(View rootView) {
		Button sendButton = (Button) rootView.findViewById(R.id.send_button);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processUserInput(v);
			}
		});
	}
	
	void processUserInput(View v){
		EditText edit = (EditText) getActivity().findViewById(
				R.id.chat_input_container);
		String sendingMessage = edit.getText().toString();
		sendMessage(sendingMessage);
		edit.setText("");
	}

	private void initListAdapter(View rootView) {
		ListView list = (ListView) rootView.findViewById(R.id.message_list);
		chatListAdapter = new ChatListAdapter(getActivity(), chatList, id);
		list.setAdapter(chatListAdapter);
	}

	void sendMessage(String sendMessage) {
		if (!sendMessage.equals("")) {
			if (socketIOhelper.socketConnected()) {
				socketIOhelper.sendMessage(new ChatListItem(sendMessage, db
						.getUserIdentification().getUsername(), db
						.getUserIdentification().getAndroidId()));
			} else {
				setCancelledSendMessageDialog();
			}
		}else{
			setEmptyEditTextDialog();
		}
	}

	void setEmptyEditTextDialog(){
		UpdateDialogHelper
		.UpdateCanceledDialog(
				getActivity(),
				getActivity().getResources().getString(
						R.string.empty_edit_text), "");
	}
	
	void setCancelledSendMessageDialog() {
		UpdateDialogHelper
				.UpdateCanceledDialog(
						getActivity(),
						getActivity().getResources().getString(
								R.string.server_connection_error), "");
	}

	void addMessageToList(JSONObject object) {
		try {
			chatList.add(new ChatListItem(object
					.getString(ChatListItem.JSON_MESSAGE), object
					.getString(ChatListItem.JSON_USERNAME), object
					.getString(ChatListItem.JSON_DATE), object
					.getString(ChatListItem.JSON_ID)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void updateChatView() {
		chatListAdapter.notifyDataSetChanged();
		ListView list = (ListView) getActivity()
				.findViewById(R.id.message_list);
		list.setSelection(chatListAdapter.getCount() - 1);
	}

	@Override
	public void onMessageReceived(JSONObject object) {
		// TODO Auto-generated method stub
		addMessageToList(object);
		getActivity().runOnUiThread(this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		updateChatView();
	}

	@Override
	public void onSocketIOConnected() {
		// TODO Auto-generated method stub
		requestMessageHistory();
	}
}
