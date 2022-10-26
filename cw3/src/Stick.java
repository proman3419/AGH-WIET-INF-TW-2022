import java.util.concurrent.Semaphore;

public class Stick extends Semaphore {
    public Stick(int permits) {
        super(permits);
    }
}
