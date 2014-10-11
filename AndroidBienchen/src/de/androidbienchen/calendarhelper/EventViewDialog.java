package de.androidbienchen.calendarhelper;

import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import de.androidbienchen.R;

public class EventViewDialog {

	private Context cont;
	private Event event;
	private OnDeleteListener dl;

	public EventViewDialog(Context cont, Event vt,
			OnDeleteListener dl) {
		this.cont = cont;
		this.event = vt;
		this.dl = dl;
		createDialog();
	}

	public interface OnDeleteListener {
		public void onDeleteEvent(Event event);
	}

	public SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm ");

	// method to delete or close an event 
	
	private void createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setTitle(this.event.Titel);
		LayoutInflater inflater = (LayoutInflater) cont
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.event_dialog, null);

		TextView info = (TextView) v.findViewById(R.id.eventdialog_info);
		TextView date = (TextView) v.findViewById(R.id.eventdialog_time);

		info.setText(event.Info);
		date.setText(formatter.format(event.StartDate) + " - "
				+ formatter.format(event.EndDate));

		// sets the "Löschen" button which deletes the event
		builder.setView(v);
		builder.setNegativeButton("Löschen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dl.onDeleteEvent(event);
				dialog.dismiss();
			}
		});
		
		// sets the "Schließen" button, which closes the dialog
		builder.setPositiveButton("Schließen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.create().show();

	}

}