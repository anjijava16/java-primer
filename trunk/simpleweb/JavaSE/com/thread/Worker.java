	package com.thread;

public class Worker extends Thread {
    // Special end-of-stream marker. If a worker retrieves
    // an Integer that equals this marker, the worker will terminate.
    static final Object NO_MORE_WORK = new Object();

    WorkQueue q;

    Worker(WorkQueue q) {
        this.q = q;
    }
    
    public void run() {
        try {
            while (true) {
                // Retrieve some work; block if the queue is empty
                Object x = q.getWork();

                // Terminate if the end-of-stream marker was retrieved
                if (x == NO_MORE_WORK) {
                    break;
                }

                // Compute the square of x
                int y = ((Integer)x).intValue() * ((Integer)x).intValue();
            }
        } catch (InterruptedException e) {
        }
    }
}
