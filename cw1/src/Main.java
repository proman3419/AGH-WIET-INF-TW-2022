public class Main {
    private static void testCounterResult(Counter counter, final int itersCount) {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < itersCount; i++) {
                counter.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < itersCount; i++) {
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
        final int itersCount = 1000000;
        CounterImpl counter = new CounterImpl();
        SynchronizedCounterImpl synchronizedCounter = new SynchronizedCounterImpl();

        testCounterResult(counter, itersCount); // differs
        testCounterResult(synchronizedCounter, itersCount); // 0

        testMaxThreadsCount(); // 3700
    }
}