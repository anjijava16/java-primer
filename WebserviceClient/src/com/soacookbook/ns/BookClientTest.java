package com.soacookbook.ns;

import java.net.URL;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.soacookbook.ns.ws.catalog.Book;
import com.soacookbook.ns.ws.catalog.Catalog;

public class BookClientTest {
	static  String wsdl = "http://localhost:8080/ejbpro_ejb/CatalogService/Catalog?wsdl";
	
  public static void main(String[] args) throws Exception {

    //Specify the WSDL 
    URL wsdlLocation = new URL(wsdl);
    
    //Create a Qualified Name that represents the 
    //namespace and local part of the service
    QName serviceName = new QName("http://ns.soacookbook.com/ws/catalog", 
            "CatalogService");
    
    //Create a proxy to get a port stub from
    Service service = Service.create(wsdlLocation, serviceName);
            
    // Return a list of QNames of ports
    System.out.println("QNames of service endpoints:");
    Iterator<QName> it = service.getPorts();
    QName lastEndpoint = null;
    while (it.hasNext()) {
    lastEndpoint = it.next();
        System.out.println("Name: " + lastEndpoint);
    }

    // Get the Hello stub
    Catalog hello = service.getPort(lastEndpoint, Catalog.class);
    
    //Invoke the business method
    Book result = hello.getBook("12345");
    System.out.println("\nResponse: " + result);
    
    String title = hello.getTitle("98765");
    System.out.println("title:" + title);
  }
}