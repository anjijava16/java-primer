package com.slieer.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2011-12-08T16:24:30.023+08:00
 * Generated source version: 2.5.0
 * 
 */
@WebService(targetNamespace = "http://webservice.ejb.ejbpro.slieer.com/", name = "HelloWebService")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface HelloWebService {

    @WebResult(name = "return", targetNamespace = "http://webservice.ejb.ejbpro.slieer.com/", partName = "return")
    @WebMethod
    public java.lang.String sayHello(
        @WebParam(partName = "arg0", name = "arg0")
        java.lang.String arg0
    );
}
