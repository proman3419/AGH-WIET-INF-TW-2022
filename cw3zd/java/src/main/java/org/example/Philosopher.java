package org.example;

import java.util.concurrent.Semaphore;

public abstract class Philosopher extends Thread {
    protected static final long EAT_TIME = 100;
    protected static final long THINK_TIME = 100;
    protected final Semaphore left;
    protected final Semaphore right;
    protected final int id;
    protected long eatCount = 0;
    protected long thinkCount = 0;
    protected long waitTime = 0;

    public Philosopher(int id, Semaphore left, Semaphore right) {
        this.id = id;
        this.left = left;
        this.right = right;
    }

    protected void eatNotify() {
        if (Main.PRINT_ALL) System.out.printf("Philosopher %d is eating for the %d. time\n", id, eatCount);
    }

    protected void eatEndNotify() {
        if (Main.PRINT_ALL) System.out.printf("Philosopher %d has ended eating\n", id);
    }

    protected void thinkNotify() {
        if (Main.PRINT_ALL) System.out.printf("Philosopher %d is thinking for the %d. time\n", id, thinkCount);
    }

    protected void thinkEndNotify() {
        if (Main.PRINT_ALL) System.out.printf("Philosopher %d has ended thinking\n", id);
    }

    protected void eatBase() {
        eatCount++;
        eatNotify();
        try {
            Thread.sleep(EAT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        eatEndNotify();
    }

    protected abstract void eat();

    private void think() {
        thinkCount++;
        thinkNotify();
        handledSleep(THINK_TIME);
        thinkEndNotify();
    }

    protected void handledSleep(long time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        do {
            think();
            eat();
        } while (eatCount < Main.ITERS_COUNT);
    }

    public long getWaitTime() {
        return waitTime;
    }

    public long getEatCount() {
        return eatCount;
    }
}
