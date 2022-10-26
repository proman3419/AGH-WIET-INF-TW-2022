public class StarvingPhilosopher extends Philosopher {
    public StarvingPhilosopher(int id, Stick left, Stick right) {
        super(id, left, right);
    }

    @Override
    protected void eat() {
        try {
            left.acquire();

            if (right.tryAcquire()) {
                eatingCount++;
                eatNotify();
                Thread.sleep(EATING_TIME);
                right.release();
                eatEndNotify();
            }

            left.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
