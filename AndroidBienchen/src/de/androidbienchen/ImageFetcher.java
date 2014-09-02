package de.androidbienchen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class ImageFetcher {

	private LocationDatabase db;
	private ImageFetcherListener listener;
	
	public ImageFetcher(Context context, ImageFetcherListener listener){
		init(context, listener);
	}
	
	private void init(Context context, ImageFetcherListener listener){
		db = new LocationDatabase(context);
		this.listener = listener;
	}
	
	public void startFetchingData(){
		new BackgroundTask().execute(AppConfig.server.IMAGE_URL);
	}
	
	private void processImage(Bitmap bm){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] img = stream.toByteArray();
		
		if(db.removeImage()){
			storeImageInDatabase(img);
		}
		setDataFetched();
	}
	
	private void storeImageInDatabase(byte[] img){
		 db.insertImage(img);
	}
	
	private void setDataFetched(){
		listener.onImageFetched(getImageFromDatabase());
	}
	
	private Bitmap getImageFromDatabase(){
		return db.getImage();
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
	
