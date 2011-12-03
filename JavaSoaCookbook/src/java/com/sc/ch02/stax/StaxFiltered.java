package com.sc.ch02.stax;

import static java.lang.System.out;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;

public class StaxFiltered {
//    private static final String fdb = "path/ch02/Catalog.xml";

    private Map<String, Double> expensiveBooks;

    private String lastTitle;
    
    //constructor
    public StaxFiltered() {
        expensiveBooks = new HashMap<String, Double>();
    }

    public static void main(String[] args) {
        StaxFiltered p = new StaxFiltered();

        p.findByEvent();
    }

    /*
     * Here our aim is to find book prices over $10. So we use a
     * filter to give us only start elements so we have already
     * filtered out items we know don't help us.
     */
    public void findByEvent() {
        try {
            XMLInputFactory xif = XMLInputFactory.newInstance();

            FileReader fr = new FileReader(Book.file);

            // wrap the XMLStreamReader with FilteredReader
            XMLEventReader reader =
                    xif.createFilteredReader(
                            xif.createXMLEventReader(fr),
                            new StartElementEventFilter());

            // work with stream and get the type of event
            // we're inspecting
            while (reader.hasNext()) {

                XMLEvent event = (XMLEvent) reader.next();
                int eventType = event.getEventType();

                switch (eventType) {
                case XMLEvent.START_ELEMENT:

                    findHighPrices(reader, event);
                }

            } // end loop

            out.println("Expensive books=" + expensiveBooks);

        } catch (FileNotFoundException fnfe) {
            out.println("Cannot find source: " + fnfe);
        } catch (XMLStreamException e) {
            out.println("Cannot parse: " + e);
        }
    }

    private void findHighPrices(XMLEventReader reader,
            XMLEvent event) throws XMLStreamException {

        String currentElem = event.asStartElement().toString();

        // save off the title so we can match the price with it
        if ("<title>".equals(currentElem)) {
            lastTitle = reader.getElementText();
        }

        // get the current price and add to collection if high
        if ("<price>".equals(currentElem)) {
            double price;
            try {
                price = Double.parseDouble(reader
                                .getElementText());

                if (price > 10.0D) {
                    expensiveBooks.put(lastTitle, price);
                }

            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            } catch (XMLStreamException xse) {
                xse.printStackTrace();
            }
        }
    }
}
/**
 * Get only start elements for efficiency. If we returned only
 * attributes, for example, we wouldn't be able to read the data
 * we're interested in here (title and price values).
 */
class StartElementEventFilter implements EventFilter {
    // only req'd method to implement
    public boolean accept(XMLEvent event) {
        return event.isStartElement();
    }
}