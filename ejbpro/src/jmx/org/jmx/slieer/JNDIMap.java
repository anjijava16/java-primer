package org.jmx.slieer;

import java.util.HashMap;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;

import org.jboss.naming.NonSerializableFactory;
import org.jboss.system.ServiceMBeanSupport;

public class JNDIMap extends ServiceMBeanSupport implements JNDIMapMBean {
	private String jndiName;
	private HashMap contextMap = new HashMap();

	public JNDIMap() {
		super();
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) throws NamingException {
		String oldName = this.jndiName;
		this.jndiName = jndiName;
		if (super.getState() == STARTED) {
			unbind(oldName);
			try {
				rebind();
			} catch (Exception e) {
				NamingException ne = new NamingException("Failed to update jndiName");
				ne.setRootCause(e);
				throw ne;
			}
		}

	}

	public void startService() throws Exception {
		rebind();
	}

	public void stopService() {
		unbind(jndiName);
	}

	private void rebind() throws NamingException {
		InitialContext rootCtx = new InitialContext();
		Name fullName = rootCtx.getNameParser("").parse(jndiName);
		NonSerializableFactory.rebind(fullName, contextMap, true);
	}

	private void unbind(String jndiName) {
		try {
			InitialContext rootCtx = new InitialContext();
			rootCtx.unbind(jndiName);
			NonSerializableFactory.unbind(jndiName);
		} catch (NamingException e) {
			System.out.println(e);
		}
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getState() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}

	public void jbossInternalLifecycle(String arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public void create() throws Exception {
		// TODO Auto-generated method stub

	}

	public void start() throws Exception {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public ObjectName preRegister(MBeanServer arg0, ObjectName arg1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void postRegister(Boolean arg0) {
		// TODO Auto-generated method stub

	}

	public void preDeregister() throws Exception {
		// TODO Auto-generated method stub

	}

	public void postDeregister() {
		// TODO Auto-generated method stub

	}

}
