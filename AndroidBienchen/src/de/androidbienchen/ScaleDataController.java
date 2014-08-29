package de.androidbienchen;

import java.util.ArrayList;

public class ScaleDataController implements ScaleResultListener{

	private ArrayList<Weight> weights;
	
	
	
	@Override
	public void onNewResult(ArrayList<Weight> weights) {
		this.weights = weights;
	}

}
