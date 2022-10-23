package org.example;

public class Main {
    public static void main(String[] args) {
        final int THREADS_COUNT = 10;
        final int ITERS_COUNT = 1000000;
        boolean error = args.length == 0;

        if (!error) {
            if (args[0].equals("if")) {
                System.out.println("SemaphoreIf");
                new Race(new BinarySemaphoreIf()).start(THREADS_COUNT, ITERS_COUNT);
            } else if (args[0].equals("while")) {
                System.out.println("SemaphoreWhile");
                new Race(new BinarySemaphoreWhile()).start(THREADS_COUNT, ITERS_COUNT);
            } else {
                error = true;
            }
        }

        if (error) {
            System.out.println("Expected argument: 'if' lub 'while'");
        }
    }
}
