class Producer extends Thread {
    private Buffer _buf;

    public Producer(Buffer buffer) {
        _buf = buffer;
    }

    public void run() {
        for (int i = 0; i < 100; ++i) {
            _buf.put(i);
        }
    }
}
