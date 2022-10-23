package org.example;

class CountingSemaphore implements Semaphore {
    private int state;
    private final BinarySemaphoreWhile stateAccess;
    private final BinarySemaphoreWhile canDecrease;

    public CountingSemaphore(int state) {
        this.state = state;
        stateAccess = new BinarySemaphoreWhile();
        canDecrease = new BinarySemaphoreWhile();
    }

    @Override
    public void P() {
        canDecrease.P();
        stateAccess.P();

        state--;

        if (state > 0) {
            canDecrease.V();
        }
        stateAccess.V();
    }

    @Override
    public void V() {
        stateAccess.P();

        state++;

        if (state >= 1) {
            canDecrease.V();
        }
        stateAccess.V();
    }
}
