package de.androidbienchen.activities;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.androidbienchen.R;
import de.androidbienchen.data.LocationDatabase;
import de.androidbienchen.geofence.ReachedReceiver;
import de.androidbienchen.listener.UserStatusReceivedListener;
import de.androidbienchen.presencestatushelper.PresenceListAdapter;
import de.androidbienchen.presencestatushelper.PresenceStatusItem;
import de.androidbienchen.socketiohelper.SocketIOHelper;
import de.androidbienchen.usernamehelper.UserIdentification;



public class PresenceStatusActivity extends Fragment implements UserStatusReceivedListener, LocationListener{
	
	public static final String ACTION_FILTER = "de.androidbienchen.intent.action.PresenceStatus";
	public static final String EXTRA_KEY_IN_RADIUS = "Present";
//	public static final double LATITUDE = 48.59662;	 //location data
//	public static final double LONGITUDE = 12.02154; //of Bienenstand
	public static final double LATITUDE = 49.03129100;
	public static final double LONGITUDE = 11.9780100;
	public static final float PROXIMITY_RADIUS = 100f;
	
	private ReachedReceiver receiver;
	
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	
	private ArrayList<PresenceStatusItem> statusList;
	private PresenceListAdapter statusListAdapter;
	private LocationDatabase db;
	private SocketIOHelper socketIOHelper;
	
	public PresenceStatusActivity(SocketIOHelper socketIOHelper){
		this.socketIOHelper = socketIOHelper;
		setListener();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_presence_status,
				container, false);
		
		initDatabase();
		initList();
		initListAdapter(rootView);
		
		return rootView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initReceiver();
		initProxmityAlert();
	}
	
	void setListener(){
		socketIOHelper.setUserStatusReceivedListener(this);
	}
	
	void initDatabase(){
		db = new LocationDatabase(getActivity());
	}
	
	private UserIdentification getUserIdentification(){
		return db.getUserIdentification();
	}
	
	void initReceiver(){
		receiver = new ReachedReceiver(this);
	}
	
	void initProxmityAlert() {	
		
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		getActivity().registerReceiver(receiver, new IntentFilter(ACTION_FILTER));
		Intent inRadiusIntent = new Intent();
		inRadiusIntent.setAction(ACTION_FILTER);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(getActivity(), -1, inRadiusIntent, 0);
		locationManager.addProximityAlert(LATITUDE, LONGITUDE, PROXIMITY_RADIUS, -1, proximityIntent);
	}
	
	void initList(){
		statusList = new ArrayList<PresenceStatusItem>();
	}
	
	void initListAdapter(View rootView) {
		ListView list = (ListView) rootView.findViewById(R.id.status_list);
		statusListAdapter = new PresenceListAdapter(getActivity(), statusList);
		list.setAdapter(statusListAdapter);
	}
	
	public void sendStatus(boolean status){
		UserIdentification userIdentification = getUserIdentification();
		String username = userIdentification.getUsername();
		String id = userIdentification.getAndroidId();
		if(socketIOHelper.socketConnected()){
			socketIOHelper.sendPresenceStatus(new PresenceStatusItem(username, id, status));
		}
	}
	  
	void updateUIThere(String username, String id){
		statusList.add(new PresenceStatusItem(username, id));
		notifyListChanges();
	  }
	  
	void updateUINotThere(String id){
	  for(int i = 0; i < statusList.size(); i++){
		  if(statusList.get(i).getId().equals(id)){
			  statusList.remove(i);
			  break;
		  }
	  }
	}
	  
	void notifyListChanges(){
		statusListAdapter.notifyDataSetChanged();
	}
	  
	void updatePresenceStatusView(JSONObject object){
		try {
			if(object.getBoolean(PresenceStatusItem.JSON_STATUS)){
				updateUIThere(object.getString(PresenceStatusItem.JSON_USERNAME), object.getString(PresenceStatusItem.JSON_ID));
			}else{
				updateUINotThere(object.getString(PresenceStatusItem.JSON_USERNAME));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  
	void requestCurrentStatuses(){
		socketIOHelper.getCurrentStatuses();
	}
	  
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}

	@Override
	public void onUserStatusReceived(JSONObject object) {
		// TODO Auto-generated method stub
		updatePresenceStatusView(object);
	}
	
	@Override
	public void onSocketIOConnected() {
		// TODO Auto-generated method stub
		if(socketIOHelper.socketConnected()){
			requestCurrentStatuses();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}