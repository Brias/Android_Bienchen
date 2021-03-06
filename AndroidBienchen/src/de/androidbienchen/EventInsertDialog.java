package de.androidbienchen;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EventInsertDialog {

	private Context cont;
	private Date clickedDate;

	public EventInsertDialog(Context cont, InsertListener ln, Date clickedDate) {
		this.cont = cont;
		this.ln = ln;
		this.clickedDate = clickedDate;
		insertDialog();
	}

	public interface InsertListener {
		public void insertComplt(Event event);
	}

	private InsertListener ln;

	public void insertDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(cont);

		LayoutInflater inflater = (LayoutInflater) cont
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.eventinsert_dialog, null);

		final EditText insertTitel = (EditText) v
				.findViewById(R.id.eventinsert_titel);
		final EditText insertInfo = (EditText) v
				.findViewById(R.id.eventinsert_info);

		final TextView startZeit = (TextView) v.findViewById(R.id.startZeit);
		final TextView endZeit = (TextView) v.findViewById(R.id.endZeit);
		final Button startButton = (Button) v.findViewById(R.id.startButton);
		final Button endButton = (Button) v.findViewById(R.id.endButton);

		final Date startDate = new Date(clickedDate.getTime());
		final Date endDate = new Date(clickedDate.getTime() + 1000*60*30);
		startZeit.setText(startDate.toString());
		endZeit.setText(endDate.toString());

		startButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentActivity fa = (FragmentActivity) cont;
				new TimePickerFragment(startDate, startZeit).show(
						fa.getSupportFragmentManager(), "datePicker");

			}
		});

		endButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentActivity fa = (FragmentActivity) cont;
				new TimePickerFragment(endDate, endZeit).show(
						fa.getSupportFragmentManager(), "datePicker");

			}
		});

		builder.setView(v);
		builder.setNegativeButton("Abbrechen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();

			}
		});

		builder.setPositiveButton("Hinzufügen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				String inserTt = insertTitel.getText().toString();
				String insertInf = insertInfo.getText().toString();
				Event event = new Event();
				event.Titel = inserTt;
				event.Info = insertInf;
				event.StartDate = startDate;
				event.EndDate = endDate;
				ln.insertComplt(event);
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		private Date date;
		private TextView outPutText;

		public TimePickerFragment(Date date, TextView outPutText) {
			this.date = date;
			this.outPutText = outPutText;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			date.setHours(hourOfDay);
			date.setMinutes(minute);
			outPutText.setText(date.toString());
		}
		
		private void copyDate(Date source, Date dest){
			dest.setMonth(source.getMonth());
			dest.setDate(source.getDate());
			dest.setYear(source.getYear());
			
		}
	}
}