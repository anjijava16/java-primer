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
	public void t(){
		XStream xstream = new XStream();
		//XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library 
		//XStream xstream = new XStream(new StaxDriver()); // does not require XPP3 library
		
		xstream.alias("person", Person.class);
		xstream.alias("phonenumber", PhoneNumber.class);
		//xstream.alias("persons", List.class);
		
		xml = xstream.toXML(list);
	}
	
	
	@After
	public void after(){
		XStream xstream = new XStream(new DomDriver()); 
		xstream.alias("person", Person.class);
		xstream.alias("phonenumber", PhoneNumber.class);

		Person newJoe = (Person)xstream.fromXML(xml);
		System.out.println("to Object:" + newJoe);
	}
	
}
