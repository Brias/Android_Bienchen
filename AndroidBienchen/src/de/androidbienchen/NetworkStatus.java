package de.androidbienchen;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {
	
	public static Boolean isNetworkAvailable(Context context)  {
	    try{
	        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	        if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
	        	return true;
	        }
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    return false;
	}
}
