package de.androidbienchen.presencestatushelper;

import org.json.JSONObject;

public class PresenceStatusItem {
	
	private String username;
	private String id;
	private boolean status;
	
	public PresenceStatusItem(String username, String id, boolean status) {
		this.username = username;
		this.id = id;
		this.status = status;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getId(){
		return id;
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public JSONObject getPresenceItemAsJSONObject(){
		JSONObject item = new JSONObject();
		
		try{
			item.put("username", username);
			item.put("id", id);
			item.put("status", status);
		}catch(Exception e){
		
		}
		
		return item;
	}
}
