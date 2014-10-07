package de.androidbienchen.activities;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
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



public class PresenceStatusActivity extends Fragment implements UserStatusReceivedListener{
	
	public static final String ACTION_FILTER = "de.androidbienchen.intent.action.PresenceStatus";
	public static final String EXTRA_KEY_IN_RADIUS = "Present";
//	public static final double LATITUDE = 48.59662;	 //location data
//	public static final double LONGITUDE = 12.02154; //of Bienenstand
	//public static final double LATITUDE = 49.03129;
	//public static final double LONGITUDE = 11.97801;
	public static final double LATITUDE = 49.03129100;
	public static final double LONGITUDE = 11.9780100;
	public static final float PROXIMITY_RADIUS = 100f;
	
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
	
	void initProxmityAlert() {	
		Criteria criteria = getCriteria();
		
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		String bestProvider =
		locationManager.getBestProvider(criteria,true);
		locationManager.getLastKnownLocation(bestProvider);
		getActivity().registerReceiver(new ReachedReceiver(this), new IntentFilter(ACTION_FILTER));
		Intent inRadiusIntent = new Intent();
		inRadiusIntent.setAction(ACTION_FILTER);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(getActivity(), -1, inRadiusIntent, 0);
		locationManager.addProximityAlert(LATITUDE, LONGITUDE, PROXIMITY_RADIUS, -1, proximityIntent);
	}
	
	private Criteria getCriteria(){
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);
		return criteria;
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
		socketIOHelper.sendPresenceStatus(new PresenceStatusItem(username, id, status));
	}
	  
	void updateUIThere(String username){
		statusList.add(new PresenceStatusItem(db.getUserIdentification().getUsername(), db.getUserIdentification().getAndroidId(), true));
		notifyListChanges();
	  }
	  
	void updateUINotThere(String username){
		  for(int i = 0; i < statusList.size(); i++){
			  if(statusList.get(i).getUsername().equals(username)){
				  statusList.remove(i);
				  break;
			  }
		  }
		  statusList.add(new PresenceStatusItem("EXIT LOCATION", db.getUserIdentification().getAndroidId(), false));
		  notifyListChanges();
	  }
	  
	  void notifyListChanges(){
		  statusListAdapter.notifyDataSetChanged();
	  }
	  
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onUserStatusReceived(JSONObject object) {
		// TODO Auto-generated method stub
		try {
			if(object.getBoolean("status")){
				updateUIThere(object.getString("username"));
			}else{
				updateUINotThere(object.getString("username"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}