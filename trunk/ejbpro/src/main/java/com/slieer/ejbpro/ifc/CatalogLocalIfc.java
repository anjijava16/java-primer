package com.slieer.ejbpro.ifc;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;

import org.apache.log4j.Logger;


public interface CatalogLocalIfc {
	public static final Logger LOGGER = Logger.getLogger(CatalogLocalIfc.class);

	@WebMethod
	@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, 
			use = SOAPBinding.Use.LITERAL, 
			parameterStyle = SOAPBinding.ParameterStyle.BARE)
	@WebResult(name = "book", targetNamespace = "http://ns.soacookbook.com/catalog")
	public abstract	Book getBook(@WebParam(name = "isbn", 
			 targetNamespace = "http://ns.soacookbook.com/catalog") String isbn);
	// ...
	 @WebMethod
   @WebResult(name="title")
   public String getTitle(
       @WebParam(name="id") String id);


}