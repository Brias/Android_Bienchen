package de.androidbienchen.listener;

import java.util.ArrayList;

import de.androidbienchen.statistichelper.Weight;

public interface ScaleResultListener {
	public void onNewResult(ArrayList<Weight> weights);
}
