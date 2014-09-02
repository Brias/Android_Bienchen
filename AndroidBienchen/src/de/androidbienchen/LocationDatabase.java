package de.androidbienchen;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LocationDatabase {

	private static LocationDatabaseHelper dbHelper;
	private static SQLiteDatabase db;
	
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
	
	public long insertImage(byte[] image) throws SQLiteException{
	    ContentValues newImageValue = new  ContentValues();
	    
	    newImageValue.put(AppConfig.ImageData.IMAGE_KEY, image);
	    
	    return db.insert( AppConfig.ImageData.TABLE_KEY_IMAGE, null, newImageValue);
	}
	
	public Bitmap getImage(){
		Cursor cursor = db.query(AppConfig.ImageData.TABLE_KEY_IMAGE, new String[] {AppConfig.ImageData.IMAGE_KEY}, null, null, null, null, null);
		byte[] image = null;
		
		if(cursor.moveToFirst()){
			image = cursor.getBlob(0);
		}
		
		Bitmap bitmap = BitmapFactory.decodeByteArray(image , 0, image.length);
		
		return bitmap;
	}
	
	public boolean removeImage(){
		if(getImage() == null){
			return true;
		}else{
			return db.delete(AppConfig.ImageData.TABLE_KEY_IMAGE, null, null) > 0;
		}
	}
	
	public long insertScaleValue(Weight weight){
		ContentValues newScaleValue = new ContentValues();
		
		newScaleValue.put(AppConfig.SizeData.SCALE_KEY, weight.getScaleValue());
		newScaleValue.put(AppConfig.Data.DATE_KEY, weight.getMeasureDate());
		
		return db.insert(AppConfig.SizeData.TABLE_KEY_SCALE, null, newScaleValue);
	}
	
	public long insertTemperatureValue(Temperature temperature){
		ContentValues newTemperatureValue = new ContentValues();
		
		newTemperatureValue.put(AppConfig.TemperatureData.TEMPERTURE_KEY, temperature.getTemperatureValue());
		newTemperatureValue.put(AppConfig.Data.DATE_KEY, temperature.getMeasureDate());
		
		return db.insert(AppConfig.TemperatureData.TABLE_KEY_TEMPERATURE, null, newTemperatureValue);
	}
	
	public ArrayList<Weight> getWeights(){
		ArrayList<Weight> weights = new ArrayList<Weight>();
		Cursor cursor =  db.query(AppConfig.SizeData.TABLE_KEY_SCALE, new String[] {AppConfig.Data.ID_KEY, AppConfig.SizeData.SCALE_KEY, AppConfig.Data.DATE_KEY}, null, null, null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				int id = cursor.getInt(0);
				float value = cursor.getFloat(1);
				String date = cursor.getString(2);
				
				Weight weight = new Weight(value, id, date);
				weights.add(weight);
			}while(cursor.moveToNext());
		}
		
		return weights;
	}
	
	public ArrayList<Temperature> getTemperatures(){
		ArrayList<Temperature> temperatures = new ArrayList<Temperature>();
		Cursor cursor =  db.query(AppConfig.TemperatureData.TABLE_KEY_TEMPERATURE, new String[] {AppConfig.Data.ID_KEY, AppConfig.TemperatureData.TEMPERTURE_KEY, AppConfig.Data.DATE_KEY}, null, null, null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				int id = cursor.getInt(0);
				float value = cursor.getFloat(1);
				String date = cursor.getString(2);
				
				Temperature temperature = new Temperature(value, id, date);
				temperatures.add(temperature);
			}while(cursor.moveToNext());
		}
		
		return temperatures;
	}
	
	public boolean removeAllScaleValues(){
		if(getWeights().size() == 0){
			return true;
		}else{
			return db.delete(AppConfig.SizeData.TABLE_KEY_SCALE, null, null) > 0;
		}
	}
	
	public boolean removeAllTemperatureValues(){
		if(getTemperatures().size() == 0){
			return true;
		}else{
			return db.delete(AppConfig.TemperatureData.TABLE_KEY_TEMPERATURE, null, null) > 0;
		}
	}
	
	/*public boolean removeOldestWeight(){
		int oldestId = getOldestWeightId();
		
		return db.delete(AppConfig.SizeData.TABLE_KEY_SCALE, ""+AppConfig.Data.ID_KEY + " = " + oldestId+"", null) > 0;
	}
	
	private int getOldestWeightId(){
		final SQLiteStatement stmt = db.compileStatement("SELECT MIN(" +AppConfig.Data.ID_KEY+ ") FROM " +AppConfig.SizeData.TABLE_KEY_SCALE);

	    return (int) stmt.simpleQueryForLong();
	}*/
	
	private class LocationDatabaseHelper extends SQLiteOpenHelper{
		
		private static final String DATABASE_CREATE_SCALE_TABLE = "create table "
				+ AppConfig.SizeData.TABLE_KEY_SCALE + " ("+ AppConfig.Data.ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ AppConfig.SizeData.SCALE_KEY + " REAL NOT NULL, "
				+ AppConfig.Data.DATE_KEY + " TEXT NOT NULL);";
		
		private static final String DATABASE_CREATE_TEMPERATURE_TABLE = "create table "
				+ AppConfig.TemperatureData.TABLE_KEY_TEMPERATURE + " ("+ AppConfig.Data.ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ AppConfig.TemperatureData.TEMPERTURE_KEY + " REAL NOT NULL, "
				+ AppConfig.Data.DATE_KEY + " TEXT NOT NULL);";
		
		private static final String DATABASE_CREATE_IMAGE_TABLE = "create table "
				+ AppConfig.ImageData.TABLE_KEY_IMAGE + " ("
				+ AppConfig.Data.ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ AppConfig.ImageData.IMAGE_KEY + " BLOB);";
		
		public LocationDatabaseHelper(Context context, String name, CursorFactory factory, int version){
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_SCALE_TABLE);
			db.execSQL(DATABASE_CREATE_TEMPERATURE_TABLE);
			db.execSQL(DATABASE_CREATE_IMAGE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}	
	}
}
