package de.androidbienchen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class CamActivity extends Activity implements ImageFetcherListener{

	private ImageFetcher imageFetcher;
	ImageFetcherTimer timer;
	LocationDatabase db;
	Bitmap bm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cam);

		init();
		fetchingData();
	}
	
	void init(){
		imageFetcher = new ImageFetcher(this, this);
		timer = new ImageFetcherTimer(this, this);
		db = new LocationDatabase(this);
		try {
			bm = db.getImage();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	void fetchingData(){
		if(NetworkAvailability.networkStatus(this)){
			db.open();
			imageFetcher.startFetchingData();
			
		}else{
			Toast.makeText(getApplicationContext(), "Keine Internetverbindung vorhanden",
					Toast.LENGTH_SHORT).show();
			setImageContent();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cam, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onImageFetched(Bitmap bm) {
		this.bm = bm;
		setImageContent();
	}
	
	void setImageContent(){
		if(bm != null){
			ImageView v = (ImageView) findViewById(R.id.imageView1);
			v.setImageBitmap(this.bm);
		}else{
			Toast.makeText(getApplicationContext(), "Keine Anzeige möglich", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		db.close();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		db.open();
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		db.open();
		super.onRestart();
	}
}
