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
        long startTime = System.nanoTime();
        while (!judge.tryAcquire()) ;
        long endTime;
        try {
            left.acquire();
            right.acquire();
            endTime = System.nanoTime();
            eatBase();
            left.release();
            right.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
            endTime = System.nanoTime();
        }
        judge.release();
        waitTime += endTime - startTime;
    }
}
