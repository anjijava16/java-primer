package com.xml.kxml2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

public class KXML2Test {
	@Test
	public void readTest() throws Exception {
		URL url = this.getClass().getResource("/");
		String xmlFile = url.getPath() + "com/xml/unformattedXML.xml";
		File file = new File(xmlFile);

		InputStream stream = new FileInputStream(file);
		KXmlParser parser = new KXmlParser();
		parser.setInput(stream, null);
		int eventType = parser.getEventType();
		
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				System.out.println(parser.getText());
				break;
			case XmlPullParser.END_DOCUMENT:
				
				break;
			case XmlPullParser.START_TAG:
				
				break;
			}
		}
	}
}
