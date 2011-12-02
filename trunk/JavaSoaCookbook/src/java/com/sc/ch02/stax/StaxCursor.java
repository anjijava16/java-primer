package com.sc.ch02.stax;

import static java.lang.System.out;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.junit.BeforeClass;
import org.junit.Test;

/** Example: Using the Cursor parsing method of parsing in StAX. */
public class StaxCursor {
	private static InputStream is = null;
	private List<Book> books = new LinkedList<Book>();

	@BeforeClass
	public static void getResources() throws Exception {
		is = new FileInputStream(Book.file);
	}

	@Test
	public void test() {
		find();
		
		for(Book b : books){
			System.out.println(b);
		}
	}

	// parse the document and offload work to helpers
	public void find() {
		XMLInputFactory xif = XMLInputFactory.newInstance();
		XMLStreamReader reader = null;
		int eventType;
		String current = "";

		try {
			reader = xif.createXMLStreamReader(is);
			while (reader.hasNext()) {
				// because this is Cursor, we get an integer token to next event
				eventType = reader.next();

				switch (eventType) {
				case XMLEvent.START_ELEMENT:
					current = reader.getName().toString();
					initBook(current, reader);
					break;

				case XMLEvent.CHARACTERS:
					setProperties(current, reader);
					break;
			} // end loop
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
			out.println("Cannot parse: " + e);
		}
	}

	private void initBook(String current, XMLStreamReader r) {
		current = r.getName().toString();
		if ("book".equals(current)) {
			//System.out.println("new book....");
			String k = r.getAttributeName(0).toString();
			String v = r.getAttributeValue(0);
			book = new Book();
			book.sku = v;
			books.add(book);
			beforEl = null;
			System.out.println("----------------------" + books.size());			
		}
	}

	String beforEl = null;
	Book book = null;
	// inspect author elements and read their values.
	private void setProperties(String current, XMLStreamReader r) throws XMLStreamException {
		Book.BookField b = null;
		try{
			b = Book.BookField.valueOf(current);			
		}catch (Exception e) {
			//System.out.println(e.getMessage());
			return;
		}
		
		String v = r.getText().trim();
		
		if(beforEl == null || !beforEl.equals(current)){
			System.out.println("field,val---->" + b + "," + v);
			beforEl = current;
			switch (b) {
			case title:
				book.title = v;			
				break;
			case author:
				book.author = v;			
				break;
			case price:
				book.price = v;			
				break;
			case category:
				book.category = v;			
				break;
			}			
		}
	}
}
