package de.androidbienchen.chathelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

public class ChatListItem {
	public static final String JSON_USERNAME = "user";
	public static final String JSON_DATE = "date";
	public static final String JSON_MESSAGE = "message";
	public static final String JSON_ID = "id";
	
	private String message;
	private String date;
	private String username;
	private String id;
	
	public ChatListItem(String message, String username, String id) {
		this.message = message;
		this.username = username;
		this.id = id;
		initDate();
	}
	
	public ChatListItem(String message, String username, String date, String id) {
		this.message = message;
		this.username = username;
		this.date = date;
		this.id = id;
	}
	
	private void initDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.GERMAN);
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
	
	public String getId(){
		return id;
	}
	
	public JSONObject getItemAsJSONObject(){
		JSONObject item = new JSONObject();
		try{
			item.put(JSON_USERNAME, username);
			item.put(JSON_DATE, date);
			item.put(JSON_MESSAGE, message);
			item.put(JSON_ID, id);
		}catch(Exception e){
			
		}
		return item;
	}
}
