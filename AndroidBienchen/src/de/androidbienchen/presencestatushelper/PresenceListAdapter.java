package de.androidbienchen.presencestatushelper;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.androidbienchen.R;



public class PresenceListAdapter extends ArrayAdapter<PresenceStatusItem>{
	
	private List<PresenceStatusItem> presenceList;
	private Context context;

	public PresenceListAdapter(Context context, List<PresenceStatusItem> chatList) {
		super(context, R.id.status_list, chatList);

		this.context = context;
		this.presenceList = chatList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.listitem_person, null);

		}

		PresenceStatusItem presenceItem = presenceList.get(position);

		if (presenceItem != null) {
			TextView username = (TextView) v.findViewById(R.id.present_user);
			TextView status = (TextView) v.findViewById(R.id.presence_status);
			ImageView statusImage = (ImageView) v.findViewById(R.id.status_image);
			
			username.setText(presenceItem.getUsername());
			status.setText(R.string.online);
			statusImage.setImageResource(R.drawable.calendar_next_arrow);
			
		}

		return v;
	}
}
