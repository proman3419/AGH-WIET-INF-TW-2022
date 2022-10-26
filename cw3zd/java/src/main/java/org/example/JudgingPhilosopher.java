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
        if (judge.tryAcquire()) {
            if (left.tryAcquire()) {
                if (right.tryAcquire()) {
                    eatBase();
                    right.release();
                }
                left.release();
            }
            judge.release();
        }
    }
}
