package org.example;

import java.util.concurrent.Semaphore;

public class StarvingPhilosopher extends Philosopher {
    public StarvingPhilosopher(int id, Semaphore left, Semaphore right) {
        super(id, left, right);
    }

    @Override
    protected void eat() {
        try {
            left.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (right.tryAcquire()) {
            eatBase();
            right.release();
        }
        left.release();
    }
}
