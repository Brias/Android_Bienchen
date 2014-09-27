package de.androidbienchen.listener;

import java.util.ArrayList;

import de.androidbienchen.statistichelper.Temperature;
import de.androidbienchen.statistichelper.Weight;

public interface DataFetcherListener {

	public void onScaleDataFetched(ArrayList<Weight> weights);
	public void onTemperatureDataFetched(ArrayList<Temperature> temperatures);
}
