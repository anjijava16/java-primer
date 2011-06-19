package com.thread;


public class HookTest {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread3());
		Thread1 t1 = new Thread1();
		t1.start();
		Thread2 t2 = new Thread2();
		t2.start();

	}
}

class Thread1 extends Thread {
	public void run() {
		int i = 0;
		while (i < 10) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException ex) {
			}
			System.out.println("~Thread 1~");
			i++;
		}
	}
}

class Thread2 extends Thread {
	public void run() {
		int i = 0;
		while (i < 10) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
			}
			System.out.println("~Thread 2~");
			i++;
		}
	}
}

class Thread3 extends Thread {
	public void run() {
		System.out.println("---end---");
	}
}
