package com.slieer.ejbpro.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DerbyTest {
	public static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	public static String protocol = "jdbc:derby:";

	public static void main(String[] args) {
		Properties props = new Properties();
		try {
				Class.forName(driver).newInstance();
				@SuppressWarnings("unused")
				Connection conn = DriverManager.getConnection(protocol + "derbyDB;create=true", props);
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
