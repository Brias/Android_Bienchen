package de.androidbienchen;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;

import de.androidbienchen.geofence.GeofenceUtils;
import de.androidbienchen.geofence.SimpleGeofence;
import de.androidbienchen.geofence.GeofenceActivity.GeofenceSampleReceiver;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CopyOfPresenceStatusActivity extends FragmentActivity {
	
	private SimpleGeofence mGeofence;
	private GeofenceSampleReceiver mBroadcastReceiver;
	private IntentFilter mIntentFilter;
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static String GEOFENCE_ID = "Bienenstock Imkerverein Sinzing";
    private final static double GEOFENCE_LATITUDE = 48.995038;
    private final static double GEOFENCE_LONGITUDE = 12.035295;
    private final static float GEOFENCE_RADIUS = 15;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_presence_status);
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

        // Create a new broadcast receiver to receive updates from the listeners and service
        mBroadcastReceiver = new GeofenceSampleReceiver();

        // Create an intent filter for the broadcast receiver
        mIntentFilter = new IntentFilter();

        // Action for broadcast Intents that report successful addition of geofences
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_ADDED);

        // Action for broadcast Intents that report successful removal of geofences
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_REMOVED);

        // Action for broadcast Intents containing various types of geofencing errors
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCE_ERROR);

        // All Location Services sample apps use this category
        mIntentFilter.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);
		
		
		if (servicesConnected() != false){
			createGeofence();
		}
	}

	private void createGeofence(){
		mGeofence = new SimpleGeofence(GEOFENCE_ID, GEOFENCE_LATITUDE, GEOFENCE_LONGITUDE,
		            GEOFENCE_RADIUS, Geofence.NEVER_EXPIRE,
		            Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);

		// Store this flat version in SharedPreferences
		//mPrefs.setGeofence("2", mGeofence);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.presence_status, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Geofence Detection",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Get the error code
            //int errorCode = connectionResult.getErrorCode();
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode, //errorCode
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(
                		getSupportFragmentManager(),
                        "Geofence Detection");
            }
            return false;
        }
    }
    
    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
     @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            //...
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    //...
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                    //...
                    break;
                }
            //...
        }
        //...
    }/**
      * Define a Broadcast receiver that receives updates from connection listeners and
      * the geofence transition service.
      */
     public class GeofenceSampleReceiver extends BroadcastReceiver {
         /*
          * Define the required method for broadcast receivers
          * This method is invoked when a broadcast Intent triggers the receiver
          */
         @Override
         public void onReceive(Context context, Intent intent) {
        	 
             // Check the action code and determine what to do
             String action = intent.getAction();

             // Intent contains information about errors in adding or removing geofences
             if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_ERROR)) {

                 handleGeofenceError(context, intent);

             // Intent contains information about successful addition or removal of geofences
             } else if (
                     TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_ADDED)
                     ||
                     TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_REMOVED)) {

                 handleGeofenceStatus(context, intent);

             // Intent contains information about a geofence transition
             } else if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_TRANSITION)) {

                 handleGeofenceTransition(context, intent);

             // The Intent contained an invalid action
             } else {
                 Log.e(GeofenceUtils.APPTAG, getString(R.string.invalid_action_detail, action));
                 Toast.makeText(context, R.string.invalid_action, Toast.LENGTH_LONG).show();
             }
         }

         /**
          * If you want to display a UI message about adding or removing geofences, put it here.
          *
          * @param context A Context for this component
          * @param intent The received broadcast Intent
          */
         private void handleGeofenceStatus(Context context, Intent intent) {

         }

         /**
          * Report geofence transitions to the UI
          *
          * @param context A Context for this component
          * @param intent The Intent containing the transition
          */
         private void handleGeofenceTransition(Context context, Intent intent) {
        	 
        	 
        	 
             /*
              * If you want to change the UI when a transition occurs, put the code
              * here. The current design of the app uses a notification to inform the
              * user that a transition has occurred.
              */
         }

         /**
          * Report addition or removal errors to the UI, using a Toast
          *
          * @param intent A broadcast Intent sent by ReceiveTransitionsIntentService
          */
         private void handleGeofenceError(Context context, Intent intent) {
             String msg = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS);
             Log.e(GeofenceUtils.APPTAG, msg);
             Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
         }
     }

    
    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        //...
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        //...
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        //...
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
        //...
    }

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_presence_status,
					container, false);
			return rootView;
		}
	}

}
