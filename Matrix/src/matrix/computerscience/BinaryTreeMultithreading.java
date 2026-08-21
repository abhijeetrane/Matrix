package matrix.computerscience;

import javax.swing.JOptionPane;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class BinaryTreeMultithreading {

    // Task representing a node in the thread tree
    static class TreeNodeTask extends RecursiveAction {
        private final int currentDepth;
        private final int maxDepth;
        private final String nodeId;

        public TreeNodeTask(int currentDepth, int maxDepth, String nodeId) {
            this.currentDepth = currentDepth;
            this.maxDepth = maxDepth;
            this.nodeId = nodeId;
        }

        @Override
        protected void compute() {
            String indent = "  ".repeat(currentDepth);
            System.out.printf("%s[Thread: %s] Executing Node %s at Depth %d%n",
                    indent, Thread.currentThread().getName(), nodeId, currentDepth);

            // Base case: stop spawning threads when max depth is reached
            if (currentDepth >= maxDepth) {
                return;
            }

            // Create left and right child thread tasks
            TreeNodeTask leftChild = new TreeNodeTask(currentDepth + 1, maxDepth, nodeId + ".1");
            TreeNodeTask rightChild = new TreeNodeTask(currentDepth + 1, maxDepth, nodeId + ".2");

            // Fork both child tasks to run in parallel
            invokeAll(leftChild, rightChild);
        }
    }

    public static void main(String[] args) {
        // Prompt user for binary tree depth using a GUI dialog box
        String input = JOptionPane.showInputDialog(
                null,
                "Enter the binary tree depth (e.g., 3):",
                "Binary Tree Threading Simulation",
                JOptionPane.QUESTION_MESSAGE
        );

        if (input == null || input.trim().isEmpty()) {
            System.out.println("No depth entered. Exiting program.");
            return;
        }

        try {
            int depth = Integer.parseInt(input.trim());
            if (depth < 0) {
                JOptionPane.showMessageDialog(null, "Depth must be non-negative.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("Starting Binary Tree multithreading simulation with depth: " + depth);
            System.out.println("------------------------------------------------------------");

            // Execute the root task in the common thread pool
            ForkJoinPool pool = ForkJoinPool.commonPool();
            TreeNodeTask rootTask = new TreeNodeTask(0, depth, "1");
            pool.invoke(rootTask);

            System.out.println("------------------------------------------------------------");
            System.out.println("All thread branches completed successfully.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid integer input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}