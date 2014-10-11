package de.androidbienchen.camhelper;

import java.util.TimerTask;

import android.content.Context;
import de.androidbienchen.listener.ImageFetcherListener;

public class ImageFetcherTimer extends TimerTask{

	private ImageFetcher imageFetcher;
	
	public ImageFetcherTimer(Context context, ImageFetcherListener listener){
		imageFetcher = new ImageFetcher(context, listener);
	}

	@Override
	public void run() {
		if(imageFetcher != null){
			imageFetcher.startFetchingData();
		}
	}
}
