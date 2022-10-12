public class SynchronizedCounterImpl implements Counter {
    private int counter = 0;

    @Override
    public synchronized void increment() {
        counter++;
    }

    @Override
    public synchronized void decrement() {
        counter--;
    }

    @Override
    public int getCounter() {
        return counter;
    }
}
