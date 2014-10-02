package de.androidbienchen.chathelper;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.androidbienchen.R;



public class ChatListAdapter extends ArrayAdapter<ChatListItem>{
	
	private List<ChatListItem> chatList;
	private Context context;

	public ChatListAdapter(Context context, List<ChatListItem> chatList) {
		super(context, R.id.message_list, chatList);

		this.context = context;
		this.chatList = chatList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.chat_income_message_item, null);

		}

		ChatListItem chatItem = chatList.get(position);

		if (chatItem != null) {
			TextView message = (TextView) v.findViewById(R.id.message_container);
			TextView receivedDate = (TextView) v.findViewById(R.id.message_date);
			TextView username = (TextView) v.findViewById(R.id.username_container);
			
			message.setText(chatItem.getTask());
			receivedDate.setText(chatItem.getDate());
			username.setText(chatItem.getUsername());
		}

		return v;
	}
}
