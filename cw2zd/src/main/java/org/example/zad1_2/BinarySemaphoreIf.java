package org.example.zad1_2;

class BinarySemaphoreIf implements Semaphore {
    private boolean state = true;
    private int awaits = 0;

    public BinarySemaphoreIf() {
    }

    @Override
    public synchronized void P() { // sleep
        awaits++;
        if (!state) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        state = false;
        awaits--;
    }

    @Override
    public synchronized void V() { // wake-up
        if (awaits > 0) {
            notify();
        }
        state = true;
    }
}
