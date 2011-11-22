package com.beans;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

public class PropertyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PersonBean bean = new PersonBean();
		bean.setAge(1);
		bean.setId(1);
		bean.setName("abc");
		
		PersonEntity en = new PersonEntity();
		try {
			/*
			 *第一个参数  target bean
			 *第二个参数 source bean
			 */
			PropertyUtils.copyProperties(en,bean);
			
			System.out.println(en);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
