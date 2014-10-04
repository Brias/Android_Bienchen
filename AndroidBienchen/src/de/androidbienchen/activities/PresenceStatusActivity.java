package de.androidbienchen.activities;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.androidbienchen.R;
import de.androidbienchen.geofence.ReachedReceiver;
import de.androidbienchen.presencestatushelper.PresenceListAdapter;
import de.androidbienchen.presencestatushelper.PresenceStatusItem;



public class PresenceStatusActivity extends Fragment {
	
	public static final String ACTION_FILTER = "de.androidbienchen.intent.action.PresenceStatus";
	public static final String EXTRA_KEY_IN_RADIUS = "Present";
//	public static final double LATITUDE = 48.59662;	 //location data
//	public static final double LONGITUDE = 12.02154; //of Bienenstand
	public static final double LATITUDE = 49.03129;
	public static final double LONGITUDE = 11.97801;
	public static final float PROXIMITY_RADIUS = 30;
	
	private ArrayList<PresenceStatusItem> statusList;
	private PresenceListAdapter statusListAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_presence_status,
				container, false);
		
		initList();
		initListAdapter(rootView);
		
		return rootView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initProxmityAlert();
	}
	
	private void initProxmityAlert() {	
		getActivity().registerReceiver(new ReachedReceiver(this), new IntentFilter(ACTION_FILTER));
		LocationManager locationManger = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		Intent inRadiusIntent = new Intent();
		inRadiusIntent.setAction(ACTION_FILTER);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(getActivity(), -1, inRadiusIntent, 0);
		
		locationManger.addProximityAlert(LATITUDE, LONGITUDE, PROXIMITY_RADIUS, -1, proximityIntent);
	}
	
	private void initList(){
		statusList = new ArrayList<PresenceStatusItem>();
	}
	
	private void initListAdapter(View rootView) {
		ListView list = (ListView) rootView.findViewById(R.id.status_list);
		statusListAdapter = new PresenceListAdapter(getActivity(), statusList);
		list.setAdapter(statusListAdapter);
	}
	  
	  public void updateUIThere(String username){
		statusList.add(new PresenceStatusItem(username));
		notifyListChanges();
	  }
	  
	  public void updateUINotThere(String username){
		  for(int i = 0; i < statusList.size(); i++){
			  if(statusList.get(i).getUsername().equals(username)){
				  statusList.remove(i);
				  break;
			  }
			  notifyListChanges();
		  }
	  }
	  
	  void notifyListChanges(){
		  statusListAdapter.notifyDataSetChanged();
	  }
	  
	@Override
	public void onResume() {
		super.onResume();
	}
}