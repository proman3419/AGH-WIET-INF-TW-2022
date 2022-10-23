package org.example;

class BinarySemaphoreIf implements Semaphore {
    private boolean state = true;
    private int awaits = 0;

    public BinarySemaphoreIf() {
    }

    @Override
    public synchronized void P() {
        if (!state) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        state = false;
        this.notifyAll();
    }

    @Override
    public synchronized void V() {
        if (state) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        state = true;
        this.notifyAll();
    }
}
