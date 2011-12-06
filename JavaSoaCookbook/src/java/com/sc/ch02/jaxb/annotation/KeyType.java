package com.sc.ch02.jaxb.annotation;

import javax.xml.datatype.XMLGregorianCalendar;

public class KeyType {
	public String event;
	public XMLGregorianCalendar datetime;

	public KeyType() {
	}

	public KeyType(String event, XMLGregorianCalendar datetime) {
		this.event = event;
		this.datetime = datetime;
	}
}
