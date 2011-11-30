package com.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;

public class PropertyTest {
	@Test
	public void t(){
		ComplexBean bean = new ComplexBean();
		bean.setArray(new int[]{1,2,4,7});
		bean.setId("zhai");
		
		List<String> list = new ArrayList<String>();
		list.add("aa");
		list.add("bb");
		bean.setList(list);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("key", "value");
		map.put("k", "val");
		bean.setMap(map);
		
		MyBean myBean = new MyBean();
		myBean.setId("myben");
		myBean.setName("ben");
		bean.setMyBean(myBean);
		
		List<PersonBean> personList = new ArrayList<PersonBean>();
		personList.add(new PersonBean(1, "per1", 12));
		personList.add(new PersonBean(1, "per23", 22));
		bean.setPersonList(personList);
		Map<String, Object> properties = new HashMap<String, Object>();
		
		try {
			properties = BeanUtils.describe(bean);
			Object obj = properties.get("personList");
			System.out.println(obj.getClass().getName());
			System.out.println(obj);
			
			obj = properties.get("map");
			System.out.println(obj);
			
			Object object = PropertyUtils.getProperty(bean, "personList");
			
			if(object instanceof List){
				System.out.println("list");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对象间的复制
	 * @param args
	 */
	@Test
	public void copy() {
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
