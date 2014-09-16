package de.androidbienchen;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

public class ScaleActivity extends Activity implements DataFetcherListener{

	private LineGraphView graphView;
	private ArrayList<Temperature> temperatures;
	private ArrayList<Weight> weights;
	private LocationDatabase db;
	private ScaleFetcher scaleFetcher;
	private TemperatureFetcher temperatureFetcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scale);
		
		init();
		fetchData();
	}
	
	void init(){
		graphView = new LineGraphView(this, "Statistic");
		scaleFetcher = new ScaleFetcher(this,this);
		temperatureFetcher = new TemperatureFetcher(this,this);
		db = new LocationDatabase(this);
		db.open();
		try {
			temperatures = db.getTemperatures();
			weights = db.getWeights();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	void fetchData(){
		if(NetworkAvailability.networkStatus(this)){
			temperatureFetcher.startFetchingData();
			scaleFetcher.startFetchingData();
		}else{
			setupTemperatureGraph();
			setupWeightGraph();
			Toast.makeText(getApplicationContext(), "Keine Internetverbindung vorhanden",
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
		LinearLayout layout = (LinearLayout) findViewById(R.id.statistic_layout);
		layout.addView(graphView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scale, menu);
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
