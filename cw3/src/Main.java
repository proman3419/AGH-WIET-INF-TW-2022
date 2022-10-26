import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void testStarvingPhilosopher(int philosophersCount) {
        List<Stick> sticks = Stream.generate(() -> new Stick(1))
                .limit(philosophersCount)
                .collect(Collectors.toCollection(ArrayList::new));
        List<StarvingPhilosopher> philosophers = new ArrayList<>();
        for (int i = 0; i < philosophersCount; i++) {
            philosophers.add(new StarvingPhilosopher(i, sticks.get(i), sticks.get((i + 1) % philosophersCount)));
        }

        philosophers.forEach(StarvingPhilosopher::start);
        philosophers.forEach(philosopher -> {
            try {
                philosopher.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        testStarvingPhilosopher(5);
    }
}