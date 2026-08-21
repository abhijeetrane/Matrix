package matrix.computerscience;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BinaryTreeThreadVisualizer extends JFrame {

    // Status colors: RED = Running, GREEN = Completed, GRAY = Unvisited
    private static final Color COLOR_RUNNING = new Color(220, 53, 69);
    private static final Color COLOR_COMPLETED = new Color(40, 167, 69);
    private static final Color COLOR_DEFAULT = new Color(108, 117, 125);

    private final int maxDepth;
    private final Map<String, Color> nodeStatusMap = new ConcurrentHashMap<>();
    private final TreePanel treePanel;

    public BinaryTreeThreadVisualizer(int maxDepth) {
        this.maxDepth = maxDepth;
        setTitle("Binary Tree Multithreading Simulation");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        treePanel = new TreePanel();
        add(new JScrollPane(treePanel), BorderLayout.CENTER);

        // Start tree thread execution on a background thread
        new Thread(this::startSimulation).start();
    }

    private void startSimulation() {
        System.out.println("Starting Binary Tree thread execution for depth: " + maxDepth);
        spawnNodeThread(0, "1");
        System.out.println("All thread branches completed successfully.");
    }

    private void spawnNodeThread(int currentDepth, String nodeId) {
        // Update visual state to Running
        nodeStatusMap.put(nodeId, COLOR_RUNNING);
        treePanel.repaint();

        // Simulate work performed by this thread
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Branching step: Spawn left and right child threads in parallel
        if (currentDepth < maxDepth) {
            String leftId = nodeId + ".1";
            String rightId = nodeId + ".2";

            Thread leftThread = new Thread(() -> spawnNodeThread(currentDepth + 1, leftId), "ThreadNode-" + leftId);
            Thread rightThread = new Thread(() -> spawnNodeThread(currentDepth + 1, rightId), "ThreadNode-" + rightId);

            leftThread.start();
            rightThread.start();

            // Wait for both child threads to finish execution
            try {
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Update visual state to Completed
        nodeStatusMap.put(nodeId, COLOR_COMPLETED);
        treePanel.repaint();
    }

    // Panel responsible for rendering the tree structure and node statuses
    private class TreePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawNode(g2, 0, "1", getWidth() / 2, 50, getWidth() / 4);
        }

        private void drawNode(Graphics2D g2, int currentDepth, String nodeId, int x, int y, int xOffset) {
            if (currentDepth > maxDepth) return;

            int nextY = y + 70;

            // Draw connections to child nodes
            if (currentDepth < maxDepth) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.setStroke(new BasicStroke(2));
                
                // Left branch line
                g2.drawLine(x, y, x - xOffset, nextY);
                // Right branch line
                g2.drawLine(x, y, x + xOffset, nextY);

                // Recurse down left and right children
                drawNode(g2, currentDepth + 1, nodeId + ".1", x - xOffset, nextY, xOffset / 2);
                drawNode(g2, currentDepth + 1, nodeId + ".2", x + xOffset, nextY, xOffset / 2);
            }

            // Draw node circle
            int radius = 20;
            Color statusColor = nodeStatusMap.getOrDefault(nodeId, COLOR_DEFAULT);
            g2.setColor(statusColor);
            g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - radius, y - radius, radius * 2, radius * 2);

            // Draw node label
            g2.drawString(nodeId, x - 10, y + 5);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String input = JOptionPane.showInputDialog(
                    null,
                    "Enter Binary Tree Depth (e.g., 0 to 5):",
                    "Binary Tree Multithreading",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input == null || input.trim().isEmpty()) {
                System.exit(0);
            }

            try {
                int depth = Integer.parseInt(input.trim());
                if (depth < 0) {
                    JOptionPane.showMessageDialog(null, "Depth must be 0 or greater.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

                BinaryTreeThreadVisualizer frame = new BinaryTreeThreadVisualizer(depth);
                frame.setVisible(true);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
    }
}