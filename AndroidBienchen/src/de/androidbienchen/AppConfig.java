package de.androidbienchen;

public class AppConfig {
	
	public class SizeData{
		public static final String TABLE_KEY_SCALE = "_scale";
		public static final String SCALE_KEY = "_scaleValue";
	}
	
	public class Data{
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_KEY = "BienchenData";
		public static final String ID_KEY = "_id";
		public static final String DATE_KEY = "_date";
	} 
	
	public class TemperatureData{
		public static final String TABLE_KEY_TEMPERATURE = "_temperatureTable";
		public static final String TEMPERTURE_KEY = "_temperature";
	}
	
	public class ImageData{
		public static final String TABLE_KEY_IMAGE = "_imageTable";
		public static final String IMAGE_KEY = "_image";
	}
}
