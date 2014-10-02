package de.androidbienchen.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import de.androidbienchen.activities.PresenceStatusActivity;

public class ReachedReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean entering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
		if(entering) {
			launchActivity(context);
		}
	}

	private void launchActivity(Context context) {
		Intent intent = new Intent();
		intent.putExtra(PresenceStatusActivity.EXTRA_KEY_PROXMITY_REACHED, true);
		intent.setClassName("de.androidbienchen.activities", "de.androidbienchen.activities.PresenceStatusActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
	}

}
