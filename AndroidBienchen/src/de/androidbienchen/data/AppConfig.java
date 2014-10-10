package de.androidbienchen.data;

public class AppConfig {
	
	public class server{
		public static final String SCALE_URL = "http://app.iv-sinzing.de/waage";
		public static final String TEMPERATURE_URL = "http://app.iv-sinzing.de/temperatur";
		public static final String IMAGE_URL = "http://app.iv-sinzing.de/snapshot.jpg";
		public static final String WEBSITE_URL = "http://www.iv-sinzing.de/";
		public static final String CHAT_URL = "http://132.199.139.24:9425/"; 
	}
	
	public class UsernameData{
		public static final String TABLE_KEY_USERNAME = "_username";
		public static final String USERNAME_KEY = "_usernameValue";
		public static final String ANDROID_ID_KEY ="_androidId";
	}
	
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
