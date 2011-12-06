package com.sc.ch02.jaxb.annotation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ParentType")
public class ParentType {
	protected List item;

	public ParentType(){}

	@XmlElement(name = "item")
	@XmlElementWrapper(name = "wrapper")
	public List getItem() {
		if (item == null)
			item = new ArrayList();
		return item;
	}
}
