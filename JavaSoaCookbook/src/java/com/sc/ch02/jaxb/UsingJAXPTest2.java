package com.sc.ch02.jaxb;

/**
 * $Id: UsingJAXPTest2.java,v 1.1 2003/01/01 03:18:32 bhakti Exp $
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
 * This shows how to create an xml document using DOM validate it against the
 * books.xsd and transform it using identity transform to an xml file
 */

public class UsingJAXPTest2 {
	Document document = null;
	/**
	 * document to xml file
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception{
        DOMSource domSource = new DOMSource(document);
        
        URL u = this.getClass().getResource("");
		String path = u.getFile() + "books.xml";
		System.out.println(path);
		StreamResult streamResult =new StreamResult(new FileOutputStream(path));
        TransformerFactory tFactory = TransformerFactory.newInstance();

        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT , "yes");
        transformer.transform(domSource,streamResult);

        System.out.println("Document created using JAXP "); 
        System.out.println("see jaxpOutput2.xml for details "); 

		
	}
	
	/**
	 * dom 生成xml document.
	 */
	@Before
	public void dom() {

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			documentBuilderFactory.setValidating(true);
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilderFactory.setAttribute(
					"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");

	        URL u = this.getClass().getResource("");
			String path = u.getFile() + "books.xsd";

			documentBuilderFactory.setAttribute(
					"http://java.sun.com/xml/jaxp/properties/schemaSource",
					new InputSource(new FileInputStream(path)));

			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			// documentBuilder.setErrorHandler(new MyErrorHandler());

			document = documentBuilder.newDocument();

			Element collection = document.createElement("Collection");
			document.appendChild(collection);

			Element books = document.createElement("books");
			collection.appendChild(books);

			Element book = document.createElement("book");
			books.appendChild(book);

			Element name = document.createElement("name");
			Text nameText = document.createTextNode("JAXB today and beyond");
			name.appendChild(nameText);
			book.appendChild(name);

			Element ISBN = document.createElement("ISBN");
			Text ISBNText = document.createTextNode("987665");
			ISBN.appendChild(ISBNText);
			book.appendChild(ISBN);

			Element price = document.createElement("price");
			Text priceText = document.createTextNode("45$");
			price.appendChild(priceText);
			book.appendChild(price);

			Element authors = document.createElement("authors");
			Element authorName = document.createElement("authorName");
			Text authorNameText = document.createTextNode("Richard K.");
			authorName.appendChild(authorNameText);
			authors.appendChild(authorName);
			book.appendChild(authors);

			Element description = document.createElement("description");
			Text descriptionText = document
					.createTextNode("This is an intermediate book on JAXB");
			description.appendChild(descriptionText);
			book.appendChild(description);

			Element promotion = document.createElement("promotion");
			Element Discount = document.createElement("Discount");
			Text DiscountText = document.createTextNode("5% off regular price");
			Discount.appendChild(DiscountText);
			promotion.appendChild(Discount);
			book.appendChild(promotion);

			Element publicationDate = document.createElement("publicationDate");
			Text publicationDateText = document.createTextNode("2002-02-02");
			publicationDate.appendChild(publicationDateText);
			book.appendChild(publicationDate);

			Element bookCategory = document.createElement("bookCategory");
			Text bookCategoryText = document.createTextNode("other");
			bookCategory.appendChild(bookCategoryText);
			book.appendChild(bookCategory);

			Attr attr = document.createAttribute("itemId");
			attr.setValue("307");
			book.setAttributeNode(attr);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

	}
}
