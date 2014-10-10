package de.androidbienchen.chathelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

public class ChatListItem {
	public static final String JSON_USERNAME = "user";
	public static final String JSON_DATE = "date";
	public static final String JSON_MESSAGE = "message";
	
	private String message;
	private String date;
	private String username;
	
	public ChatListItem(String message, String username) {
		this.message = message;
		this.username = username;
		initDate();
	}
	
	public ChatListItem(String message, String username, String date) {
		this.message = message;
		this.username = username;
		this.date = date;
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
	
	public JSONObject getItemAsJSONObject(){
		JSONObject item = new JSONObject();
		try{
			item.put(JSON_USERNAME, username);
			item.put(JSON_DATE, date);
			item.put(JSON_MESSAGE, message);
		}catch(Exception e){
			
		}
		return item;
	}
}
