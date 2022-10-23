import java.util.ArrayList;

class Buffer {
    private final int[] buffer;
    private final int size;
    private boolean putLock = false;
    private boolean getLock = false;
    private int lastPutId = -1;
    private int lastGetId = -1;

    public Buffer(int n) {
        buffer = new int[n];
        size = n;
    }

    public void put(int i) {
        int currPutId = (lastPutId + 1) % size;
        putLock = currPutId == lastGetId;
        if (!putLock) {
            buffer[currPutId] = i;
            lastPutId = currPutId;
        } else {
            while (putLock) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public int get() {

    }
}
