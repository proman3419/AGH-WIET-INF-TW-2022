package org.example.zad3;

public class SharedResource {
    private final CountingSemaphore semaphore;

    SharedResource(int state) {
        semaphore = new CountingSemaphore(state);
    }

    public void performActivity() {
        semaphore.P();

        long currThreadId = Thread.currentThread().getId();

        System.out.printf("Thread %d performs an activity on the resource\n", currThreadId);

        for (int i = 0; i < 100000; i++) ; // dummy loop to simulate an activity being performed

        System.out.printf("Thread %d returns the resource to the pool\n", currThreadId);

        semaphore.V();
    }
}
