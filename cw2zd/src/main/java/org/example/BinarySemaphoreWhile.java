package org.example;

class BinarySemaphoreWhile implements Semaphore {
    private boolean state = true;
    private int awaits = 0;

    public BinarySemaphoreWhile() {
    }

    @Override
    public synchronized void P() {
        while (!state) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        state = false;
        this.notifyAll();
    }

    @Override
    public synchronized void V() {
        while (state) {
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
