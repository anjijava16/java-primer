package com.sc.ch02.stax;

import static java.lang.System.out;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import org.junit.Test;

public class StaxIterator {
	@Test
	public void test() {
		find();
	}

	public void find() {
		XMLInputFactory xif = XMLInputFactory.newInstance();
		// forward-only, most efficient way to read
		XMLEventReader reader = null;

		// get ahold of the file
		try {
			final InputStream is = new FileInputStream(Book.file);
			// create the reader from the stream
			reader = xif.createXMLEventReader(is);

			// work with stream and get the type of event
			// we're inspecting
			while (reader.hasNext()) {
				XMLEvent e = reader.nextEvent();

				if (e.isStartElement()) {
					e = e.asStartElement().getAttributeByName(new QName("sku"));

					if (e != null) {
						out.println(e);
					}
				}
			} // end loop
		} catch (Exception e) {
			out.println("Cannot parse: " + e);
		}
	}
}
