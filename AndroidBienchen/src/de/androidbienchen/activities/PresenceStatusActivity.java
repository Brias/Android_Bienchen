package de.androidbienchen.activities;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.androidbienchen.R;



public class PresenceStatusActivity extends Fragment {
	
	public static final String CUSTOM_BROADCAST_URI = "de.androidbienchen.intent.action.PresenceStatus";
	public static final String EXTRA_KEY_PROXMITY_REACHED = "Present";
	public static final double LATITUDE = 49.01861;
	public static final double LONGITUDE = 12.11334;
	public static final float PROXIMITY_RADIUS = 50;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initProxmityAlert();
	}
	
	private void initProxmityAlert() {
		LocationManager locationManger = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		Intent kugelReachedIntent = new Intent();
		kugelReachedIntent.setAction(CUSTOM_BROADCAST_URI);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(getActivity(), -1, kugelReachedIntent, 0);
		
		locationManger.addProximityAlert(LATITUDE, LONGITUDE, PROXIMITY_RADIUS, -1, proximityIntent);
	}
	
	  @Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_presence_status,
					container, false);
			return rootView;
		}
	
	@Override
	public void onResume() {
		boolean kugelReached = getActivity().getIntent().getBooleanExtra(EXTRA_KEY_PROXMITY_REACHED, false);
		displayText(kugelReached);
		super.onResume();
	}
	
	private void displayText(Boolean reachedKugel) {
		TextView output = (TextView) getActivity().findViewById(R.id.statusView);
		String text;
		if(reachedKugel) {
			text = "DADADA";
		}else{
			text = "BlaBlaBLa";
		}
		output.setText(text);
	}

	

}



//public class PresenceStatusActivity extends Fragment {
//    /*
//     * Use to set an expiration time for a geofence. After this amount
//     * of time Location Services will stop tracking the geofence.
//     * Remember to unregister a geofence when you're finished with it.
//     * Otherwise, your app will use up battery. To continue monitoring
//     * a geofence indefinitely, set the expiration time to
//     * Geofence#NEVER_EXPIRE.
//     */
//    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
//    private static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
//           GEOFENCE_EXPIRATION_IN_HOURS * DateUtils.HOUR_IN_MILLIS;
//
//    // Store the current request
//    private REQUEST_TYPE mRequestType;
//
//    // Store the current type of removal
//    //private REMOVE_TYPE mRemoveType;
//
//    // Persistent storage for geofences
//    private SimpleGeofenceStore mPrefs;
//
//    // Store a list of geofences to add
//    List<Geofence> mCurrentGeofences;
//
//    // Add geofences handler
//    private GeofenceRequester mGeofenceRequester;
//    // Remove geofences handler
//    private GeofenceRemover mGeofenceRemover;
//
//    /*
//     * Internal lightweight geofence object
//     */
//    private SimpleGeofence mGeofence;
//
//    private ReceiveTransitionsIntentService transitionService;
//
//    /*
//     * An instance of an inner class that receives broadcasts from listeners and from the
//     * IntentService that receives geofence transition events
//     */
//    private GeofenceSampleReceiver mBroadcastReceiver;
//
//    // An intent filter for the broadcast receiver
//    private IntentFilter mIntentFilter;
//
//    // Store the list of geofences to remove
//    private List<String> mGeofenceIdsToRemove;
//
//    private final static String GEOFENCE_ID = "Bienenstock Imkerverein Sinzing";
//    private final static double GEOFENCE_LATITUDE = 48.59662;
//    private final static double GEOFENCE_LONGITUDE = 12.02154;
//    private final static float GEOFENCE_RADIUS = 100;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        
//        transitionService = new ReceiveTransitionsIntentService();
//        
//        
//        // Create a new broadcast receiver to receive updates from the listeners and service
//        mBroadcastReceiver = new GeofenceSampleReceiver();
//
//        // Create an intent filter for the broadcast receiver
//        mIntentFilter = new IntentFilter();
//
//        // Action for broadcast Intents that report successful addition of geofences
//        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_ADDED);
//
//        // Action for broadcast Intents that report successful removal of geofences
//        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_REMOVED);
//
//        // Action for broadcast Intents containing various types of geofencing errors
//        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCE_ERROR);
//
//        // All Location Services sample apps use this category
//        mIntentFilter.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);
//
//        // Instantiate a new geofence storage area
//        mPrefs = new SimpleGeofenceStore(getActivity());
//
//        // Instantiate the current List of geofences
//        mCurrentGeofences = new ArrayList<Geofence>();
//
//        // Instantiate a Geofence requester
//        mGeofenceRequester = new GeofenceRequester(getActivity());
//
//        // Instantiate a Geofence remover
//        mGeofenceRemover = new GeofenceRemover(getActivity());
//
//        // Attach to the main UI
//        //setContentView(R.layout.fragment_presence_status);
//        registerGeofence();
//
//    }
//    
//    @Override
//    public void onStart(){
//    	super.onStart();
//    	TextView a = (TextView) getActivity().findViewById(R.id.statusView);
//		a.setText("Start");
//    }
//    
//    @Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.fragment_presence_status,
//				container, false);
//		return rootView;
//	}
//
//    private void registerGeofence() {
//
//        /*
//         * Record the request as an ADD. If a connection error occurs,
//         * the app can automatically restart the add request if Google Play services
//         * can fix the error
//         */
//        mRequestType = GeofenceUtils.REQUEST_TYPE.ADD;
//
//        /*
//         * Check for Google Play services. Do this after
//         * setting the request type. If connecting to Google Play services
//         * fails, onActivityResult is eventually called, and it needs to
//         * know what type of request was in progress.
//         */
//        if (!servicesConnected()) {
//            return;
//        }
//
//        /*
//         * Create a version of geofence 1 that is "flattened" into individual fields. This
//         * allows it to be stored in SharedPreferences.
//         */
//		mGeofence = new SimpleGeofence(GEOFENCE_ID, GEOFENCE_LATITUDE, GEOFENCE_LONGITUDE,
//		            GEOFENCE_RADIUS, Geofence.NEVER_EXPIRE,
//		            Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
//
//        // Store this flat version in SharedPreferences
//        mPrefs.setGeofence(GEOFENCE_ID, mGeofence);
//
//        /*
//         * Add Geofence objects to a List. toGeofence()
//         * creates a Location Services Geofence object from a
//         * flat object
//         */
//        mCurrentGeofences.add(mGeofence.toGeofence());
//
//        // Start the request. Fail if there's already a request in progress
//        try {
//            // Try to add geofences
//            mGeofenceRequester.addGeofences(mCurrentGeofences);
//        } catch (UnsupportedOperationException e) {
//            // Notify user that previous request hasn't finished.
//            Toast.makeText(getActivity(), R.string.add_geofences_already_requested_error,
//                        Toast.LENGTH_LONG).show();
//        }
//	}
//
//	/*
//     * Whenever the Activity resumes, reconnect the client to Location
//     * Services and reload the last geofences that were set
//     */
//    @Override
//    public void onResume() {
//        super.onResume();
//        // Register the broadcast receiver to receive status updates
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, mIntentFilter);
//    }
//
//    /*
//     * Inflate the app menu
//     */
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.presence_status, menu);
////        return true;
////
////    }
//    /*
//     * Respond to menu item selections
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            // Pass through any other request
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    /*
//     * Save the current geofence settings in SharedPreferences.
//     */
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//
//    /**
//     * Verify that Google Play services is available before making a request.
//     *
//     * @return true if Google Play services is available, otherwise false
//     */
//    private boolean servicesConnected() {
//    	
//        // Check that Google Play services is available
//        int resultCode =
//                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
//
//        // If Google Play services is available
//        if (ConnectionResult.SUCCESS == resultCode) {
//
//            // In debug mode, log the status
//            Log.d(GeofenceUtils.APPTAG, getString(R.string.play_services_available));
//
//            // Continue
//            return true;
//
//        // Google Play services was not available for some reason
//        } else {
//
//            // Display an error dialog
//            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), 0);
//            if (dialog != null) {
//                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
//                errorFragment.setDialog(dialog);
//                errorFragment.show(getFragmentManager(), GeofenceUtils.APPTAG);
//            }
//            return false;
//        }
//    }
//
//    /**
//     * Define a Broadcast receiver that receives updates from connection listeners and
//     * the geofence transition service.
//     */
//    public class GeofenceSampleReceiver extends BroadcastReceiver {
//        /*
//         * Define the required method for broadcast receivers
//         * This method is invoked when a broadcast Intent triggers the receiver
//         */
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//        	//Log.d("Gefofence_Status", ""+intent.getExtras().getInt(GeofenceUtils.EXTRA_GEOFENCE_STATUS));
//        	//Log.d("Transition_Enter", ""+Geofence.GEOFENCE_TRANSITION_ENTER);
//        	//Log.d("Transition_Exit",""+Geofence.GEOFENCE_TRANSITION_EXIT);
//        	//Log.d("Receiver", "OnReceive");
//            // Check the action code and determine what to do
//            String action = intent.getAction();
//
//            // Intent contains information about errors in adding or removing geofences
//            if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_ERROR)) {
//
//                handleGeofenceError(context, intent);
//
//            // Intent contains information about successful addition or removal of geofences
//            } else if (
//                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_ADDED)
//                    ||
//                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_REMOVED)) {
//
//                handleGeofenceStatus(context, intent);
//
//            // Intent contains information about a geofence transition
//            }else if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_TRANSITION)) {
//
//                handleGeofenceTransition(context, intent);
//
//            // The Intent contained an invalid action
//            } else {
//                Log.e(GeofenceUtils.APPTAG, getString(R.string.invalid_action_detail, action));
//                Toast.makeText(context, R.string.invalid_action, Toast.LENGTH_LONG).show();
//            }
//        }
//
//        /**
//         * If you want to display a UI message about adding or removing geofences, put it here.
//         *
//         * @param context A Context for this component
//         * @param intent The received broadcast Intent
//         */
//        private void handleGeofenceStatus(Context context, Intent intent) {
//
//        }
//
//        /**
//         * Report geofence transitions to the UI
//         *
//         * @param context A Context for this component
//         * @param intent The Intent containing the transition
//         */
//        private void handleGeofenceTransition(Context context, Intent intent) {
//            /*
//             * If you want to change the UI when a transition occurs, put the code
//             * here. The current design of the app uses a notification to inform the
//             * user that a transition has occurred.
//             */
//        	int transitionType = intent.getExtras().getInt(GeofenceUtils.EXTRA_GEOFENCE_STATUS);
//        	
//        	if(transitionType == Geofence.GEOFENCE_TRANSITION_ENTER){
//        		
//        		//hier bilder wechseln, status auf ANWESEND setzten
//        		TextView a = (TextView) getActivity().findViewById(R.id.statusView);
//        		a.setText("anwesend");
//        		
//        	}else if(transitionType == Geofence.GEOFENCE_TRANSITION_EXIT){
//        		
//        		TextView a = (TextView) getActivity().findViewById(R.id.statusView);
//        		a.setText("abwesend");
//        		
//        	}
//        }
//
//        /**
//         * Report addition or removal errors to the UI, using a Toast
//         *
//         * @param intent A broadcast Intent sent by ReceiveTransitionsIntentService
//         */
//        private void handleGeofenceError(Context context, Intent intent) {
//            String msg = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS);
//            Log.e(GeofenceUtils.APPTAG, msg);
//            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//        }
//    }
//    /**
//     * Define a DialogFragment to display the error dialog generated in
//     * showErrorDialog.
//     */
//    public static class ErrorDialogFragment extends DialogFragment {
//
//        // Global field to contain the error dialog
//        private Dialog mDialog;
//
//        /**
//         * Default constructor. Sets the dialog field to null
//         */
//        public ErrorDialogFragment() {
//            super();
//            mDialog = null;
//        }
//
//        /**
//         * Set the dialog to display
//         *
//         * @param dialog An error dialog
//         */
//        public void setDialog(Dialog dialog) {
//            mDialog = dialog;
//        }
//
//        /*
//         * This method must return a Dialog to the DialogFragment.
//         */
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            return mDialog;
//        }
//    }
//}