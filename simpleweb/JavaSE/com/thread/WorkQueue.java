package com.thread;

import java.util.LinkedList;

public class WorkQueue {
  @SuppressWarnings("unchecked")
	LinkedList queue = new LinkedList();

  // Add work to the work queue
  @SuppressWarnings("unchecked")
	public synchronized void addWork(Object o) {
      queue.addLast(o);
      notify();
  }

  // Retrieve work from the work queue; block if the queue is empty
  public synchronized Object getWork() throws InterruptedException {
      while (queue.isEmpty()) {
          wait();
      }
      return queue.removeFirst();
  }
}
