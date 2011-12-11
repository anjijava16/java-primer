package net;

import net.webservicex.ArrayOfWeatherData;
import net.webservicex.WeatherData;
import net.webservicex.WeatherForecast;
import net.webservicex.WeatherForecastSoap;
import net.webservicex.WeatherForecasts;

/*
====================================
	wsimport -verbose -d gen -extension 
	-keep http://www.webservicex.net/WeatherForecast.asmx?wsdl    
	    
====================================
	Example: Client Class that Invokes the Weather Forecast Service at
	          WebServiceX.net

	import net.webservicex.*;
	import java.math.*;

	 Calls the forecast service at WebServiceX.net.
	*/
	public class WeatherClient {
	 public static void main(String...arg) {
	    System.out.println("Invoking...");
	    WeatherForecast service = new WeatherForecast();
	    WeatherForecastSoap port = service.getWeatherForecastSoap();

	    //Invoke Service and Get Result
	    WeatherForecasts forecasts = port.getWeatherByZipCode("85255");

	    //Use the generated objects in the result
	    String placeName = forecasts.getPlaceName();

	    ArrayOfWeatherData arr = forecasts.getDetails();
	    WeatherData data = arr.getWeatherData().get(0);

	    System.out.println("Place=" + placeName);
	    System.out.println("Day=" + data.getDay());
	    System.out.println("High Temp (F)=" + data.getMaxTemperatureF());
	 
	    System.out.println("All done.");
	 }
	}
