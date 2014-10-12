package de.androidbienchen.presencestatushelper;

import org.json.JSONObject;

public class PresenceStatusItem {

	public static final String JSON_USERNAME = "user";
	public static final String JSON_ID = "id";
	public static final String JSON_STATUS = "status";

	private String username;
	private String id;
	private boolean status;

	public PresenceStatusItem(String username, String id, boolean status) {
		this.username = username;
		this.id = id;
		this.status = status;
	}

	public PresenceStatusItem(String username, String id) {
		this.username = username;
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public String getId() {
		return id;
	}

	public boolean getStatus() {
		return status;
	}

	public JSONObject getPresenceItemAsJSONObject() {
		JSONObject item = new JSONObject();

		try {
			item.put(JSON_USERNAME, username);
			item.put(JSON_ID, id);
			item.put(JSON_STATUS, status);
		} catch (Exception e) {

		}

		return item;
	}
}
