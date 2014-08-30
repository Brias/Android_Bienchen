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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class TemperatureFetcher {
	private ArrayList<String> resultArrayList;
	private DataFetcherListener listener;
	private LocationDatabase db;
	
	public TemperatureFetcher(DataFetcherListener listener, Context context){
		init(listener, context);
	}
	
	private void init(DataFetcherListener listener, Context context){
		this.listener = listener;
		db = new LocationDatabase(context);
		resultArrayList = new ArrayList<String>();
	}
	
	public void startFetchingData(){
		new BackgroundTask().execute(AppConfig.server.TEMPERATURE_URL);
	}
	
	private void openDatabase(){
		db.open();
	}
	
	private void closeDatabase(){
		db.close();
	}
	
	private void readJSON(String json) throws JSONException{
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("temperatur");
		if(db.removeAllTemperatureValues()){
			getJsonContent(jsonArray);
		}
		setTemperatureDataUpdated();
	}
	
	private void getJsonContent(JSONArray jsonArray){
		for(int i = 0; i < jsonArray.length(); i++){
			try {
				if(jsonArray.getJSONObject(i) == null){
					break;
				}else{
						insertDataInDatabase(jsonArray.getJSONObject(i));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void insertDataInDatabase(JSONObject jsonObject) throws JSONException{
			int id = jsonObject.getInt("_id");
			float temperature = (float) jsonObject.getDouble("_grad");
			String date = jsonObject.getString("_datum");
			Temperature newTemperature = new Temperature(temperature, id, date);
			db.insertTemperatureValue(newTemperature);
	}
	
	private void setTemperatureDataUpdated(){
		ArrayList<Temperature> temperatures = db.getTemperatures();
		closeDatabase();
		setDataFetched(temperatures);
	}
	
	private void setDataFetched(ArrayList<Temperature> temperatures){
		for(int i = 0; i < temperatures.size(); i++){
			Log.d("Temperatures from Database", ""+temperatures.get(i).getId()+" "+temperatures.get(i).getMeasureDate()+" "+temperatures.get(i).getTemperatureValue());
		}
		listener.onTemperatureDataFetched(temperatures);
	}
	
	private class BackgroundTask extends AsyncTask<String, Void, String>{

		@Override
		protected void onPostExecute(String result){
			openDatabase();
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
			return startHttpRequest(url[0]);
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
