
package net.webservicex;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "WeatherForecastHttpPost", targetNamespace = "http://www.webservicex.net")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface WeatherForecastHttpPost {


    /**
     * Get one week weather forecast for a valid Zip Code(USA)
     * 
     * @param zipCode
     * @return
     *     returns net.webservicex.WeatherForecasts
     */
    @WebMethod(operationName = "GetWeatherByZipCode")
    @WebResult(name = "WeatherForecasts", targetNamespace = "http://www.webservicex.net", partName = "Body")
    public WeatherForecasts getWeatherByZipCode(
        @WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "ZipCode")
        String zipCode);

    /**
     * Get one week  weather forecast for a place name(USA)
     * 
     * @param placeName
     * @return
     *     returns net.webservicex.WeatherForecasts
     */
    @WebMethod(operationName = "GetWeatherByPlaceName")
    @WebResult(name = "WeatherForecasts", targetNamespace = "http://www.webservicex.net", partName = "Body")
    public WeatherForecasts getWeatherByPlaceName(
        @WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "PlaceName")
        String placeName);

}
