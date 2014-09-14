package de.androidbienchen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import de.androidbienchen.EventDatabase.SyncListener;

public class CalendarActivity extends Fragment implements SyncListener {

	private EventDatabase dbb;
	private CaldroidFragment caldroidFragment;
	ArrayList<Event> allEvents = new ArrayList<Event>();

	/*public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.calendar, menu);
		return super.onCreateOptionsMenu(menu);
	}*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		test(savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_scale,
				container, false);
		return rootView;
	}
	
	private void test(Bundle savedInstanceState){

		//
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

			// Uncomment this to customize startDayOfWeek
			// args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
			// CaldroidFragment.TUESDAY); // Tuesday
			caldroidFragment.setArguments(args);
		}

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_refresh) {
			dbb.syncToOnlineDB(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onStart(){
		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		caldroidFragment = new CaldroidFragment();

		// // Attach to the activity
		android.support.v4.app.FragmentTransaction t = getFragmentManager().beginTransaction();
		t.replace(R.id.calendar, caldroidFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				Toast.makeText(getActivity(), formatter.format(date),
						Toast.LENGTH_SHORT).show();
				for (int i = 0; i < allEvents.size(); i++) {
					Date start = allEvents.get(i).StartDate;
					if (start.getDate() == date.getDate()
							&& start.getMonth() == date.getMonth()
							&& start.getYear() == date.getYear()) {
						new EventViewDialog(getActivity(), allEvents.get(i));
						return;
					}
				}

				new EventInsertDialog(getActivity(),
						new EventInsertDialog.InsertListener() {

							@Override
							public void insertComplt(Event event) {
								dbb.addEvent(event.Titel, event.Info,
										event.StartDate, event.EndDate);
								refreshCalenderView();
							}
						}, date);
				
			}

			@Override
			public void onChangeMonth(int month, int year) {
				String text = "month: " + month + " year: " + year;
				Toast.makeText(getActivity(), text,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCaldroidViewCreated() {
				if (caldroidFragment.getLeftArrowButton() != null) {
					Toast.makeText(getActivity(),
							"Caldroid view is created", Toast.LENGTH_SHORT)
							.show();
				}
			}

		};

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);

		dbb = new EventDatabase(getActivity());
		dbb.syncToOnlineDB(this);

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
		Toast.makeText(getActivity(), "Sync finished", Toast.LENGTH_LONG).show();

		refreshCalenderView();
	}

	private void refreshCalenderView() {
		allEvents = dbb.getAllEvents();
		for (int i = 0; i < allEvents.size(); i++) {
			Event event = allEvents.get(i);

			caldroidFragment.setBackgroundResourceForDate(
					R.color.caldroid_sky_blue, event.StartDate);

			caldroidFragment.refreshView();
		}
	}

}