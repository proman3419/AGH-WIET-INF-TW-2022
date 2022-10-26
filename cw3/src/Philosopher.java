public abstract class Philosopher extends Thread {
    // TODOD: extends Thread
    protected static final long EATING_TIME = 100;
    protected static final long THINKING_TIME = 50;
    protected final Stick left;
    protected final Stick right;
    protected final int id;
    protected long eatingCount = 0;
    protected long thinkingCount = 0;

    public Philosopher(int id, Stick left, Stick right) {
        this.id = id;
        this.left = left;
        this.right = right;
    }

    protected void eatNotify() {
        System.out.printf("Philosopher %d is eating for the %d. time\n", id, eatingCount);
    }

    protected void eatEndNotify() {
        System.out.printf("Philosopher %d has ended eating\n", id);
    }

    protected void thinkNotify() {
        System.out.printf("Philosopher %d is thinking for the %d. time\n", id, thinkingCount);
    }

    protected void thinkEndNotify() {
        System.out.printf("Philosopher %d has ended thinking\n", id);
    }

    protected abstract void eat();

    private void think() {
        thinkingCount++;
        thinkNotify();
        try {
            sleep(THINKING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thinkEndNotify();
    }

    @Override
    public void run() {
        for (;;) {
            eat();
            think();
        }
    }
}
