package com.sc.ch02.xstream;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * xstream 注释测试
 * http://xstream.codehaus.org/
 * @author me
 *
 */
public class Tutorial {
	@Test
	public void annotationTest() {
		XStream xstream = new XStream();
		xstream.processAnnotations(RendezvousMessage.class);
		RendezvousMessage msg = new RendezvousMessage(15, "firstPart","secondPart");
		
		System.out.println(xstream.toXML(msg));
	}		

}

/**
 * rendezvous ['rɔndivu:, -dei-]
		n. 约会；约会地点；集结地
		vt. 在指定地点与…相会
		vi. 会合；约会

 * @author me
 */
@XStreamAlias("message")
class RendezvousMessage {
	
	@XStreamAlias("type")
	private int messageType;

	@XStreamImplicit(itemFieldName="part")
	private List<String> content;

	public RendezvousMessage(int messageType, String ... content) {
		this.messageType = messageType;
		this.content = Arrays.asList(content);
	}
	
	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "RendezvousMessage [messageType=" + messageType + "]";
	}

}

@XStreamAlias("message")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"content"})
class RendezvousMessage1st {

	@XStreamAlias("type")
	private int messageType;

	@XStreamImplicit(itemFieldName="part")
	private List<String> content;
	
	@XStreamConverter(value=BooleanConverter.class, booleans={false}, strings={"yes", "no"})
	private boolean important;
	
	@XStreamConverter(SingleValueCalendarConverter.class)
	private Calendar created = new GregorianCalendar();

	public RendezvousMessage1st(int messageType, boolean important, String... content) {
		this.messageType = messageType;
		this.important = important;
		this.content = Arrays.asList(content);
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

	public boolean isImportant() {
		return important;
	}

	public void setImportant(boolean important) {
		this.important = important;
	}

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}

}

class SingleValueCalendarConverter implements Converter {

  public void marshal(Object source, HierarchicalStreamWriter writer,
          MarshallingContext context) {
      Calendar calendar = (Calendar) source;
      writer.setValue(String.valueOf(calendar.getTime().getTime()));
  }

  public Object unmarshal(HierarchicalStreamReader reader,
          UnmarshallingContext context) {
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(new Date(Long.parseLong(reader.getValue())));
      return calendar;
  }

  public boolean canConvert(Class type) {
      return type.equals(GregorianCalendar.class);
  }
}

