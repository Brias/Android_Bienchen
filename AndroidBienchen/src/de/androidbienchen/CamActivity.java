package de.androidbienchen;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class CamActivity extends Fragment implements ImageFetcherListener{

	private ImageFetcher imageFetcher;
	ImageFetcherTimer timer;
	LocationDatabase db;
	Bitmap bm;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_cam,
				container, false);
		return rootView;
	}
	
	public void onAttach (Activity activity){
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initFetcher();
		init();
		fetchingData();
		openDatabase();
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	void initFetcher(){
		imageFetcher = new ImageFetcher(getActivity(),this);
	}
	
	void init(){
		timer = new ImageFetcherTimer(getActivity(), this);
		db = new LocationDatabase(getActivity());
	}
	
	boolean getImage(){
		try {
			bm = db.getImage();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	void openDatabase(){
		db.open();
	}
	
	void closeDatabse(){
		db.close();
	}
	
	void fetchingData(){
		if(NetworkAvailability.networkStatus(getActivity())){
				Toast.makeText(getActivity(), "Internetverbindung vorhanden",
						Toast.LENGTH_SHORT).show();
				imageFetcher.startFetchingData();	
		}else{
			Toast.makeText(getActivity(), "Keine Internetverbindung vorhanden",
					Toast.LENGTH_SHORT).show();
			setImageContent();
		}
	}

	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	public void onStop(){
		super.onStop();
		imageFetcher.stopFetchingData();
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
			try{
				ImageView v = (ImageView) getActivity().findViewById(R.id.cam_image);
				v.setImageBitmap(this.bm);
			}catch(Exception e){
			}	
		}else{
			Toast.makeText(getActivity(), "Keine Anzeige möglich", Toast.LENGTH_SHORT).show();
		}
	}
}
