package com.sc.ch02.jaxb.annotation;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.InputSource;

import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import com.sun.org.apache.xerces.internal.dom.ElementImpl;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * XmlTransient 可以控制那些元素不转化。
 * @author slieer
 * Create Date2011-12-6
 * version 1.0
 */
public class JavaXmlTest {
	private Marshaller marshaller = null;
	
	@Test
	public void java2Xml() throws Exception{
		JAXBContext context = JAXBContext.newInstance(DocType.class);
		constructContext(context );
        DocType d = new DocType();
        d.id = "10";
        
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        d.key2entry.put(new KeyType("key", new XMLGregorianCalendarImpl(cal)), new EntryType("id","artists", "program"));
        marshaller.marshal(d, System.out);   
		
	}
	
	@Test
	public void strListTest() throws Exception{
		JAXBContext context = JAXBContext.newInstance(Sentence.class);
		constructContext(context );
		
		Sentence s = new Sentence("zhi","wang","li");
		marshaller.marshal(s, System.out);
	}
	
	@Test
	public void ojbListTest() throws Exception{
		JAXBContext context = JAXBContext.newInstance(Paragraph.class);
		constructContext(context );
		
		Paragraph p = new Paragraph(new Sentence("zhi","wang","li"), new Sentence("S","Big","Small"));
		marshaller.marshal(p, System.out);
	}
	
	@Test
	public void mixtureTest() throws Exception{
		JAXBContext context = JAXBContext.newInstance(MixtureType.class);
		constructContext(context );
		MixtureType p = new MixtureType();
		p.setTitle("china");
		QName key = new QName("baidu.com");
		p.getAny().put(key, "zh");
		p.getAny().put(key, "xia");
		marshaller.marshal(p, System.out);
	}
	
	@Test
	public void zooTypeTest() throws JAXBException{
		JAXBContext context = JAXBContext.newInstance(ZooType.class);
		constructContext(context );
		
		ZooType zooType = new ZooType();
		CoreDocumentImpl ownerDoc = new CoreDocumentImpl();
		Element el = ownerDoc.createElement("Aa");
		el.setAttribute("key", "man");
		Node node = ownerDoc.createTextNode("abc");
		el.appendChild(node);
		
		zooType.setAnimalArray(el);
		
		marshaller.marshal(zooType, System.out);
	}
	
	@Test
	public void zooTypeUnmarshallerTest() throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance( ZooType.class );
		Unmarshaller u = jc.createUnmarshaller();
		
		String xml = "<zoo>" +
					"<a>Anaconda</a>" +
					"<b>Buffalo</b>"  +
					"<c>Chameleon</c>" +
					"<d>Dromedar</d>"  +
					"</zoo>" ;

		InputSource source = new InputSource(new ByteArrayInputStream(xml.getBytes()));
		ZooType doc = (ZooType)u.unmarshal(source);
		for( Element el: doc.getAnimals() ){
		    System.out.println( el.getNodeName() + "->" +
		                        el.getTextContent() );
		}
		
	}
	
	private void constructContext(JAXBContext context) throws JAXBException{
        marshaller = context.createMarshaller();   
        marshaller.setProperty(Marshaller.JAXB_ENCODING,"gb2312");//编码格式   
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//是否格式化生成的xml串   
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);//是否省略xml头信息（<?xml version="1.0" encoding="gb2312" standalone="yes"?>）   
		
	}
	
}