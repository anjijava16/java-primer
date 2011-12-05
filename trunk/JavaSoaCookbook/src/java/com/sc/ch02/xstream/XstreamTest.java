package com.sc.ch02.xstream;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XstreamTest {
	private List<Person> list = new ArrayList<Person>();
	private String xml = null; 
	
	@Before
	public void init(){
		Person joe = new Person("Joe", "Walnes");
		joe.setPhone(new PhoneNumber(123, "1234-456"));
		joe.setFax(new PhoneNumber(123, "9999-999"));
		list.add(joe);
		
		Person jia = new Person("Li", "Jia");
		jia.setPhone(new PhoneNumber(124, "1234-4567"));
		jia.setFax(new PhoneNumber(123, "9999-999"));
		
		list.add(jia);
	}
	
	@Test
	public void toXmlStr(){
		XStream xstream = new XStream();
		//XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library 
		//XStream xstream = new XStream(new StaxDriver()); // does not require XPP3 library
		
		xstream.alias("person", Person.class);
		xstream.alias("phonenumber", PhoneNumber.class);
		xml = xstream.toXML(list.get(0));
		System.out.println(xml);
	}
	
	@Test
	public void listToXml(){
		XStream xstream = new XStream();
		//XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library 
		//XStream xstream = new XStream(new StaxDriver()); // does not require XPP3 library
		
		xstream.alias("persons", ArrayList.class);
		xstream.alias("person", Person.class);
		xstream.alias("phonenumber", PhoneNumber.class);
		xstream.alias("persons", List.class);
		
		//xstream.aliasField("author", Blog.class, "writer");	
		//xml = xstream.toXML(list);
		String xmlStr = xstream.toXML(list);
		System.out.println(xmlStr);
	}
	
	
	@After
	public void after(){
		if(xml != null){
			XStream xstream = new XStream(new DomDriver()); 
			xstream.alias("person", Person.class);
			xstream.alias("phonenumber", PhoneNumber.class);
			xstream.alias("persons", List.class);
			
			Person newJoe = (Person)xstream.fromXML(xml);
			System.out.println("to Object:" + newJoe);
		}
	}
	
}
