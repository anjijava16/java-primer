package org.jmx.slieer;

public class Counter implements CounterMBean {
	int sum = 0;

	public void add(int num) {
		sum += num;
	}

	public int get(int num) {
		sum += num;
		return sum;
	}
}
