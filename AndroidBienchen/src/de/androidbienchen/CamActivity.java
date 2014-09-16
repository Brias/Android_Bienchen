package de.androidbienchen;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
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
	
	@Override
	public void onStart(){
		super.onStart();
		
		init();
		fetchingData();
	}
	
	void init(){
		imageFetcher = new ImageFetcher(getActivity(),this);
		timer = new ImageFetcherTimer(getActivity(), this);
		db = new LocationDatabase(getActivity());
		try {
			bm = db.getImage();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	void fetchingData(){
		if(NetworkAvailability.networkStatus(getActivity())){
			db.open();
			imageFetcher.startFetchingData();
			
		}else{
			Toast.makeText(getActivity(), "Keine Internetverbindung vorhanden",
					Toast.LENGTH_SHORT).show();
			setImageContent();
		}
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
			ImageView v = (ImageView) getActivity().findViewById(R.id.cam_image);
			v.setImageBitmap(this.bm);
		}else{
			Toast.makeText(getActivity(), "Keine Anzeige möglich", Toast.LENGTH_SHORT).show();
		}
	}
}
