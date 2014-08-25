package de.androidbienchen;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationDatabase {

	private LocationDatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public LocationDatabase(Context context){
		dbHelper = new LocationDatabaseHelper(context, AppConfig.Data.DATABASE_KEY, null, AppConfig.Data.DATABASE_VERSION);
	}
	
	public void open(){
		try{
			db = dbHelper.getWritableDatabase();
		}catch(SQLException e){
			db = dbHelper.getReadableDatabase();
		}
	}
	
	public void close(){
		db.close();
	}
	
	
	
	public long insertSizeValue(Weight weight){
		ContentValues newSizeValue = new ContentValues();
		
		newSizeValue.put(AppConfig.SizeData.SCALE_KEY, weight.getSizeValue());
		newSizeValue.put(AppConfig.SizeData.DATE_KEY, weight.getMeasureDate());
		
		return db.insert(AppConfig.SizeData.TABLE_KEY_SCALE, null, newSizeValue);
	}
	
	public ArrayList<Weight> getWeights(){
		ArrayList<Weight> weights = new ArrayList<Weight>();
		Cursor cursor =  db.query(AppConfig.SizeData.TABLE_KEY_SCALE, new String[] {AppConfig.Data.ID_KEY, AppConfig.SizeData.SCALE_KEY, AppConfig.SizeData.DATE_KEY}, null, null, null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				float value = cursor.getFloat(1);
				String date = cursor.getString(2);
				Log.d("getWeights", ""+value+date);
				
				Weight weight = new Weight(value, date);
				weights.add(weight);
			}while(cursor.moveToNext());
		}
		
		return weights;
	}
	
	
	private class LocationDatabaseHelper extends SQLiteOpenHelper{
		
		private static final String DATABASE_CREATE_SIZE_TABLE = "create table "
				+ AppConfig.SizeData.TABLE_KEY_SCALE + " ("+ AppConfig.Data.ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ AppConfig.SizeData.SCALE_KEY + "REAL NOT NULL, "
				+ AppConfig.SizeData.DATE_KEY + "TEXT NOT NULL);";
		
		private static final String DATABASE_CREATE_IMAGE_TABLE = "create table "
				+ AppConfig.ImageData.TABLE_KEY_IMAGE + " ("
				+ AppConfig.Data.ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ AppConfig.ImageData.IMAGE_KEY + "real not null);";
		
		public LocationDatabaseHelper(Context context, String name, CursorFactory factory, int version){
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_SIZE_TABLE);
			//db.execSQL(DATABASE_CREATE_IMAGE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}	
	}
}
