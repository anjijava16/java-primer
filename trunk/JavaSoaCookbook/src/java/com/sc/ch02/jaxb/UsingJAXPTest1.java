package com.sc.ch02.jaxb;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;

import com.sc.ch02.jaxb.gen.BookType;
import com.sc.ch02.jaxb.gen.Collection;

public class UsingJAXPTest1 {
	private File xmlFile = null;
	
	/**
	 * xml to JavaBean
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception{
		JAXBContext jc = JAXBContext.newInstance("com.sc.ch02.jaxb.gen");
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Collection collection= (Collection)unmarshaller.unmarshal(xmlFile);
		
		Collection.Books books = collection.getBooks();
		List<BookType> list =  books.getBook();
		for(BookType t : list){
			System.out.println(t);
		}
	}
	
	@Before
	public void file(){
		URL u = this.getClass().getResource("");
		String path = u.getFile() + "books.xml";
		System.out.println(path);
		xmlFile = new File(path);
	}
}
