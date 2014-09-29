package de.androidbienchen;

import java.text.SimpleDateFormat;

import de.androidbienchen.EventInsertDialog.InsertListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class EventViewDialog {

	private Context cont;
	private Event event;
	private InsertListener dl;

	// public EventViewDialog(Context cont, Event vt, InsertListener dl)
	public EventViewDialog(Context cont, Event vt) {
		this.cont = cont;
		this.event = vt;
		// this.dl = dl;
		createDialog();
	}

	public interface InsertListener {
		public void deletEvent(Event event);
	}

	public SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm ");

	private void createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		Log.e("AYLA", this.event.toString());
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

		builder.setPositiveButton("Schließen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.create().show();

		// builder.setPositiveButton("Löschen", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dl.deletEvent(event);
		// dialog.dismiss();
		// }
		// });
		// builder.create().show();
		// }

	}
}
