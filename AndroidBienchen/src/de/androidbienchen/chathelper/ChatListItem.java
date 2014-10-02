package de.androidbienchen.chathelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatListItem {

	private String message;
	private String date;
	private String username;
	
	public ChatListItem(String message, String username) {
		this.message = message;
		this.username = username;
		initDate();
	}
	
	private void initDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.GERMAN);
		this.date = dateFormat.format(new Date());
	}

	public String getMessage() {
		return message;
	}

	public String getDate() {
		return date;
	}
	
	public String getUsername(){
		return username;
	}
}
