package com.sc.ch02.jaxb;

import static java.lang.System.out;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sc.ch02.jaxb.Book.Category;

/**
 * Example: Using JAXB to Marshal a Book Object into XML and Print it tothe Console

 * @author slieer
 * Create Date 2011-12-3
 * version 1.0
 */
public class MarshalToConsole {

	public static void main(String... arg) {
		try {

			Book book = new Book();
			Author a = new Author();
			a.setFirstName("Jacques");
			a.setLastName("Derrida");
			book.setAuthor(a);
			book.setPrice(34.95);
			book.setTitle("Of Grammatology");
			book.setCategory(Category.PHILOSOPHY);

			Class[] c = { Book.class };
			JAXBContext ctx = JAXBContext.newInstance(c);
			Marshaller m = ctx.createMarshaller();

			// could also use System.out here
			m.marshal(book, out);
/*			try {
				m.marshal(book, new FileOutputStream(new File("aBook.xml")));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/

			out.println("\nAll done.");
		} catch (JAXBException ex) {
			ex.printStackTrace();
		}
	}
}
