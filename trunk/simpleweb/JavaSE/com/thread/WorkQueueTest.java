package com.thread;


public class WorkQueueTest {
	
	public static void main(String...args){
		testWorkQueue();
	}
	
	public static void testWorkQueue(){
	// Create the work queue
		WorkQueue queue = new WorkQueue();

		// Create a set of worker threads
		final int numWorkers = 2;
		Worker[] workers = new Worker[numWorkers];
		for (int i=0; i<workers.length; i++) {
		    workers[i] = new Worker(queue);
		    workers[i].start();
		}

		// Add some work to the queue; block if the queue is full.
		// Note that null cannot be added to a blocking queue.
		for (int i=0; i<100; i++) {
		    queue.addWork(i);
		}

		// Add special end-of-stream markers to terminate the workers
		for (int i=0; i<workers.length; i++) {
		    queue.addWork(Worker.NO_MORE_WORK);
		}		
	}
}
