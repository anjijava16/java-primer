package com.sc.ch02.stax;

import static java.lang.System.out;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class WriteStax {
	private static final String REPAIR_NS = "javax.xml.stream.isRepairingNamespaces";

	private static final String NS = "http://ns.example.com/books";

	public static void main(String... args) {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		// autobox
		factory.setProperty(REPAIR_NS, true);

		try {
			// setup a destination file
			FileOutputStream fos = new FileOutputStream("result.xml");

			// create the writer
			final XMLStreamWriter xsw = factory.createXMLStreamWriter(fos);
			xsw.setDefaultNamespace(NS);

			// open the document. Can also add encoding, etc
			xsw.writeStartDocument("1.0");
			xsw.writeEndDocument();

			xsw.writeComment("Powered by StAX");

			// make enclosing book
			xsw.writeStartElement("book");
			xsw.writeNamespace("b", NS);
			xsw.writeAttribute("sku", "345_iui");

			// make title child element
			xsw.writeStartElement(NS, "title");
			xsw.writeCharacters("White Noise");
			xsw.writeEndElement(); // close title

			xsw.writeEndElement(); // close book

			// clean up
			xsw.flush();
			fos.close();
			xsw.close();

			out.print("All done.");
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (XMLStreamException xse) {
			xse.printStackTrace();
		}
	}
}
