package de.androidbienchen;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.net.ParseException;



public class Weight {
	
	private float value;
	private String measureDateString;
	
	public Weight(float value, String measureDate){
		this.value = value;	
		measureDateString = measureDate;
	}
	
	public float getSizeValue(){
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
