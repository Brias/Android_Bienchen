package de.androidbienchen;

import java.text.SimpleDateFormat;

import de.androidbienchen.EventInsertDialog.InsertListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class EventViewDialog {

	private Context cont;
	private Event event;
	private OnDeleteListener dl;
	private EventDatabase dbb;


	 public EventViewDialog(Context cont, Event vt, EventDatabase dbb, OnDeleteListener dl){
		this.cont = cont;
		this.event = vt;
		this.dl = dl;
		this.dbb = dbb;
		createDialog();
	}

	public interface OnDeleteListener {
		public void onDeleteEvent(Event event);
	}

	public SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm ");

	private void createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
//		Log.e("AYLA", this.event.toString());
		builder.setTitle(this.event.Titel);
		LayoutInflater inflater = (LayoutInflater) cont
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.event_dialog, null);

		TextView info = (TextView) v.findViewById(R.id.eventdialog_info);
		TextView date = (TextView) v.findViewById(R.id.eventdialog_time);

		info.setText(event.Info);
		date.setText(formatter.format(event.StartDate) + " - "
				+ formatter.format(event.EndDate));

		builder.setView(v);
		builder.setNegativeButton("Löschen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dl.onDeleteEvent(event);
				dialog.dismiss();
			}
		});

		builder.setPositiveButton("Schließen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.create().show();

	}

}
