package http;

import org.junit.Test;

import com.xcap.ifc.XMLValidator;

public class XmlvalidatorTest {

	@Test
	public void xml() {
		String xml = "test/http/xml/example-new-contacts.xml";
		String schemaFilePath = "test/http/xml/contacts.xsd";
		try {
			int re = XMLValidator.xmlFileValidator(xml,schemaFilePath);
			System.out.println(re);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
