package com.slieer.ejbpro.ejb.webservice;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.log4j.Logger;

import com.slieer.ejbpro.ifc.Author;
import com.slieer.ejbpro.ifc.Book;
import com.slieer.ejbpro.ifc.CatalogLocalIfc;
import com.slieer.ejbpro.ifc.Book.Category;

@WebService(serviceName = "CatalogService", 
		name = "Catalog", 
		targetNamespace = "http://ns.soacookbook.com/ws/catalog")
@Stateless
@Local(CatalogLocalIfc.class)
public class CatalogEJB implements CatalogLocalIfc {
	final static Logger LOG = Logger.getLogger(CatalogEJB.class);

	@Override
	@WebMethod
	@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, 
			use = SOAPBinding.Use.LITERAL, 
			parameterStyle = SOAPBinding.ParameterStyle.BARE)
	public @WebResult(name = "book", 
			targetNamespace = "http://ns.soacookbook.com/catalog")
	Book getBook(@WebParam(name = "isbn", 
			targetNamespace = "http://ns.soacookbook.com/catalog") String isbn) {

		LOG.info("Executing. ISBN=" + isbn);
		Book book = new Book();

		// you would go to a database here.
		if ("12345".equals(isbn)) {
			LOG.info("Search by ISBN: " + isbn);
			book.setTitle("King Lear");
			Author shakespeare = new Author();
			shakespeare.setFirstName("William");
			shakespeare.setLastName("Shakespeare");
			book.setAuthor(shakespeare);
			book.setCategory(Category.LITERATURE);
			book.setIsbn("12345");

		} else {
			LOG.info("Search by ISBN: " + isbn + ". NO RESULTS.");
		}

		LOG.info("Returning book: " + book.getTitle());
		return book;
	}

	 @WebMethod
   public @WebResult(name="title") String 
           getTitle(
           @WebParam(name="id") String id)  {
       
       if ("12345".equals(id)) return "Hamlet";
       if ("98765".equals(id)) return "King Lear";
       if ("55555".equals(id)) return "Macbeth";
           
       return "--Item not in catalog--";
   }
}
