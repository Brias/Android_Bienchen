package de.androidbienchen;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ScaleActivity extends Fragment implements DataFetcherListener, ImageFetcherListener{

	private LocationDatabase db;
	private ArrayList<Temperature> temperatures;
	private ArrayList<Weight> weights;
	private Context context;
	
	
	public ScaleActivity(Context context){
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_scale,
				container, false);
		return rootView;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		db = new LocationDatabase(getActivity());
        ImageFetcher imageFetcher = new ImageFetcher(context, this);
        ScaleFetcher scaleFetcher = new ScaleFetcher(this, context);
        TemperatureFetcher temperatureFetcher = new TemperatureFetcher(this, context);
        if(NetworkAvailability.networkStatus(context)){
	        db.open();
	        temperatureFetcher.startFetchingData();
	        scaleFetcher.startFetchingData();
	        imageFetcher.startFetchingData();
        }else{
        	weights = db.getWeights();
            temperatures = db.getTemperatures();
        }
        Timer myTimer = new Timer();
        ImageFetcherTimer imageFetcherTimer = new ImageFetcherTimer(context, this);
        myTimer.schedule(imageFetcherTimer, 0, 600000);
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
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}
	}

	@Override
	public void onScaleDataFetched(ArrayList<Weight> weights) {
		// TODO Auto-generated method stub
		this.weights = weights;
	}

	@Override
	public void onTemperatureDataFetched(ArrayList<Temperature> temperatures) {
		// TODO Auto-generated method stub
		this.temperatures = temperatures;
	}

	@Override
	public void onImageFetched(Bitmap bm) {
		// TODO Auto-generated method stub
		ImageView view = (ImageView) getView().findViewById(R.id.image_view);
		view.setImageBitmap(bm);
	}

}
