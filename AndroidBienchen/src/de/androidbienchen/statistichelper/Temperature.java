package de.androidbienchen.statistichelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.net.ParseException;

public class Temperature {

	private int id;
	private float value;
	private String measureDateString;
	
	public Temperature(float value, String measureDate){
		this.value = value;	
		measureDateString = measureDate;
	}
	
	public Temperature(float value, int id, String measureDate){
		this.value = value;	
		this.id = id;
		measureDateString = measureDate;
	}
	
	public int getId(){
		return id;
	}
	
	public float getTemperatureValue(){
		return value;
	}
	
	public String getMeasureDate(){
		return measureDateString;
	}
	
	@SuppressLint("SimpleDateFormat") public Date formatDate() throws java.text.ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date date;
		try {
			date = format.parse(this.measureDateString);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
}
