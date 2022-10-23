public class CounterImpl implements Counter {
    private int counter = 0;

    @Override
    public void increment() {
        counter++;
    }

    @Override
    public void decrement() {
        counter--;
    }

    @Override
    public int getCounter() {
        return counter;
    }
}
