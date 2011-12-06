package com.sc.ch02.jaxb.annotation;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "doc")
public class DocType {
	@XmlAttribute
	String id ;
	public Map<KeyType, EntryType> key2entry = new HashMap<KeyType, EntryType>();

	public DocType() {
	}
}
