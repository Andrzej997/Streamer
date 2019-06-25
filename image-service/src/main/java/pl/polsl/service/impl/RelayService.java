package pl.polsl.service.impl;

public class RelayService {

    private final int timeout; // in nanoseconds

    public RelayService() {
        this.timeout = 100000;
    }

    public RelayService(int timeout) {
        this.timeout = timeout * 1000;
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
}
