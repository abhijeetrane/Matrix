package matrix.computerscience;
import java.util.concurrent.ThreadLocalRandom;

public class BinaryTreeThreads {

    // Maximum depth of the binary tree (Depth 3 = 15 total threads)
    private static final int MAX_DEPTH = 3;

    // Worker Thread representing a node in the tree
    static class TreeNodeTask extends Thread {
        private final String nodeId;
        private final int depth;

        public TreeNodeTask(String nodeId, int depth) {
            this.nodeId = nodeId;
            this.depth = depth;
            // Name the thread for clear log output
            this.setName("Thread-" + nodeId);
        }

        @Override
        public void run() {
            log("Started execution at depth " + depth);

            // Perform simulated work for this node
            doWork();

            // If we haven't reached max depth, spawn 2 child threads (Binary Tree expansion)
            if (depth < MAX_DEPTH) {
                log("Spawning child threads...");

                // Create Left and Right child worker threads
                TreeNodeTask leftChild = new TreeNodeTask(nodeId + ".L", depth + 1);
                TreeNodeTask rightChild = new TreeNodeTask(nodeId + ".R", depth + 1);

                // Start execution of child threads concurrently
                leftChild.start();
                rightChild.start();

                // Wait for both child threads to finish execution
                try {
                    leftChild.join();
                    rightChild.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println(getName() + " was interrupted while waiting for children.");
                }

                log("Child threads (" + leftChild.nodeId + ", " + rightChild.nodeId + ") finished.");
            } else {
                log("Reached maximum depth (" + MAX_DEPTH + "). Leaf node work complete.");
            }

            log("Finished execution.");
        }

        private void doWork() {
            try {
                // Simulate processing time between 200ms - 500ms
                int sleepTime = ThreadLocalRandom.current().nextInt(200, 501);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void log(String message) {
            System.out.printf("[%s] [Depth %d] %s%n", Thread.currentThread().getName(), depth, message);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Binary Tree Multithreading Simulation Started ===");
        System.out.println("Max Depth: " + MAX_DEPTH + "\n");

        long startTime = System.currentTimeMillis();

        // Spawn Root Thread (Node "1" at Depth 1)
        TreeNodeTask rootThread = new TreeNodeTask("1", 1);
        rootThread.start();

        // Main thread waits for the root (and all its sub-tree branches) to complete
        try {
            rootThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("\n=== All Threads Executed Successfully ===");
        System.out.println("Total Execution Time: " + duration + " ms");
    }
}