package pl.polsl.service.impl;

public class RelayService {

    private final int timeout; // in nanoseconds

    public RelayService() {
        this.timeout = 50000000;
    }

    public RelayService(int timeout) {
        this.timeout = timeout * 1000000;
    }

    void calculatePi() {
        double pi = 0;
        boolean exit = false;
        int counter = 0;
        long time = System.nanoTime();
        do {
            pi += Math.pow(-1, counter)/(2*counter+1);
            counter++;
            long measuredTime = System.nanoTime();
            if (measuredTime >= time + timeout) {
                exit = true;
            }
        } while (!exit);
    }

    void calculateTimelessPi() {
        double pi = 0;
        boolean exit = false;
        for (int i = 0; i < 1000000; i++) {
            pi += Math.pow(-1, i)/(2*i+1);
        }
        System.out.println(pi);
    }
}
