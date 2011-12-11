
package com.slieer.ejbpro.ifc.soacookbook.ns.ws.catalog;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.soacookbook.ns.ws.catalog package. 
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

    private final static QName _GetTitle_QNAME = new QName("http://ns.soacookbook.com/ws/catalog", "getTitle");
    private final static QName _Book_QNAME = new QName("http://ns.soacookbook.com/ws/catalog", "book");
    private final static QName _GetTitleResponse_QNAME = new QName("http://ns.soacookbook.com/ws/catalog", "getTitleResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.soacookbook.ns.ws.catalog
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetTitleResponse }
     * 
     */
    public GetTitleResponse createGetTitleResponse() {
        return new GetTitleResponse();
    }

    /**
     * Create an instance of {@link GetTitle }
     * 
     */
    public GetTitle createGetTitle() {
        return new GetTitle();
    }

    /**
     * Create an instance of {@link Author }
     * 
     */
    public Author createAuthor() {
        return new Author();
    }

    /**
     * Create an instance of {@link Book }
     * 
     */
    public Book createBook() {
        return new Book();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTitle }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ns.soacookbook.com/ws/catalog", name = "getTitle")
    public JAXBElement<GetTitle> createGetTitle(GetTitle value) {
        return new JAXBElement<GetTitle>(_GetTitle_QNAME, GetTitle.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Book }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ns.soacookbook.com/ws/catalog", name = "book")
    public JAXBElement<Book> createBook(Book value) {
        return new JAXBElement<Book>(_Book_QNAME, Book.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTitleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ns.soacookbook.com/ws/catalog", name = "getTitleResponse")
    public JAXBElement<GetTitleResponse> createGetTitleResponse(GetTitleResponse value) {
        return new JAXBElement<GetTitleResponse>(_GetTitleResponse_QNAME, GetTitleResponse.class, null, value);
    }

}
