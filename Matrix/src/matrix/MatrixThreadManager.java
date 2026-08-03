package matrix;
public class MatrixThreadManager {

    // Shared object used for signaling and synchronization
    private static final Object lock = new Object();
    
    // States to track which thread is allowed to run: 
    // 0 = Reality, 1 = EastMatrix, 2 = WestMatrix
    private static int activeThread = 0; 

    public void alternateBetweenEastAndWestMatrix() {
        // 1. Instantiate the worker threads
        Thread eastMatrix = new Thread(new EastMatrixTask(), "EastMatrix");
        Thread westMatrix = new Thread(new WestMatrixTask(), "WestMatrix");

        // Start worker threads so they enter their waiting state
        eastMatrix.start();
        westMatrix.start();

        // 2. Thread "Reality" (Master Controller)
        Thread reality = new Thread(() -> {
            System.out.println("[Reality] Starting control loop...\n");

            
            
            // Loop to alternate execution between EastMatrix and WestMatrix
          while (0 < 1 ) { // Set to 4 cycles (adjust or make infinite as needed)
                
                // --- Call EastMatrix ---
                synchronized (lock) {
                    System.out.println("[Reality] Signaling Thread EastMatrix to run...");
                    activeThread = 1; // Hand control to EastMatrix
                    lock.notifyAll(); // Wake up worker threads
                    
                    // Wait until EastMatrix finishes its 1-second run and releases control back
                    while (activeThread != 0) {
                        try {
                            lock.wait();                               
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                // --- Call WestMatrix ---
                synchronized (lock) {
                    System.out.println("[Reality] Signaling Thread WestMatrix to run...");
                    activeThread = 2; // Hand control to WestMatrix
                    lock.notifyAll(); // Wake up worker threads
                    
                    // Wait until WestMatrix finishes its 1-second run and releases control back
                    while (activeThread != 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

            //System.out.println("\n[Reality] Alternating sequence completed.");
            //System.exit(0); // Exit process
        }, "Reality");

        // Start thread Reality
        reality.start();
    }

    // Task for EastMatrix Thread
    static class EastMatrixTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    // Wait until Reality gives signal (activeThread == 1)
                    while (activeThread != 1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }

                // Execute for 5 seconds
                System.out.println("  ---> [" + Thread.currentThread().getName() + "] Executing task for 1 seconds...");
                try {
                    Thread.sleep(1000); // 1-second active execution / sleep period
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("  ---> [" + Thread.currentThread().getName() + "] Done. Returning control to Reality.\n");

                // Hand control back to Reality
                synchronized (lock) {
                    activeThread = 0;
                    lock.notifyAll();
                }
            }
        }
    }

    // Task for WestMatrix Thread
    static class WestMatrixTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    // Wait until Reality gives signal (activeThread == 2)
                    while (activeThread != 2) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }

                // Execute for 5 seconds
                System.out.println("  ---> [" + Thread.currentThread().getName() + "] Executing task for 1 second...");
                try {
                    Thread.sleep(1000); // 1-second active execution / sleep period
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("  ---> [" + Thread.currentThread().getName() + "] Done. Returning control to Reality.\n");

                // Hand control back to Reality
                synchronized (lock) {
                    activeThread = 0;
                    lock.notifyAll();
                }
            }
        }
    }
}