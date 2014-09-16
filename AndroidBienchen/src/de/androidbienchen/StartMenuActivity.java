package de.androidbienchen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;




public class StartMenuActivity extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
    }
    
    void init(){
    	//Button anwesend = (Button) findViewById(R.id.anwesend);
    	//Button chat = (Button) findViewById(R.id.chat);
    	Button kalender = (Button) findViewById(R.id.kalender);
    	Button statistik = (Button) findViewById(R.id.statistik);
    	Button cam = (Button) findViewById(R.id.cam);
    	Button website = (Button) findViewById(R.id.website);
    	
    	//anwesend.setOnClickListener(this);
    	//chat.setOnClickListener(this);
    	kalender.setOnClickListener(this);
    	statistik.setOnClickListener(this);
    	cam.setOnClickListener(this);
    	website.setOnClickListener(this);
    }

    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Intent intent = new Intent(StartMenuActivity.this, CalendarActivity.class);
        	startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.anwesend:
				startNextActivity(PresenceStatusActivity.class);
				break;
			case R.id.chat:
				startNextActivity(ChatActivity.class);
				break;
			case R.id.kalender:
				startNextActivity(CalendarActivity.class);
				break;
			case R.id.cam:
				startNextActivity(CamActivity.class);
				break;
			case R.id.statistik:
				startNextActivity(ScaleActivity.class);
				break;
			case R.id.website:
				startWebsite();
				break;
		}
	}
	
	void startWebsite(){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.server.WEBSITE_URL));
		startActivity(browserIntent);
	}
	
	@SuppressWarnings("rawtypes")
	void startNextActivity(Class nextActivity){
		Intent intent = new Intent(this, nextActivity);
		startActivity(intent);
	}
}
