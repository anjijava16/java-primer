package com.sc.ch02.jaxb;

/**
 * $Id: UsingJAXPTest1.java,v 1.1 2003/01/01 03:18:32 bhakti Exp $
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * This shows how to parse an XML File using SAX and display the same
 * information from the xml file by implementing the ContentHandler interface
 */

public class UsingJAXPTest {
	String path = null;
	/**
	 * 取与 this class在同目录下的文件路径
	 */
	@Before
	public void filePath() throws FileNotFoundException{
		String resourceName = "books.xml"; 
	    path = getFilePath(resourceName);
	    System.out.println(path);
	    
	}
	
	/**
	 * sax 方式解析xml
	 */
	@Test
	public void saxParseXml() {
		InputSource is = null;
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			XMLReader xmlReader = spf.newSAXParser().getXMLReader();
			
			xmlReader.setContentHandler(new MyContentHandler());
			is = new InputSource(path);

			xmlReader.parse(is);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (SAXException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(0);
		}

	}
	
	/**
	 *同 URL u = this.getClass().getResource("");
	 * @param resourceName
	 * @return
	 */
	private String getFilePath(String resourceName) {
		Package pack = this.getClass().getPackage();
	    String packageName = pack.getName();
	    String classname_resource = "/" + packageName.replace('.', '/') + "/" + resourceName;
	    URL url = this.getClass().getResource(classname_resource);
	    String urlPath = url.getPath();
	    File classFile = new File(urlPath);
	    return classFile.getPath();
	}	
}

class MyContentHandler extends XMLFilterImpl {
	boolean printMe = false;
	static int authorCount = 0;

	private boolean getPrintValue() {
		return printMe;
	}

	private void setPrintValue(boolean printValue) {
		printMe = printValue;
	}

	public void characters(char[] ch, int start, int length) {
		String gotString = new String(ch, start, length);
		if (printMe) {
			if (!gotString.trim().equals("")) {
				System.out.println(gotString.trim());
			}
		}
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attributes) {

		if (localName.equals("book")) {
			authorCount = 0;
			System.out.println("\nBook Details");
			int attrsLength = attributes.getLength();
			for (int i = 0; i < attrsLength; i++) {
				String itemId = attributes.getValue(i);
				System.out.println("ItemId " + itemId);

			}
		}

		if (localName.equals("ISBN") || localName.equals("price")
				|| localName.equals("description")
				|| localName.equals("Discount")
				|| localName.equals("bookCategory") || localName.equals("name")) {

			System.out.println("Book " + localName + ": ");
			printMe = true;

		} else if (localName.equals("authorName")) {
			System.out.println("Book " + localName + ": ");
			authorCount++;
			printMe = true;

		} else

			printMe = false;

	}

	public void endElement(String namespaceURI, String localName, String qName) {
		if (localName.equals("authors"))
			System.out.println("No of authors :" + authorCount);
	}

}
