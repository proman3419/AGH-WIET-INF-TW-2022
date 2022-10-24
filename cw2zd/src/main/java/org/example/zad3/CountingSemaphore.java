package org.example.zad3;

import org.example.zad1_2.BinarySemaphoreWhile;
import org.example.zad1_2.Semaphore;

class CountingSemaphore implements Semaphore {
    private int state;
    private final BinarySemaphoreWhile stateAccess;
    private final BinarySemaphoreWhile canDecrease;

    public CountingSemaphore(int state) {
        if (state <= 0) {
            System.out.println("The state's value should be positive, setting 1 as the default value");
            this.state = 1;
        } else {
            this.state = state;
        }
        stateAccess = new BinarySemaphoreWhile();
        canDecrease = new BinarySemaphoreWhile();
    }

    @Override
    public void P() { // lock
        canDecrease.P();
        stateAccess.P();

        state--;

        if (state > 0) {
            canDecrease.V();
        }
        stateAccess.V();
    }

    @Override
    public void V() { // unlock
        stateAccess.P();

        state++;

        canDecrease.V();
        stateAccess.V();
    }
}
