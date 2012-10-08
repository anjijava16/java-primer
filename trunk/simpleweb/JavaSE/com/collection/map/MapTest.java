package com.collection.map;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.junit.Test;

final public class MapTest {
	static enum e{a,a1,a2};
	Map<String,Ob> map = new HashMap<String, Ob>();
	
	final static class Ob{
		
	}
	
	@Test
	public void treeMapTest(){
		
	}
	
	public static void main(String[] args) {
		Map map = new HashMap();         // HashMap.Entry[]
		//LinkedHashMap ;WeakHashMap;IdentityHashMap
		
		//TreeMap.Entry<K,V>  &  Node in the Tree(自已，孩子，父) & 红黑树（Red-Black tree）
		Map tMap = new TreeMap();        
	    Map eMap = new EnumMap(e.class); //Object基本数组
	    
	    PriorityQueue queue = new PriorityQueue(); //Object[] & balanced binary heap
		
	}
}
