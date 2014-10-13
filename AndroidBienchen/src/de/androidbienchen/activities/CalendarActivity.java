package de.androidbienchen.activities;

import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import de.androidbienchen.R;
import de.androidbienchen.calendarhelper.Event;
import de.androidbienchen.calendarhelper.EventInsertDialog;
import de.androidbienchen.calendarhelper.EventViewDialog;
import de.androidbienchen.calendarhelper.EventViewDialog.OnDeleteListener;
import de.androidbienchen.data.EventDatabase;
import de.androidbienchen.data.NetworkAvailability;
import de.androidbienchen.data.EventDatabase.SyncListener;
import de.androidbienchen.data.UpdateDialogHelper;

public class CalendarActivity extends Fragment implements SyncListener,
		OnDeleteListener {

	private EventDatabase dbb;
	private CaldroidFragment caldroidFragment;
	ArrayList<Event> allEvents = new ArrayList<Event>();

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

		initCaldroidFragment();
		attachToLibraryActivity();
		initDatabase();
		setCaldroidListener();
		synchToDatabase();
		refreshCalenderView();
	}

	private void attachToLibraryActivity() {
		// Attach to the activity
		FragmentTransaction t = getFragmentManager().beginTransaction();
		t.replace(R.id.calendar, caldroidFragment);
		t.commit();
	}

	private void setCaldroidListener() {
		caldroidFragment.setCaldroidListener(getCaldroidListener());
	}

	private CaldroidListener getCaldroidListener() {
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				if(!checkIfEventExist(date, view)){
					createNewEvent(date);
				}
			}
		};

		return listener;
	}	
		
	private void createNewEvent(Date date){
		new EventInsertDialog(getActivity(),
				new EventInsertDialog.InsertListener() {

					@Override
					public void insertComplt(Event event) {
						if (NetworkAvailability
								.networkStatus(getActivity())) {
							addOnline(event);
							addLocal(event);
							refreshCalenderView();
						} else {
							updateCanceledDialog();
						}
					}
				}, date);
	}
	
	//Shows the Event if it exists
	@SuppressWarnings("deprecation")
	private boolean checkIfEventExist(Date date, View view){
		for (int i = 0; i < allEvents.size(); i++) {
			Date start = allEvents.get(i).StartDate;
			if (start.getDate() == date.getDate()
					&& start.getMonth() == date.getMonth()
					&& start.getYear() == date.getYear()) {
				new EventViewDialog(getActivity(), allEvents.get(i),
						CalendarActivity.this);
				return true;
			}
		}
		return false;
	}

	private void initCaldroidFragment() {
		caldroidFragment = new CaldroidFragment();
	}

	private void initDatabase() {
		dbb = new EventDatabase(getActivity());
	}

	private void synchToDatabase() {
		if (NetworkAvailability.networkStatus(getActivity())) {
			dbb.syncToOnlineDB(this);
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

	// refreshes the calendar if events are added
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

	// deletes an event out of online and local database
	private void deleteEvent(final Event event) {
		dbb.deleteEvent(event, new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject pquery, ParseException e) {

				if (pquery != null) {
					pquery.deleteInBackground();
				}
				dbb.deleteLocal(event);
				caldroidFragment.setBackgroundResourceForDate(
						R.color.caldroid_white, event.StartDate);
				onDatabaseUpdated();
			}
		});
	}

	private void updateCanceledDialog() {
		UpdateDialogHelper.UpdateCanceledDialog(getActivity(), getActivity()
				.getResources().getString(R.string.no_network_error_message),
				"");
	}

	@Override
	public void onDeleteEvent(Event event) {
		if (NetworkAvailability.networkStatus(getActivity())) {
			deleteEvent(event);
		} else
			updateCanceledDialog();
	}

	// updates the database after deleting
	@Override
	public void onDatabaseUpdated() {
		allEvents.clear();
		allEvents = dbb.getAllEvents();
		caldroidFragment.refreshView();
	}
}