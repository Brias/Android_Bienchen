package de.androidbienchen;

import java.util.ArrayList;

public interface ScaleResultListener {
	public void onNewScaleResult(ArrayList<Weight> weights);
	public void onNewTemperatureResult(ArrayList<Temperature> temperatures);
}
