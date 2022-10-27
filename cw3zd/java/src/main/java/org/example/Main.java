package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static List<Double> getAverageWaitTimes(List<Philosopher> philosophers, double unit) {
        return philosophers.stream()
                .map(philosopher -> (((double) philosopher.getWaitTime() * unit) / Philosopher.ITERS_COUNT))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<Long> getEatingCounts(List<Philosopher> philosophers) {
        return philosophers.stream()
                .map(Philosopher::getEatCount)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static void runTest(List<Philosopher> philosophers) {
        philosophers.forEach(Philosopher::start);
        philosophers.forEach(philosopher -> {
            try {
                philosopher.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(getAverageWaitTimes(philosophers, Math.pow(10, -9)));
        System.out.println(getEatingCounts(philosophers));
    }

    private static void testStarvingPhilosopher(int philosophersCount) {
        List<Semaphore> sticks = Stream
                .generate(() -> new Semaphore(1))
                .limit(philosophersCount)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Philosopher> philosophers = new ArrayList<>();
        for (int i = 0; i < philosophersCount; i++) {
            philosophers.add(new StarvingPhilosopher(i, sticks.get(i), sticks.get((i + 1) % philosophersCount)));
        }
        runTest(philosophers);
    }

    private static void testJudgingPhilosopher(int philosophersCount) {
        List<Semaphore> sticks = Stream
                .generate(() -> new Semaphore(1))
                .limit(philosophersCount)
                .collect(Collectors.toCollection(ArrayList::new));
        Semaphore judge = new Semaphore(1);
        List<Philosopher> philosophers = new ArrayList<>();
        for (int i = 0; i < philosophersCount; i++) {
            philosophers.add(new JudgingPhilosopher(i, sticks.get(i), sticks.get((i + 1) % philosophersCount), judge));
        }
        runTest(philosophers);
    }

    public static void main(String[] args) {
        testStarvingPhilosopher(25);
//        testJudgingPhilosopher(5);
    }
}
