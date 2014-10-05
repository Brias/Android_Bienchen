package de.androidbienchen.activities;

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
import de.androidbienchen.R;
import de.androidbienchen.camhelper.ImageFetcher;
import de.androidbienchen.camhelper.ImageFetcherTimer;
import de.androidbienchen.data.LocationDatabase;
import de.androidbienchen.data.NetworkAvailability;
import de.androidbienchen.listener.ImageFetcherListener;
import de.androidbienchen.listener.UpdateStatusListener;

public class CamActivity extends Fragment implements ImageFetcherListener{

	private ImageFetcher imageFetcher;
	ImageFetcherTimer timer;
	LocationDatabase db;
	Bitmap bm;
	UpdateStatusListener listener;
	
	public CamActivity(UpdateStatusListener listener){
		this.listener = listener;
	}
	
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
		Log.d("USERID", ""+db.getUserIdentification().getAndroidId());
		Log.d("USERNAME", db.getUserIdentification().getUsername());
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
				imageFetcher.startFetchingData();	
		}else{
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
		informListener();
	}
	
	void setImageContent(){
		if(bm != null){
			try{
				ImageView v = (ImageView) getActivity().findViewById(R.id.cam_image);
				v.setImageBitmap(this.bm);
			}catch(Exception e){
			}	
		}else{
			listener.onLocalDataError(this.toString());
		}
	}
	
	void informListener(){
		listener.onUpdateFinished();
	}
}
