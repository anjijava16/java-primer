package test;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import org.jmx.slieer.Counter;

public class CounterServer {

	public static void main(String[] args) throws Exception {
		MBeanServer server = MBeanServerFactory.createMBeanServer();
		ObjectName name = new ObjectName("book.liuyang:service=Counter");
		server.registerMBean(new Counter(), name);
		String[] sig1 = { "int" };

		Object[] opArgs1 = { new Integer(3) };
		Object result1 = server.invoke(name, "add", opArgs1, sig1);
		System.out.println(result1);
		String[] sig2 = { "int" };
		Object[] opArgs2 = { new Integer(0) };
		Object result2 = server.invoke(name, "get", opArgs2, sig2);
		System.out.println(result2);
	}
}