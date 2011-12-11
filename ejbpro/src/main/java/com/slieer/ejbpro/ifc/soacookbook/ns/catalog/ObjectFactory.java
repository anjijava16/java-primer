
package com.slieer.ejbpro.ifc.soacookbook.ns.catalog;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.slieer.ejbpro.ifc.soacookbook.ns.ws.catalog.Book;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.soacookbook.ns.catalog package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Book_QNAME = new QName("http://ns.soacookbook.com/catalog", "book");
    private final static QName _Isbn_QNAME = new QName("http://ns.soacookbook.com/catalog", "isbn");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.soacookbook.ns.catalog
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Book }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ns.soacookbook.com/catalog", name = "book")
    public JAXBElement<Book> createBook(Book value) {
        return new JAXBElement<Book>(_Book_QNAME, Book.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ns.soacookbook.com/catalog", name = "isbn")
    public JAXBElement<String> createIsbn(String value) {
        return new JAXBElement<String>(_Isbn_QNAME, String.class, null, value);
    }

}
