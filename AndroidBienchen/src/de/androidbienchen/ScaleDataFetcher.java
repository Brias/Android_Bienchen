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
import android.util.Log;

public class ScaleDataFetcher {

	public static final String SCALE_URL = "http://app.iv-sinzing.de/waage";
	
	private Context context;	
	private ArrayList<Weight> weights;
	private ScaleResultListener listener;
	ArrayList<String> resultArrayList;
	
	public ScaleDataFetcher(Context context, ScaleResultListener listener){
		this.context = context;
		this.listener = listener;
		weights = new ArrayList<Weight>();
		resultArrayList = new ArrayList<String>();
	}
	
	public void executeHttpRequest(){
		new BackgroundTask().execute(SCALE_URL);
	}
	
	private void readJSON() throws JSONException{
		JSONObject jsonOnject = new JSONObject(resultArrayList.get(0));	
	}
	
	
	
	private void setWeights(){
		listener.onNewResult(weights);
	}
	
private class BackgroundTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... url) {
			return getJSONString(url);
		}
		
		@Override 
		protected void onPostExecute(String result){
			executeReadJSON();
			super.onPostExecute(result);
		}
		
		private void executeReadJSON(){
			try {
				readJSON();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private String getJSONString(String... url){
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response;
				try{
					response = httpClient.execute(new HttpGet(url[0]));
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
				Log.d("BackgroundTask", resultArrayList.get(0));
			return resultArrayList.get(0);
		}
	}
}
