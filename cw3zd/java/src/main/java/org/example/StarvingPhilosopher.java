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

            if (right.tryAcquire()) {
                eatBase();
                right.release();
            }

            left.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
