package de.androidbienchen.activities;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.androidbienchen.R;
import de.androidbienchen.data.LocationDatabase;
import de.androidbienchen.geofencehelper.ReachedReceiver;
import de.androidbienchen.listener.UserStatusReceivedListener;
import de.androidbienchen.presencestatushelper.PresenceListAdapter;
import de.androidbienchen.presencestatushelper.PresenceStatusItem;
import de.androidbienchen.socketiohelper.SocketIOHelper;
import de.androidbienchen.usernamehelper.UserIdentification;

public class PresenceStatusActivity extends Fragment implements
		UserStatusReceivedListener, Runnable {

	public static final String ACTION_FILTER = "de.androidbienchen.intent.action.PresenceStatus";
	public static final String EXTRA_KEY_IN_RADIUS = "Present";
	// public static final double LATITUDE = 48.59662; //location data
	// public static final double LONGITUDE = 12.02154; //of Bienenstand
	public static final double LATITUDE = 49.03129100;
	public static final double LONGITUDE = 11.9780100;
	
	public static final float PROXIMITY_RADIUS = 200f;

	private static final long TIME_BW_UPDATES = 1000 * 60 * 5;
	private static final long TIME_BEFORE_FIRST_UPDATE = 1000 * 5;
	
	
	private ReachedReceiver receiver;
	
	private ArrayList<PresenceStatusItem> statusList;
	
	private PresenceListAdapter statusListAdapter;
	
	private LocationDatabase db;
	
	private SocketIOHelper socketIOHelper;
	
	private LocationManager locationManager;
	
	private UserIdentification userIdentification;
	
	private PendingIntent proximityIntent;

	
	public PresenceStatusActivity(SocketIOHelper socketIOHelper) {
		this.socketIOHelper = socketIOHelper;
		setListener();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_presence_status,
				container, false);

		initDatabase();
		setUserIdentification();
		initList();
		initListAdapter(rootView);
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupLocationManager();
	}

	void setupLocationManager() {
		initReceiver();
		initLocationManager();
		registerLocationManager();
		initProximityIntent();
		initProxmityAlert();
		setSingleUpdateRequest();
		initLocationUpdateTimer();
	}

	void initLocationUpdateTimer() {
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setSingleUpdateRequest();
				handler.postDelayed(this, TIME_BW_UPDATES);
			}
		};

		handler.postDelayed(runnable, TIME_BW_UPDATES);
	}

	void setListener() {
		socketIOHelper.setUserStatusReceivedListener(this);
	}

	void initDatabase() {
		db = new LocationDatabase(getActivity());
	}

	void setUserIdentification() {
		db.open();
		userIdentification = db.getUserIdentification();
		db.close();
	}

	void initReceiver() {
		receiver = new ReachedReceiver(this);
	}

	void initProximityIntent() {
		proximityIntent = PendingIntent.getBroadcast(getActivity(), -1,
				getInRadiusIntent(), 0);
	}

	private Intent getInRadiusIntent() {
		Intent inRadiusIntent = new Intent();
		inRadiusIntent.setAction(ACTION_FILTER);
		return inRadiusIntent;
	}

	void initLocationManager() {
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
	}

	void registerLocationManager() {
		getActivity().registerReceiver(receiver,
				new IntentFilter(ACTION_FILTER));
	}

	void initProxmityAlert() {
		locationManager.addProximityAlert(LATITUDE, LONGITUDE,
				PROXIMITY_RADIUS, -1, proximityIntent);
	}

	void setSingleUpdateRequest() {
		locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,
				proximityIntent);
	}

	void initList() {
		statusList = new ArrayList<PresenceStatusItem>();
	}

	void initListAdapter(View rootView) {
		ListView list = (ListView) rootView.findViewById(R.id.status_list);
		statusListAdapter = new PresenceListAdapter(getActivity(), statusList);
		list.setAdapter(statusListAdapter);
	}

	public void sendStatus(boolean status) {
		String username = userIdentification.getUsername();
		String id = userIdentification.getAndroidId();
		if (socketIOHelper.socketConnected()) {
			socketIOHelper.sendPresenceStatus(new PresenceStatusItem(username,
					id, status));
		}
	}

	void updateUIThere(String username, String id) {
		statusList.add(new PresenceStatusItem(username, id));
	}

	void updateUINotThere(String id) {
		for (int i = 0; i < statusList.size(); i++) {
			if (statusList.get(i).getId().equals(id)) {
				statusList.remove(i);
				break;
			}
		}
	}

	void notifyListChanges() {
		statusListAdapter.notifyDataSetChanged();
	}

	void updatePresenceStatusView(JSONObject object) {
		try {
			if (!object.get(PresenceStatusItem.JSON_ID).equals(
					userIdentification.getAndroidId())) {
				if (object.getBoolean(PresenceStatusItem.JSON_STATUS)) {
					updateUIThere(
							object.getString(PresenceStatusItem.JSON_USERNAME),
							object.getString(PresenceStatusItem.JSON_ID));
				} else {
					updateUINotThere(object
							.getString(PresenceStatusItem.JSON_ID));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void requestCurrentStatuses() {
		socketIOHelper.getCurrentStatuses();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}

	@Override
	public void onUserStatusReceived(JSONObject object) {
		// TODO Auto-generated method stub
		updatePresenceStatusView(object);
		getActivity().runOnUiThread(this);
	}

	@Override
	public void onSocketIOConnected() {
		// TODO Auto-generated method stub
		if (socketIOHelper.socketConnected()) {
			requestCurrentStatuses();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		notifyListChanges();
	}
}