package de.androidbienchen;

import java.util.ArrayList;

public interface DataFetcherListener {

	public void onScalDataFetched(ArrayList<Weight> weights);
	public void onTemperatureDataFetched(ArrayList<Temperature> temperatures);
}
