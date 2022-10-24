package org.example.zad1_2;

class BinarySemaphoreIf implements Semaphore {
    private boolean state = true;
    private int awaits = 0;

    public BinarySemaphoreIf() {
    }

    @Override
    public synchronized void P() { // lock
        awaits++;
        if (!state) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        awaits--;
        state = false;
    }

    @Override
    public synchronized void V() { // unlock
        if (awaits > 0) {
            notify();
        }
        state = true;
    }
}
