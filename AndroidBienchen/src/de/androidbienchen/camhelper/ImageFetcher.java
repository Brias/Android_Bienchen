package de.androidbienchen.camhelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import de.androidbienchen.data.AppConfig;
import de.androidbienchen.listener.ImageFetcherListener;

public class ImageFetcher {

	private ImageFetcherListener listener;
	
	public ImageFetcher(Context context, ImageFetcherListener listener){
		init(context, listener);
	}
	
	private void init(Context context, ImageFetcherListener listener){
		this.listener = listener;
	}
	
	public void startFetchingData(){
		new BackgroundTask().execute(AppConfig.server.IMAGE_URL);
	}
	
	public void stopFetchingData(){
		new BackgroundTask().cancel(true);
	}
	
	private void processImage(Bitmap bm){
		setDataFetched(bm);
	}
	
	private void setDataFetched(Bitmap bm){
		listener.onImageFetched(bm);
	}
	
	private class BackgroundTask extends AsyncTask<String, Void, Bitmap>{

		@Override 
		protected void onPostExecute(Bitmap bm){
			processImage(bm);
			super.onPostExecute(bm);
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			return getImage(params[0]);
		}
		
		private Bitmap getImage(String adress){
			URL url = getURL(adress);
			HttpURLConnection connection = getHttpConnection(url);
			InputStream is = getInputSTream(connection);
			
			Bitmap img = BitmapFactory.decodeStream(is);
			
			return img;
		}
		
		private URL getURL(String adress){
			String imageUrl= adress;
			
			URL url = null;
			try {
				url = new URL(imageUrl);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return url;
		}
		
		private HttpURLConnection getHttpConnection(URL url){
			HttpURLConnection connection = null;
			try {
				connection = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return connection;
		}
		
		private InputStream getInputSTream(HttpURLConnection connection){
			InputStream is = null;
			try {
				is = connection.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return is;
		}
	}
}
	
