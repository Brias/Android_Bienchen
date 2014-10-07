package de.androidbienchen.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import de.androidbienchen.activities.PresenceStatusActivity;

public class ReachedReceiver extends BroadcastReceiver{

	private static final String USERNAME = "Enter Location";
	PresenceStatusActivity presenceActivity;
	
	public ReachedReceiver(PresenceStatusActivity presenceActivity){
		this.presenceActivity = presenceActivity;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		boolean entering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
		Log.d("ONRECEIVE", "INRADIUS");
		updateUI(entering);
	}
	
	private void updateUI(boolean entering){
		if(entering) {
			presenceActivity.updateUIThere(USERNAME);
		}else{
			presenceActivity.updateUINotThere(USERNAME);
		}
	}
}
