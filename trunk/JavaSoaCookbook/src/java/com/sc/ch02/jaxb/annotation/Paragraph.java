package com.sc.ch02.jaxb.annotation;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="paragraph")
@XmlType
class Paragraph {
	@XmlElement
	List<Sentence> sentence;

	public Paragraph() {
		super();
	}

	public Paragraph(Sentence... w) {
		sentence = Arrays.asList(w);
	}
	
	
}
