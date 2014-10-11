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

		caldroidFragment = new CaldroidFragment();

		// Attach to the activity
		FragmentTransaction t = getFragmentManager().beginTransaction();
		t.replace(R.id.calendar, caldroidFragment);
		t.commit();

		// Setup listener, which listen to events and
		// creates new dialogs depending on what was clicked

		final CaldroidListener listener = new CaldroidListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onSelectDate(Date date, View view) {

				// shows a already existing event in the calendar
				for (int i = 0; i < allEvents.size(); i++) {
					Date start = allEvents.get(i).StartDate;
					if (start.getDate() == date.getDate()
							&& start.getMonth() == date.getMonth()
							&& start.getYear() == date.getYear()) {
						new EventViewDialog(getActivity(), allEvents.get(i),
								CalendarActivity.this);
						return;
					}
				}

				// creates a new event in the calendar which is filled
				// with information from EventInsertDialog

				new EventInsertDialog(getActivity(),
						new EventInsertDialog.InsertListener() {

							@Override
							public void insertComplt(Event event) {
								// if Internetverbindung vorhanden

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

		};

		// Setup Caldroid, initializing, synchronization and refreshing of new
		// database

		caldroidFragment.setCaldroidListener(listener);
		dbb = new EventDatabase(getActivity());
		if (NetworkAvailability.networkStatus(getActivity())) {
			dbb.syncToOnlineDB(this);
		}
		refreshCalenderView();
	}

	// Save current states of the caldroid

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
	
	private void deleteEvent(final Event event){
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
	
	private void updateCanceledDialog(){
		UpdateDialogHelper.UpdateCanceledDialog(getActivity(), getActivity().getResources().getString(R.string.no_network_error_message), "");
	}

	// deletes an event out of online and local database
	@Override
	public void onDeleteEvent(Event event) {
		if(NetworkAvailability.networkStatus(getActivity())){
			deleteEvent(event);
		}else updateCanceledDialog();
	}

	// updates the database after deleting

	@Override
	public void onDatabaseUpdated() {
		allEvents.clear();
		allEvents = dbb.getAllEvents();
		caldroidFragment.refreshView();
	}
}