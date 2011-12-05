package com.sc.ch02.jaxb;

import static java.lang.System.out;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;

import com.sc.ch02.jaxb.Book.Category;

/**
 * Using JAXBElement to Unmarshal an XML String
 * 
 * @author slieer Create Date2011-12-3 version 1.0
 */
public class UnmarshalWithElement {
	 Book book = null;
	 String bookXml = null;
	
	@Before
	public void initTest(){
		book = new Book();
		Author a = new Author();
		a.setFirstName("Jacques");
		a.setLastName("Derrida");
		book.setAuthor(a);
		book.setPrice(34.95);
		book.setTitle("Of Grammatology");
		book.setCategory(Category.PHILOSOPHY);
		
		bookXml = "<book>" +
					"<author><firstName>Jacques</firstName><lastName>Derrida</lastName></author>" +
					"<category>PHILOSOPHY</category>" +
					"<price>34.95</price>" +
					"<title>Of Grammatology</title>" +
				 "</book>";
	}
	
	/**
	 * xml to JavaBean.
	 * @throws Exception
	 */
	@Test
	public void unmarchal() throws Exception {
		JAXBContext ctx = JAXBContext.newInstance(Book.class);
		Unmarshaller um = ctx.createUnmarshaller();

		StringReader sr = new StringReader(bookXml);

		JAXBElement<Book> b = um.unmarshal(new StreamSource(sr), Book.class);

		Book book = b.getValue();
		System.out.println(book);
		//System.console().printf("Title: %s", book.getTitle());
	}
	
	@Test
	public void unmarchalList(){
		
	}
	
	/**
	 * javaBean to xml
	 * @throws Exception
	 */
	@Test
	public void marchal() throws Exception {
		Class[] c = { Book.class };
		JAXBContext ctx = JAXBContext.newInstance(c);
		Marshaller m = ctx.createMarshaller();

		m.marshal(book, out);
		//m.marshal(book, new FileOutputStream(new File("aBook.xml")));
		out.println("\nAll done.");
	}
}
