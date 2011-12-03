package com.sc.ch02.xstream;




import java.io.Serializable;

import com.sc.ch02.jaxb.Author;
import com.sc.ch02.jaxb.Book;
import com.sc.ch02.jaxb.Book.Category;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Shows Java to XML back to Java with no mapping
 * using XStream.
 */
public class XMLStreamRoundTrip  {
    
    public static void main(String...arg){
     
        //Create a complex object to work with
        Book book = new Book();
        Author a = new Author();
        a.setFirstName("Jacques");
        a.setLastName("Derrida");
        book.setAuthor(a);
        book.setPrice(39.95);
        book.setTitle("Glas");
        book.setCategory(Category.PHILOSOPHY);
        
        //Put the book into XML
        XMLStreamRoundTrip x = new XMLStreamRoundTrip();
        String bookXml = x.toXml(book);
        
        //Print entire XML
        System.console().printf("XML:\n%s\n", bookXml);
        
        //Create a new object by rehydrating the XML
        Book newBook = x.fromXml(bookXml);
        
        //Show values
        System.console().printf("Object:\n%s costs $%s\n", 
                newBook.getTitle(), newBook.getPrice());
        
    }

    public <T> String toXml(T model) {
        return new XStream().toXML(model);
    }

    @SuppressWarnings("unchecked")
    public <T>T fromXml(String modelAsString) {
        XStream xstream = new XStream(new DomDriver());
        T model = (T)xstream.fromXML(modelAsString);
        return model;
    }
}
    
   