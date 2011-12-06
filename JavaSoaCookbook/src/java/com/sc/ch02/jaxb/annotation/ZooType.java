package com.sc.ch02.jaxb.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.w3c.dom.Element;

@XmlRootElement(name="zoo")
public class ZooType {
	protected List<Element> animals;

	public ZooType() {
	}

	@XmlAnyElement
	public List<Element> getAnimals() {
		if (animals == null)
			animals = new ArrayList<Element>();
		return animals;
	}

	public void setAnimals(List<Element> value) {
		animals = value;
	}
	
	public void setAnimalArray(Element... elements) {
		animals = Arrays.asList(elements);
	}
}
