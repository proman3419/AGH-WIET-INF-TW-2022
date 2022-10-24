package org.example.zad1_2;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final int testsCount = 25;
        final int threadsCount = 10;
        final int itersCount = 100000;

        boolean error = args.length == 0;
        int semaphoreType = 0;

        if (args[0].equals("if")) {
            semaphoreType = 1;
            System.out.println("BinarySemaphoreIf");
        } else if (args[0].equals("while")) {
            System.out.println("BinarySemaphoreWhile");
        } else {
            error = true;
        }

        if (error) {
            System.out.println("Expected argument: 'if' or 'while'");
        } else {
            System.out.printf("Running %d tests\n", testsCount);
            List<Long> results = new ArrayList<>();
            for (int i = 0; i < testsCount; i++) {
                Race race;
                if (semaphoreType == 1) {
                    race = new Race(new BinarySemaphoreIf());
                } else {
                    race = new Race(new BinarySemaphoreWhile());
                }
                race.start(threadsCount, itersCount);
                results.add(race.getCounter());
            }
            System.out.print("Counters' final values: ");
            System.out.println(results);
        }
    }
}
