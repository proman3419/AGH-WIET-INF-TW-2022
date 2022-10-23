package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Race {
    private long counter = 0;
    private final Semaphore semaphore;

    public Race(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    private void increment() {
        semaphore.P();
        counter++;
        semaphore.V();
    }

    private void decrement() {
        semaphore.P();
        counter--;
        semaphore.V();
    }

    public void start(final int THREADS_COUNT, final long ITERS_COUNT) {
        System.out.printf("%d incrementing and decrementing threads, each of them does %d iterations\n", THREADS_COUNT, ITERS_COUNT);

        System.out.println("Creating threads");
        List<Thread> incThreads = Stream.generate(() -> new Thread(() -> {
            for (int i = 0; i < ITERS_COUNT; i++) {
                increment();
            }
        })).limit(THREADS_COUNT).collect(Collectors.toCollection(ArrayList::new));
        List<Thread> decThreads = Stream.generate(() -> new Thread(() -> {
            for (int i = 0; i < ITERS_COUNT; i++) {
                decrement();
            }
        })).limit(THREADS_COUNT).collect(Collectors.toCollection(ArrayList::new));

        System.out.println("Starting threads");
        for (int i = 0; i < THREADS_COUNT; i++) {
            incThreads.get(i).start();
            decThreads.get(i).start();
        }

        try {
            for (int i = 0; i < THREADS_COUNT; i++) {
                incThreads.get(i).join();
                decThreads.get(i).join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        System.out.printf("Finished the race, counter's state: %d\n", counter);
    }
}
