
package net.webservicex;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "WeatherForecastSoap", targetNamespace = "http://www.webservicex.net")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface WeatherForecastSoap {


    /**
     * Get one week weather forecast for a valid Zip Code(USA)
     * 
     * @param zipCode
     * @return
     *     returns net.webservicex.WeatherForecasts
     */
    @WebMethod(operationName = "GetWeatherByZipCode", action = "http://www.webservicex.net/GetWeatherByZipCode")
    @WebResult(name = "GetWeatherByZipCodeResult", targetNamespace = "http://www.webservicex.net")
    @RequestWrapper(localName = "GetWeatherByZipCode", targetNamespace = "http://www.webservicex.net", className = "net.webservicex.GetWeatherByZipCode")
    @ResponseWrapper(localName = "GetWeatherByZipCodeResponse", targetNamespace = "http://www.webservicex.net", className = "net.webservicex.GetWeatherByZipCodeResponse")
    public WeatherForecasts getWeatherByZipCode(
        @WebParam(name = "ZipCode", targetNamespace = "http://www.webservicex.net")
        String zipCode);

    /**
     * Get one week  weather forecast for a place name(USA)
     * 
     * @param placeName
     * @return
     *     returns net.webservicex.WeatherForecasts
     */
    @WebMethod(operationName = "GetWeatherByPlaceName", action = "http://www.webservicex.net/GetWeatherByPlaceName")
    @WebResult(name = "GetWeatherByPlaceNameResult", targetNamespace = "http://www.webservicex.net")
    @RequestWrapper(localName = "GetWeatherByPlaceName", targetNamespace = "http://www.webservicex.net", className = "net.webservicex.GetWeatherByPlaceName")
    @ResponseWrapper(localName = "GetWeatherByPlaceNameResponse", targetNamespace = "http://www.webservicex.net", className = "net.webservicex.GetWeatherByPlaceNameResponse")
    public WeatherForecasts getWeatherByPlaceName(
        @WebParam(name = "PlaceName", targetNamespace = "http://www.webservicex.net")
        String placeName);

}