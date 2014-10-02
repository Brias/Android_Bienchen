package de.androidbienchen.activities;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import de.androidbienchen.data.AppConfig;

public class ChatActivity extends Fragment implements IOCallback{
	
	private ArrayList<ChatListItem> chatList;
	private ArrayAdapter<ChatListItem> chatListAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.chat_input,
				container, false);
		
		initChatList();
		initUI(rootView);
		
		return rootView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initChat();
	}
	
	void initChatList(){
		chatList = new ArrayList<ChatListItem>();
	}
	
	void initUI(View rootView){
		initTaskButton(rootView);
		initListAdapter(rootView);
	}
	
	private void initTaskButton(View rootView) {
		Button sendButton = (Button) rootView.findViewById(R.id.send_button);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText edit = (EditText) getActivity().findViewById(R.id.chat_input_container);
				String sendingMessage = edit.getText().toString();
				addMessage(sendingMessage);
				edit.setText("");
			}
		});
	}

	private void initListAdapter(View rootView) {
		ListView list = (ListView) rootView.findViewById(R.id.message_list);
		chatListAdapter = new ChatListAdapter(getActivity(), chatList);
		list.setAdapter(chatListAdapter);
	}
	
	void addMessage(String sendMessage){
		if (sendMessage.equals("")) {
			return;
		} else {
			chatList.add(new ChatListItem(sendMessage, "Myself"));
			chatListAdapter.notifyDataSetChanged();
			ListView list = (ListView) getActivity().findViewById(R.id.message_list);
			list.setSelection(chatListAdapter.getCount()-1);
		}
	}

	
	void initChat(){
		SocketIO socket = null;
		try {
			socket = new SocketIO(AppConfig.server.CHAT_URL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(socket != null){
			socket.connect(this);
			socket.send("Hello Server");
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}
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
