package com.slieer.ejbpro.ifc;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;


public interface CatalogLocalIfc {

	@WebMethod
	@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
	public @WebResult(name = "book", targetNamespace = "http://ns.soacookbook.com/catalog")
	abstract Book getBook(@WebParam(name = "isbn", targetNamespace = "http://ns.soacookbook.com/catalog") String isbn);
	// ...

}