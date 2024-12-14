/*
 * Provide barriers for multiplication nodes.
 */
import java.util.concurrent.CyclicBarrier;

public class BarrierManager {
    private static BarrierManager instance = null;

    private CyclicBarrier barrier1;
    private CyclicBarrier barrier2;

    private BarrierManager(int numOfParties) {
        barrier1 = new CyclicBarrier(numOfParties);
        barrier2 = new CyclicBarrier(numOfParties);
    }

    //provide the global instance
    public static BarrierManager getInstance(int numOfParties) {
        if (instance == null) {
            synchronized (BarrierManager.class) {
                if (instance == null) {
                    instance = new BarrierManager(numOfParties);
                }
            }
        }
        return instance;
    }

    public CyclicBarrier getBarrier1() {
        return barrier1;
    }

    public CyclicBarrier getBarrier2() {
        return barrier2;
    }
}
