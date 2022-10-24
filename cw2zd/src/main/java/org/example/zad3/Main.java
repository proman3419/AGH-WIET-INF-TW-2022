package org.example.zad3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        final int resourcesCount = 4;
        final int threadsCount = 13;

        final SharedResource sharedResource = new SharedResource(resourcesCount);
        List<Thread> threads = Stream.generate(() -> new SharedResourceUser(sharedResource))
                .limit(threadsCount).collect(Collectors.toCollection(ArrayList::new));

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
