public class Main {
    private static void testCounterResult(Counter counter, final int ITERS_COUNT) {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < ITERS_COUNT; i++) {
                counter.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < ITERS_COUNT; i++) {
                counter.decrement();
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(counter.getCounter());
    }

    private static void testMaxThreadsCount() {
        for (int i = 0; ; i++) {
            new Thread(() -> {
                for (; ; ) ;
            }).start();
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        final int ITERS_COUNT = 1000000;
        CounterImpl counter = new CounterImpl();
        SynchronizedCounterImpl synchronizedCounter = new SynchronizedCounterImpl();

        testCounterResult(counter, ITERS_COUNT); // differs
        testCounterResult(synchronizedCounter, ITERS_COUNT); // 0

        testMaxThreadsCount(); // 3700
    }
}