package de.androidbienchen;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class StartMenuActivity extends Activity implements DataFetcherListener, ImageFetcherListener{

	private LocationDatabase db;
	private ArrayList<Temperature> temperatures;
	private ArrayList<Weight> weights;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        db = new LocationDatabase(this);
        ImageFetcher imageFetcher = new ImageFetcher(this, this);
        ScaleFetcher scaleFetcher = new ScaleFetcher(this, this);
        TemperatureFetcher temperatureFetcher = new TemperatureFetcher(this, this);
        if(NetworkAvailability.networkStatus(this)){
	        db.open();
	        temperatureFetcher.startFetchingData();
	        scaleFetcher.startFetchingData();
	        imageFetcher.startFetchingData();
        }else{
        	weights = db.getWeights();
            temperatures = db.getTemperatures();
        }
        Timer myTimer = new Timer();
        ImageFetcherTimer imageFetcherTimer = new ImageFetcherTimer(this, this);
        myTimer.schedule(imageFetcherTimer, 0, 10000);
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
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
		ImageView view = (ImageView) findViewById(R.id.image);
		view.setImageBitmap(bm);
	}

}
