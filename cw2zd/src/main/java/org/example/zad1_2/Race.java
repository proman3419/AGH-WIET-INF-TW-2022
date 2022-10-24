package org.example.zad1_2;

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

    public void start(final int threadsCount, final long itersCount) {
        System.out.printf("%d incrementing and decrementing threads, each of them performs %d iterations\n",
                threadsCount, itersCount);

        System.out.println("Creating threads");
        List<Thread> incThreads = Stream.generate(() -> new Thread(() -> {
            for (int i = 0; i < itersCount; i++) {
                increment();
            }
        })).limit(threadsCount).collect(Collectors.toCollection(ArrayList::new));
        List<Thread> decThreads = Stream.generate(() -> new Thread(() -> {
            for (int i = 0; i < itersCount; i++) {
                decrement();
            }
        })).limit(threadsCount).collect(Collectors.toCollection(ArrayList::new));

        System.out.println("Starting threads");
        incThreads.forEach(Thread::start);
        decThreads.forEach(Thread::start);

        for (int i = 0; i < threadsCount; i++) {
            try {
                incThreads.get(i).join();
                decThreads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Finished the race, the counter's state: %d\n", counter);
    }

    public long getCounter() {
        return counter;
    }
}
