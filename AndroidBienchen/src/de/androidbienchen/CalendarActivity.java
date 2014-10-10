package de.androidbienchen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import de.androidbienchen.EventDatabase.SyncListener;
import de.androidbienchen.EventViewDialog.OnDeleteListener;

public class CalendarActivity extends Fragment implements SyncListener, OnDeleteListener {

	private EventDatabase dbb;
	private CaldroidFragment caldroidFragment;
	ArrayList<Event> allEvents = new ArrayList<Event>();

	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.calendar, menu);
	// return super.onCreateOptionsMenu(menu);
	// }

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_refresh) {
			dbb.syncToOnlineDB(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_calendar, container,
				false);
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		caldroidFragment = new CaldroidFragment();

		// If Activity is created after rotation

		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

			caldroidFragment.setArguments(args);
		}

		// // Attach to the activity
		FragmentTransaction t = getFragmentManager().beginTransaction();
		t.replace(R.id.calendar, caldroidFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				Log.d("DELETE", "allEvents.size() in onSelectDate = "+allEvents.size());
				for (int i = 0; i < allEvents.size(); i++) {
					Date start = allEvents.get(i).StartDate;
					if (start.getDate() == date.getDate()
							&& start.getMonth() == date.getMonth()
							&& start.getYear() == date.getYear()) {
						new EventViewDialog(getActivity(), allEvents.get(i),
								dbb, CalendarActivity.this);
						return;
					}
				}

				new EventInsertDialog(getActivity(),
						new EventInsertDialog.InsertListener() {

							@Override
							public void insertComplt(Event event) {
								// if Internetverbindung vorhanden
//								if (NetworkAvailability
//										.networkStatus(getActivity())) {
									addOnline(event);
									addLocal(event);
									refreshCalenderView();
//									
//								} else {
//									// if keine Internetverbindung
//
//									addLocal(event);
//									refreshCalenderView();
//								}
							}
						}, date);
			}

		};

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);
		dbb = new EventDatabase(getActivity());
		dbb.syncToOnlineDB(this);

		refreshCalenderView();
	}

	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}
	}

	@Override
	public void syncFinished() {
		refreshCalenderView();
	}

	private void addLocal(Event event) {
		dbb.addEventLocal(event.Titel, event.Info, event.StartDate,
				event.EndDate);
	}

	private void addOnline(Event event) {
		dbb.addEventOnline(event.Titel, event.Info, event.StartDate,
				event.EndDate, this);
	}

	private void refreshCalenderView() {
		allEvents.clear();
		allEvents = dbb.getAllEvents();
		
		for (int i = 0; i < allEvents.size(); i++) {
			Event event = allEvents.get(i);
			
			caldroidFragment.setBackgroundResourceForDate(R.color.yellow,
					event.StartDate);
		}

		caldroidFragment.refreshView();
	}

	@Override
	public void onDeleteEvent(final Event event) {
		dbb.deleteEvent(event, new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject pquery, ParseException e) {
//				if (pquery == null)
//					Log.d("SCHEISSE", "SCHEISSE");
				
				if (pquery != null) {
					pquery.deleteInBackground();									
 				}
				
				dbb.deleteLocal(event);
				
				Log.d("DELETE", "allEvents.size() in onDeleteEvent = "+allEvents.size());
				caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, event.StartDate);
				onDatabaseUpdated();
			}
		});
		
//		refreshCalenderView();
	}

	@Override
	public void onDatabaseUpdated() {
		allEvents.clear();
		allEvents = dbb.getAllEvents();
		caldroidFragment.refreshView();
	}
	
	

}
