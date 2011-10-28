package org.jmx.slieer;

import javax.naming.NamingException;

import org.jboss.system.ServiceMBean;

public interface JNDIMapMBean extends ServiceMBean {
	public String getJndiName();

	public void setJndiName(String jndiName) throws NamingException;
}
