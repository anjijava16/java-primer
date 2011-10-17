package com.xcap.ifc.error;

/**
 * @version 1.0<br>
 * @CreateDate 2011-10-17<br>
 * @author slieer
 * 
 * http://www.ietf.org/rfc/rfc4825.txt [Page 48]<br>
 * \servers\sip-presence\xdm\commons\src\main\java\org\openxdm\xcap\common\error
 * <ul>
 *  <li>1、not-well-formed {@link NotWellFormedConflictException}</li>
 *	<li>2、no-parent {@link NoParentConflictException}</li>
 *	<li>3、not-xml-frag {@link }</li>
 *	<li>4、schema-validation-error {@link SchemaValidationErrorConflictException}</li>
 *	<li>5、not-xml-att-value {@link NotXMLAttributeValueConflictException}</li>
 *  <li>6、cannot-insert {@link CannotInsertConflictException}</li>
 *	<li>7、cannot-delete {@link CannotDeleteConflictException}</li>
 *	<li>8、uniqueness-failure {@link UniquenessFailureConflictException}</li>
 *	<li>9、constraint-failure {@link ConstraintFailureConflictException}</li>
 *	<li>10、extension {@link }</li>
 *	<li>11、not-utf-8 {@link NotUTF8ConflictException}</li>
 *</ul>
 *
 */
public class XCAPErrors {
	private XCAPErrors(){}
	
	public static class NotWellFormedConflictException extends ConflictException {
		private static final long serialVersionUID = 1L;

		public NotWellFormedConflictException() {}
		
		protected String getConflictError() {
			return "<not-well-formed />";
		}
	}
	
	public static class NoParentConflictException extends ConflictException {

		private static final long serialVersionUID = 1L;
		private String conflictError = null;
		private String existingAncestor = null;
		private String queryComponent = null;
		private String schemeAndAuthorityURI = null;
				
		public NoParentConflictException(String existingAncestor) {
			if (existingAncestor == null) {
				throw new IllegalArgumentException("existing ancestor must not be null");
			}
			this.existingAncestor = existingAncestor;
		}
		
		public void setQueryComponent(String queryComponent) {
			this.queryComponent = queryComponent;
		}
		
		public void setSchemeAndAuthorityURI(String schemeAndAuthorityURI) {		
			this.schemeAndAuthorityURI = schemeAndAuthorityURI;
		}
		
		protected String getConflictError() {
			if (conflictError == null) {
				if (schemeAndAuthorityURI != null) {
					StringBuilder sb = new StringBuilder("<no-parent><ancestor>").append(schemeAndAuthorityURI);
					if (existingAncestor != "") {
						sb.append(existingAncestor);
					}
					if (queryComponent != null) {
						sb.append('?').append(queryComponent);
					}
					sb.append("</ancestor></no-parent>");
					conflictError = sb.toString();				
				}
				else{
					return "<parent />";
				}			
			}
			return conflictError;
		}
	}	
	
	public static class NotValidXMLFragmentConflictException extends ConflictException {
		private static final long serialVersionUID = 1L;		
		
		protected String getConflictError() {
			return "<not-xml-frag />";
		}
	}
	
	public static class NotXMLAttributeValueConflictException extends ConflictException {
		private static final long serialVersionUID = 1L;
		
		protected String getConflictError() {
			return "<not-xml-att-value />";
		}
	}
	
	public static class CannotInsertConflictException extends ConflictException {
		private static final long serialVersionUID = 1L;
		
		public CannotInsertConflictException() {}
		
		protected String getConflictError() {
			return "<cannot-insert />";
		}
	}	
	
	public static class CannotDeleteConflictException extends ConflictException {
		private static final long serialVersionUID = 1L;

		protected String getConflictError() {
			return "<cannot-delete />";
		}
	}
		
	public static class UniquenessFailureConflictException extends ConflictException {
		private static final long serialVersionUID = 1L;
		
		protected String getConflictError() {
			return "<uniqueness-failure />";
		}
	}
	
	public static class ConstraintFailureConflictException extends ConflictException {
		private static final long serialVersionUID = 1L;
		private String phrase;
				
		protected String getConflictError() {
			StringBuilder sb = new StringBuilder("<constraint-failure phrase='").append(phrase).append("' />");
			return sb.toString();
		}
	}
	
	public static class NotUTF8ConflictException extends ConflictException {
		private static final long serialVersionUID = 1L;
		
		protected String getConflictError() {
			return "<not-utf-8 />";
		}
	}
	
	public static class SchemaValidationErrorConflictException extends ConflictException {
		private static final long serialVersionUID = 1L;
		
		protected String getConflictError() {
			return "<schema-validation-error />";
		}
	}

	
}
