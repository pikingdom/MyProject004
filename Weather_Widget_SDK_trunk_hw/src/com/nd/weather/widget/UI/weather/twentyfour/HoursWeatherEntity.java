package com.nd.weather.widget.UI.weather.twentyfour;

import java.util.List;

public class HoursWeatherEntity {

	public String title;
	public int gmt;
	public TemperatureRange temp;
	public List<HourWecther> items;
	
	public static class TemperatureRange{
		public int height;
		public int low;
	}
	
	public static class HourWecther{
		
		public String name;
		public int type;
		public int temp;
		public int climate;
		public String time;
		public int dayType;
	}
}
