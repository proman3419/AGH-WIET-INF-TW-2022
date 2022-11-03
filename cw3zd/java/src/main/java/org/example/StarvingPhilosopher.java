package org.example;

import java.util.concurrent.Semaphore;

public class StarvingPhilosopher extends Philosopher {
    public StarvingPhilosopher(int id, Semaphore left, Semaphore right) {
        super(id, left, right);
    }

    @Override
    protected void eat() {
        long startTime = System.nanoTime();
        try {
            left.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long endTime;
        if (right.tryAcquire()) {
            endTime = System.nanoTime();
            eatBase();
            right.release();
        } else {
            endTime = System.nanoTime();
        }
        left.release();
        waitTime += endTime - startTime;
    }
}
