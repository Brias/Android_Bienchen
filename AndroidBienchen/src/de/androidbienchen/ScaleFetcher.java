package de.androidbienchen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

public class ScaleFetcher {

	private ArrayList<String> resultArrayList;
	private DataFetcherListener listener;
	private LocationDatabase db;
	
	public ScaleFetcher(DataFetcherListener listener, Context context){
		init(listener, context);
	}
	
	private void init(DataFetcherListener listener, Context context){
		this.listener = listener;
		db = new LocationDatabase(context);
		resultArrayList = new ArrayList<String>();
	}
	
	public void startFetchingData(){
		new BackgroundTask().execute(AppConfig.server.SCALE_URL);
	}
	
	private void readJSON(String json) throws JSONException{
		JSONObject jsonObject = new JSONObject(json);
		jsonObject.getJSONArray("");
	}
	
	
	private class BackgroundTask extends AsyncTask<String, Void, String>{

		@Override
		protected void onPostExecute(String result){
			callReadJSON(result);
			super.onPostExecute(result);
		}
		
		private void callReadJSON(String result){
			try {
				readJSON(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		protected String doInBackground(String... url) {
			startHttpRequest(url[0]);
			return null;
		}
		
		private String startHttpRequest(String url){
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response;
				try{
					response = httpClient.execute(new HttpGet(url));
					StatusLine statusLine = response.getStatusLine();
					if(statusLine.getStatusCode() == HttpStatus.SC_OK){
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						resultArrayList.add(out.toString());
					}else{
						response.getEntity().getContent().close();
						throw new IOException(statusLine.getReasonPhrase());
					}
				} catch (Exception e){
					e.printStackTrace();
				}
			return resultArrayList.get(0);
		}
	}
}
