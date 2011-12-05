package com.sc.ch02.xstream;

import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class Json {
	private Person bean = null;
	
	@Before
	public void init(){
		bean = new Person("Li", "Jia", new PhoneNumber(1, "1234-456"), new PhoneNumber(2, "1234-456"));		
	}
	
	@Test
	public void writeEntiry2JSON() {
	    XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
	    //xstream.setMode(XStream.NO_REFERENCES);
	    xstream.alias("person", Person.class);
	    //xstream.toXML(bean);
	    
	    xstream = new XStream(new JsonHierarchicalStreamDriver() {
	        public HierarchicalStreamWriter createWriter(Writer out) {
	            return new JsonWriter(out, JsonWriter.DROP_ROOT_MODE);
	        }
	    });
	    //xstream.setMode(XStream.NO_REFERENCES);
	    //xstream.alias("person", Person.class);
	    String json = xstream.toXML(bean);
	    System.out.println(json);
	}
	
	
	

}
