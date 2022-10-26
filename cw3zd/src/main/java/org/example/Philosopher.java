package org.example;

import java.util.concurrent.Semaphore;

public abstract class Philosopher extends Thread {
    public  static final long ITERS_COUNT = 30;
    protected static final long EATING_TIME = 100;
    protected static final long THINKING_TIME = 100;
    protected final Semaphore left;
    protected final Semaphore right;
    protected final int id;
    protected long eatingCount = 0;
    protected long thinkingCount = 0;
    protected long waitTime = 0;

    public Philosopher(int id, Semaphore left, Semaphore right) {
        this.id = id;
        this.left = left;
        this.right = right;
    }

    protected void eatNotify() {
        System.out.printf("Philosopher %d is eating for the %d. time\n", id, eatingCount);
    }

    protected void eatEndNotify() {
        System.out.printf("Philosopher %d has ended eating\n", id);
    }

    protected void thinkNotify() {
        System.out.printf("Philosopher %d is thinking for the %d. time\n", id, thinkingCount);
    }

    protected void thinkEndNotify() {
        System.out.printf("Philosopher %d has ended thinking\n", id);
    }

    protected void eatBase() {
        eatingCount++;
        eatNotify();
        try {
            Thread.sleep(EATING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        eatEndNotify();
    }

    protected abstract void eat();

    private void think() {
        thinkingCount++;
        thinkNotify();
        try {
            long startTime = System.nanoTime();
            sleep(THINKING_TIME);
            long deltaTime = System.nanoTime() - startTime;
            waitTime += deltaTime;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thinkEndNotify();
    }

    @Override
    public void run() {
        for (int i = 0; i < ITERS_COUNT; i++) {
            eat();
            think();
        }
    }

    public long getWaitTime() {
        return waitTime;
    }

    public long getEatingCount() {
        return eatingCount;
    }
}
