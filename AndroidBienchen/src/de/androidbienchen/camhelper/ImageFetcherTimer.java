package de.androidbienchen.camhelper;

import java.util.TimerTask;

import de.androidbienchen.listener.ImageFetcherListener;

import android.content.Context;

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
