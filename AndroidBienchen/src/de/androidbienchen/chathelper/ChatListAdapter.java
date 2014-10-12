package de.androidbienchen.chathelper;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.androidbienchen.R;

public class ChatListAdapter extends ArrayAdapter<ChatListItem> {

	private List<ChatListItem> chatList;
	private Context context;
	private String id;

	public ChatListAdapter(Context context, List<ChatListItem> chatList,
			String id) {
		super(context, R.id.message_list, chatList);

		this.context = context;
		this.chatList = chatList;
		this.id = id;
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

		LinearLayout rightLayout = (LinearLayout) v.findViewById(R.id.right_chat_item_layout);
		LinearLayout leftLayout =  (LinearLayout) v.findViewById(R.id.left_chat_item_layout);
		TextView message_Left = (TextView) v
				.findViewById(R.id.message_container_left);
		TextView receivedDate_Left = (TextView) v
				.findViewById(R.id.message_date_left);
		TextView username_Left = (TextView) v
				.findViewById(R.id.username_container_left);
		
		TextView message_Right = (TextView) v
				.findViewById(R.id.message_container_right);
		TextView receivedDate_Right = (TextView) v
				.findViewById(R.id.message_date_right);
		TextView username_Right = (TextView) v
				.findViewById(R.id.username_container_right);	
		
		if (chatItem != null) {
			if (chatItem.getId().equals(id)) {
				setText(message_Right, username_Right, receivedDate_Right, chatItem, rightLayout, context.getResources().getColor(R.color.start_menu_button_color_light));
				setTextEmpty(message_Left, username_Left, receivedDate_Left, leftLayout);
			} else {
				setText(message_Left, username_Left, receivedDate_Left, chatItem, leftLayout, context.getResources().getColor(R.color.start_menu_button_color_dark));
				setTextEmpty(message_Right, username_Right, receivedDate_Right, rightLayout);
			}
		}

		return v;
	}

	private void setTextEmpty(TextView message, TextView username, TextView date, LinearLayout layout) {
		message.setText("");
		date.setText("");
		username.setText("");
		layout.setBackgroundResource(0);
	}

	private void setText(TextView message, TextView username, TextView date,
			ChatListItem chatItem, LinearLayout layout, int color) {
		message.setText(chatItem.getMessage());
		date.setText(chatItem.getDate());
		username.setText(chatItem.getUsername());
		
		layout.setBackgroundColor(color);
	}
}
