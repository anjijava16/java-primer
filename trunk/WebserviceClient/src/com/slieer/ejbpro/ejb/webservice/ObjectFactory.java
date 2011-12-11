
package com.slieer.ejbpro.ejb.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.slieer.ejbpro.ejb.webservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CallCarResponse_QNAME = new QName("http://webservice.ejb.ejbpro.slieer.com/", "callCarResponse");
    private final static QName _CallCar_QNAME = new QName("http://webservice.ejb.ejbpro.slieer.com/", "callCar");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.slieer.ejbpro.ejb.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CallCar }
     * 
     */
    public CallCar createCallCar() {
        return new CallCar();
    }

    /**
     * Create an instance of {@link Car }
     * 
     */
    public Car createCar() {
        return new Car();
    }

    /**
     * Create an instance of {@link CallCarResponse }
     * 
     */
    public CallCarResponse createCallCarResponse() {
        return new CallCarResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CallCarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.ejb.ejbpro.slieer.com/", name = "callCarResponse")
    public JAXBElement<CallCarResponse> createCallCarResponse(CallCarResponse value) {
        return new JAXBElement<CallCarResponse>(_CallCarResponse_QNAME, CallCarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CallCar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.ejb.ejbpro.slieer.com/", name = "callCar")
    public JAXBElement<CallCar> createCallCar(CallCar value) {
        return new JAXBElement<CallCar>(_CallCar_QNAME, CallCar.class, null, value);
    }

}
