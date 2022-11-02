package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static long ITERS_COUNT;
    public static boolean PRINT_ALL;

    private static List<Double> getAverageWaitTimesInMiliseconds(List<Philosopher> philosophers, double unit) {
        return philosophers.stream()
                .map(philosopher -> (((double) philosopher.getWaitTime() * unit) / ITERS_COUNT))
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

        System.out.print("Average wait times [ms]: ");
        // waitTime [ns]
        getAverageWaitTimesInMiliseconds(philosophers, Math.pow(10, -6))
                .forEach(waitTime -> System.out.printf("%.0f, ", waitTime));
        System.out.println();
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
        Semaphore judge = new Semaphore(philosophersCount - 1);
        List<Philosopher> philosophers = new ArrayList<>();
        for (int i = 0; i < philosophersCount; i++) {
            philosophers.add(new JudgingPhilosopher(i, sticks.get(i), sticks.get((i + 1) % philosophersCount), judge));
        }
        runTest(philosophers);
    }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Expected 4 arguments: numberOfPhilosophers, itersCount, methodName <starving/arbiter>, printAll <true/false>");
        } else {
            int philosophersCount = Integer.parseInt(args[0]);
            ITERS_COUNT = Integer.parseInt(args[1]);
            String methodName = args[2];
            PRINT_ALL = Boolean.parseBoolean(args[3]);

            if (methodName.equals("starving")) {
                testStarvingPhilosopher(philosophersCount);
            } else if (methodName.equals("arbiter")) {
                testJudgingPhilosopher(philosophersCount);
            }
        }
    }
}
