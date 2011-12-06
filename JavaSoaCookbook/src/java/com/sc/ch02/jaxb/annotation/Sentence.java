package com.sc.ch02.jaxb.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType
class Sentence {
	@XmlAttribute
	String name;
	
	@XmlElement(name="word")
	//@XmlElementWrapper( name="wrapper" )

	//@XmlList
	List<String> words = new ArrayList<String>();

	public Sentence() {
		super();
	}

	public Sentence(String name,String... val) {
		this.name = name;
		words = Arrays.asList(val);
	}

}
