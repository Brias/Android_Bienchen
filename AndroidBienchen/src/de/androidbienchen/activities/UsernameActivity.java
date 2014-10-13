package de.androidbienchen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.androidbienchen.R;
import de.androidbienchen.data.LocationDatabase;
import de.androidbienchen.usernamehelper.UserIdentification;

public class UsernameActivity extends Activity{
	private String androidId;
	
	private LocationDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.username_input);
		 if ( savedInstanceState == null ) {
			 initUserId();
			 initDatabase();
			 checkIfUsernameNecessary();
		}
	}
	
	void initUserId(){
		 androidId = Secure.getString(this.getContentResolver(),
	                Secure.ANDROID_ID);
	}
	
	void initDatabase(){
    	db = new LocationDatabase(this);
    }
    
    void openDatabase(){
    	db.open();
    }
    
    void closeDatabase(){
    	db.close();
    }
    
    void insertUserIdentificationIntoDatabse(UserIdentification ui){
		db.insertUserIdentification(ui);
		closeDatabase();
	}
    
    private String getUsernameOfDatabase(){
    	UserIdentification ui = db.getUserIdentification();
    	try{
    		String username = ui.getUsername();
    		return username;
    	}catch(Exception e){
    		
    	}
    	return null;
    }
    
    //If Username already exists it opens the MainActivity immediately
    void checkIfUsernameNecessary(){
    	openDatabase();
    	if(getUsernameOfDatabase() == null){
    		initButton(initEditText());
    	}else{
    		closeDatabase();
    		startMainApp();
    	}
    }
    
    void startMainApp(){
    	Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
    }
    
    void initButton(final EditText edit){
    	Button btn = (Button) findViewById(R.id.username_btn);
    	btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkEditText(edit);
			}
		});
    }
    
    private EditText initEditText(){
    	EditText edit = (EditText) findViewById(R.id.username_edit);
    	return edit;
    }
    
    void checkEditText(EditText edit){
    	if(!edit.getText().toString().equals("")){
    		initUserIdentification(edit.getText().toString());
    		startMainApp();
    	}
    }
    
    void initUserIdentification(String username){
    	UserIdentification ui = new UserIdentification(username, androidId);
    	insertUserIdentificationIntoDatabse(ui);
    }  
}
