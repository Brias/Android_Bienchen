package de.androidbienchen.activities;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import de.androidbienchen.R;
import de.androidbienchen.data.AppConfig;

public class ChatActivity extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_chat,
				container, false);
		return rootView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initChat();
	}
	
	void initChat(){
		SocketIO socket = null;
		try {
			socket = new SocketIO(AppConfig.server.CHAT_URL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket.connect(new IOCallback() {
			
			@Override
			public void onMessage(JSONObject arg0, IOAcknowledge arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMessage(String arg0, IOAcknowledge arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(SocketIOException arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDisconnect() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onConnect() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void on(String arg0, IOAcknowledge arg1, Object... arg2) {
				// TODO Auto-generated method stub
				
			}
		});
		
		socket.send("Hello Server");
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
}
