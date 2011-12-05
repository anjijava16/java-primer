package com.sc.ch02.jaxb;

import java.io.PrintStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import com.sc.ch02.jaxb.hello.GreetingListType;
import com.sc.ch02.jaxb.hello.GreetingType;
import com.sc.ch02.jaxb.hello.ObjectFactory;

public class Hello {

	private ObjectFactory of;
	private GreetingListType grList;

	public Hello() {
		of = new ObjectFactory();
		grList = of.createGreetingListType();
	}

	@Test
	public void marshal() {
		try {
			make("a", "b");
			make( "Hey, you", "en" ); 

			JAXBElement<GreetingListType> gl = of.createGreetings(grList);
			JAXBContext jc = JAXBContext.newInstance("hello");
			Marshaller m = jc.createMarshaller();
			
			PrintStream out = System.out;
			m.marshal(gl, out);

		} catch (JAXBException jbe) {
			// ...
		}
	}

	public void make(String t, String l) {
		GreetingType g = of.createGreetingType();
		g.setText(t);
		g.setLanguage(l);
		grList.getGreeting().add(g);
	}

}
