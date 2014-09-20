package de.androidbienchen;

import java.util.ArrayList;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

public class ScaleActivity extends Fragment implements DataFetcherListener{

	private LineGraphView graphView;
	private ArrayList<Temperature> temperatures;
	private ArrayList<Weight> weights;
	private LocationDatabase db;
	private ScaleFetcher scaleFetcher;
	private TemperatureFetcher temperatureFetcher;
	
	
	public ScaleActivity(){

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
		init();
		fetchData();
	}
	
	void init(){
		graphView = new LineGraphView(getActivity(), "Statistic");
		scaleFetcher = new ScaleFetcher(this, getActivity());
		temperatureFetcher = new TemperatureFetcher(this, getActivity());
		db = new LocationDatabase(getActivity());
		db.open();
		try {
			temperatures = db.getTemperatures();
			weights = db.getWeights();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	void fetchData(){
		if(NetworkAvailability.networkStatus(getActivity())){
			temperatureFetcher.startFetchingData();
			scaleFetcher.startFetchingData();
		}else{
			setupTemperatureGraph();
			setupWeightGraph();
			Toast.makeText(getActivity(), "Keine Internetverbindung vorhanden SCALE",
					Toast.LENGTH_SHORT).show();
		}
		showGraph();
	}
		
	void showGraph(){
		graphView.setViewPort(0, 30);
		//graphView.setScalable(true);
		graphView.setShowLegend(true);
		graphView.setLegendAlign(LegendAlign.TOP);
		graphView.setLegendWidth(200);
		graphView.setBackgroundColor(getResources().getColor(R.color.black));
		LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.statistic_layout);
		layout.addView(graphView);
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
		this.weights = weights;
		setupWeightGraph();
	}

	@Override
	public void onTemperatureDataFetched(ArrayList<Temperature> temperatures) {
		this.temperatures = temperatures;
		setupTemperatureGraph();
	}

	void setupWeightGraph(){
		int num = 30;
		GraphViewData[] data = new GraphViewData[num];
		
		for(int i = 0; i < data.length; i++){
			data[i] = new GraphViewData(i, weights.get(i).getScaleValue());
		}
		
		GraphViewSeries scaleGraph = new GraphViewSeries("Gewicht", new GraphViewSeriesStyle(Color.RED, 3), data);
		graphView.addSeries(scaleGraph);
	}
	
	void setupTemperatureGraph(){
		int num = 30;
		GraphViewData[] data = new GraphViewData[num];
		
		for(int i = 0; i < data.length; i++){
			data[i] = new GraphViewData(i, temperatures.get(i).getTemperatureValue());
		}
		GraphViewSeries temperatureGraph = new GraphViewSeries("Temperatur", new GraphViewSeriesStyle(Color.BLUE, 3), data);
		graphView.addSeries(temperatureGraph);
	}
}
