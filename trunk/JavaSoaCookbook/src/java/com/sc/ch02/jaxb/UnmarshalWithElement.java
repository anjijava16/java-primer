package com.sc.ch02.jaxb;


import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 * Using JAXBElement to Unmarshal an XML String

 * @author slieer
 * Create Date2011-12-3
 * version 1.0
 */
public class UnmarshalWithElement {
    
  public static void main(String...arg) {
    try {

        //Create context
        JAXBContext ctx = JAXBContext.newInstance(Book.class);
        
        //Create marshaller
        Unmarshaller um = ctx.createUnmarshaller();

        //Read in the XML from anywhere
        //In this case it is a complete XML book as string.
        StringReader sr = new StringReader(getBookXml());
        
        //Get XML from object
        JAXBElement<Book> b = um.unmarshal(
                new StreamSource(sr), Book.class);
        
        //Start working with object
        Book book = b.getValue();
        
        System.console().printf("Title: %s", book.getTitle());

    } catch (JAXBException ex) {
        ex.printStackTrace();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
  }
  
  private static String getBookXml(){
      return "<" + Book.PACKAGE_PATH + ">" +
              "<title>On Friendship</title>" +
              "<price>39.95</price>" + 
              "</" + Book.PACKAGE_PATH + ">";
  }
}
