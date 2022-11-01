package org.example;

import java.util.concurrent.Semaphore;

public class JudgingPhilosopher extends Philosopher {
    private final Semaphore judge;

    public JudgingPhilosopher(int id, Semaphore left, Semaphore right, Semaphore judge) {
        super(id, left, right);
        this.judge = judge;
    }

    @Override
    protected void eat() {
        while (!judge.tryAcquire()) {
            waitTime(25);
        }
        try {
            left.acquire();
            right.acquire();
            eatBase();
            left.release();
            right.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        judge.release();
    }
}
