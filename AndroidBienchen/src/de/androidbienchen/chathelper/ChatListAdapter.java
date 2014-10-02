package de.androidbienchen.chathelper;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ChatListAdapter extends ArrayAdapter<ChatListItem>{
	
	private List<ChatListItem> chatList;
	private Context context;

	public ChatListAdapter(Context context, List<ChatListItem> chatList) {
		super(context, R.id.chat_income_message_item, chatList);

		this.context = context;
		this.chatList = chatList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.todo_item, null);

		}

		ChatListItem task = chatList.get(position);

		if (task != null) {
			TextView message = (TextView) v.findViewById(R.id.task_name);
			TextView receivedDate = (TextView) v.findViewById(R.id.task_date);

			message.setText(task.getTask());
			receivedDate.setText(task.getDate());
		}

		return v;
	}
}
