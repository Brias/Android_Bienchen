package de.androidbienchen;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

public class EventInsertDialog extends DialogFragment implements
		 TimePickerDialog.OnTimeSetListener {

	public static final String DATEPICKER_TAG = "datepicker";
	public static final String TIMEPICKER_TAG = "timepicker";

	final Calendar calendar = Calendar.getInstance();
	final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY),
			calendar.get(Calendar.MINUTE), false, false);
	
	private Context cont;
	private Event event;

	public EventInsertDialog(Event vt, Context cont) {
		this.cont = cont;
		event = vt;
		insertDialog();
	}

	public void insertDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(cont);

		LayoutInflater inflater = (LayoutInflater) cont
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.eventinsert_dialog, null);

		final EditText insertTitel = (EditText) v
				.findViewById(R.id.eventinsert_titel);
		final EditText insertInfo = (EditText) v.findViewById(R.id.eventinsert_info);


//		date.setText(event.StartDate.toString() + " bis "
//				+ event.EndDate.toString());

		builder.setView(v);
		builder.setPositiveButton("Schließen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String inserTt = insertTitel.getText().toString();
				String insertInf = insertInfo.getText().toString();
				
				onTimeSet(timePickerDialog, which, which);

				dialog.dismiss();

			}
		});
		builder.create().show();

	}

	public void onTimeSet(TimePickerDialog timePickerDialog2, int hourOfDay, int minute) {
		Toast.makeText(cont, "new time:" + hourOfDay + "-" + minute,
				Toast.LENGTH_LONG).show();
	}

	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		
	}


}
