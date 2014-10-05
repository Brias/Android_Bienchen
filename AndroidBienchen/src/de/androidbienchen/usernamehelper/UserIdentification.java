package de.androidbienchen.usernamehelper;


public class UserIdentification {
	private String androidId;
	private String username;
	
	public UserIdentification(String username, String androidId){
		this.username = username;
		this.androidId = androidId;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getAndroidId(){
		return androidId;
	}
}
