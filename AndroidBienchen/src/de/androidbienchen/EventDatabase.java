package de.androidbienchen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class EventDatabase {
	private dbOpenHelper dbH;

	public EventDatabase(Context context) {
		this.context = context;
		dbH = new dbOpenHelper(context, DATABASE_KEY, null, DATABASE_VERSION);
		Parse.initialize(this.context, "4RP3AlbWRKmrrhi542rrP3jFuleZN9TSgwAek8N0",
				"FjLHxQAqH7gcZzoBvFiTvtntMRHeW36LbzGrg1A9");
	}

	private SQLiteDatabase database;

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_KEY = "bienendatabase";
	public static final String TABLE_KEY = "events";
	public static final String ID_KEY = "id";
	public static final String TITLE_KEY = "title";
	public static final String INFO_KEY = "infotext";
	public static final String START_DATE = "start";
	public static final String END_DATE = "end";
	public static final String PARSE_KEY = "parse_key";

	private Context context;

	private class dbOpenHelper extends SQLiteOpenHelper {
		private static final String DATABASE_CREATE = "create table "
				+ TABLE_KEY + " (" 
				+ ID_KEY + " integer primary key autoincrement, " 
				+ TITLE_KEY + " text not null, " 
				+ START_DATE + " long not null, "
				+ END_DATE + " long not null, " + PARSE_KEY + " text, "
				+ INFO_KEY + " text not null );";

		public dbOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}
	}

	public void openDB() {
		database = dbH.getWritableDatabase();
	}

	public ArrayList<Event> getAllEvents() {
		ArrayList<Event> events = new ArrayList<Event>();
		openDB();
		Cursor cursor = database.query(TABLE_KEY, null, null, null, null, null,
				null);

		if (cursor.moveToFirst()) {
			do {
				Event event = new Event();
				event.Titel = cursor.getString(1);
				Date startDate = new Date(cursor.getLong(2));
				Date endDate = new Date(cursor.getLong(3));
				event.StartDate = startDate;
				Log.e("MARK", startDate.toString());
				event.EndDate = endDate;
				event.Info = cursor.getString(5);
				event.parseId = cursor.getString(4);
				events.add(event);

			} while (cursor.moveToNext());

		}
		database.close();
		return events;
	}

	public void addEvent(String title, String infotext, Date start, Date end) {
		ContentValues data = new ContentValues();
		data.put(TITLE_KEY, title);
		data.put(INFO_KEY, infotext);
		data.put(START_DATE, start.getTime());
		data.put(END_DATE, end.getTime());

		openDB();
		final long id = database.insert(TABLE_KEY, null, data); // Returns key
		database.close();

		final ParseObject parseData = new ParseObject(TABLE_KEY);
		parseData.put(TITLE_KEY, title);
		parseData.put(INFO_KEY, infotext);
		parseData.put(START_DATE, start.getTime());
		parseData.put(END_DATE, end.getTime());
		parseData.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				ContentValues cid = new ContentValues();
				cid.put(PARSE_KEY, parseData.getObjectId());
				openDB();
				database.update(TABLE_KEY, cid,
						ID_KEY + "=" + String.valueOf(id), null);
				database.close();
			}


		});
	}

	public void syncToOnlineDB(final SyncListener sl) {
		ParseQuery<ParseObject> pquery = new ParseQuery<ParseObject>(TABLE_KEY);

		pquery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				ArrayList<Event> localEvents = getAllEvents();
				for (int i = 0; i < objects.size(); i++) {
					ParseObject curObject = objects.get(i);
					boolean entryfound = false;
					for (int j = 0; j < localEvents.size(); j++) {
						String localParseId = localEvents.get(j).parseId;

						if (localParseId == null) { // catch, maybe other
													// solution
							continue;
						}
						String onlineParseId = curObject.getObjectId();
						if (localParseId.equals(onlineParseId)) { // Hier
																	// Vergleich
							entryfound = true;
						}
					}
					if (entryfound == false) {

						ContentValues cvs = new ContentValues();

						cvs.put(TITLE_KEY, curObject.getString(TITLE_KEY));
						cvs.put(INFO_KEY, curObject.getString(INFO_KEY));
						cvs.put(START_DATE, curObject.getLong(START_DATE));
						cvs.put(END_DATE, curObject.getLong(END_DATE));
						cvs.put(PARSE_KEY, curObject.getObjectId());
						openDB();
						database.insert(TABLE_KEY, null, cvs);
						database.close();
						
					}
					if (sl != null){
						sl.syncFinished();
					}
				}

			}

		});
	}
	
	
	public interface SyncListener{
		public void syncFinished();
		
		
		
	}
}
