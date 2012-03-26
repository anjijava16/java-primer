package com.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.junit.Test;

import simple.foundation.StringTest;

public class CollectionTest {
	static enum e{a,a1,a2};

	void test(){
		List list = new ArrayList();         //Object基本数组
		List llist = new LinkedList();       //双项链表

		
		Set set = new TreeSet();  //内部采用TreeMap 实现,红黑树。
		set.toArray();
		Set hset = new HashSet();  //内部采用HashMap实现。
		//LinkedHashSet
		
		Set eSet =  EnumSet.noneOf(e.class);    //Enum[]
		Set<e> set2 = EnumSet.allOf(e.class);
		set2.toArray();
		Set<e> set3 = EnumSet.of(e.a1, e.a2);

		Map map = new HashMap();         // HashMap.Entry[]
		//LinkedHashMap ;WeakHashMap;IdentityHashMap
		
		//TreeMap.Entry<K,V>  &  Node in the Tree(自已，孩子，父) & 红黑树（Red-Black tree）
		Map tMap = new TreeMap();        
	    Map eMap = new EnumMap(e.class); //Object基本数组
	    
	    PriorityQueue queue = new PriorityQueue(); //Object[] & balanced binary heap
	}
	
	static void arrayToList(){
		List<String> list = new ArrayList<String>();
		String[] strs = new String[list.size()];
		//Collections.
		list.toArray(strs);
		
		arrayToList(strs);
		arrayToList("");
	}
	
	static void arrayToList(String... strs){
		System.out.println("multiple parameter.");
		List<String> wordList = Arrays.asList(strs);
	}

	
	static void arrayToList(String strs){
		System.out.println("single parameter.");
	}
	
	static Object[] arrayLen(){
		return new Object[]{null, null};
	}
	
	
	static void mapEntry(){
		HashMap<Long, Boolean>purchasedGoodsIdsMap = new HashMap<Long, Boolean>();
		purchasedGoodsIdsMap.put(1L, false);
		
		Set<Entry<Long, Boolean>>  entrySet = purchasedGoodsIdsMap.entrySet();
		
		Long[] ids = new Long[entrySet.size()];
		int index = 0;
		Iterator<Entry<Long, Boolean>> it = entrySet.iterator();
		while(it.hasNext()){
			Entry<Long,Boolean> entry = it.next();
			if(! entry.getValue()){
				System.out.println("......");
				ids[index++] = entry.getKey();
			}
		}
		
	}
	
	public static void main(String[] args) {
		//arrayToList();
		//System.out.println(arrayLen().length);
		//mapEntry();
		
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		ids.add(11L);
		ids.add(111L);
		
		Long[] idArr = new Long[ids.size()];
		ids.toArray(idArr);
		
		System.out.println(idArr);
	}
	
	
}
