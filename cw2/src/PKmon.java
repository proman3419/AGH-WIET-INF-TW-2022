import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PKmon {
    private static void testWaitNotify(int bufferSize, int producersCount, int consumentsCount) {
        Buffer buffer = new Buffer(bufferSize);
        ArrayList<Producer> producers = Stream.generate(() -> new Producer(buffer)).limit(producersCount)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Consumer> consumers = Stream.generate(() -> new Consumer(buffer)).limit(consumentsCount)
                .collect(Collectors.toCollection(ArrayList::new));

        producers.forEach(Producer::start);
        consumers.forEach(Consumer::start);
    }

    public static void main(String[] args) {
    }
}
