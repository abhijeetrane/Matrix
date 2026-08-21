package matrix.computerscience;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class BinaryTreeInfiniteMultithreading extends JFrame {

    // Status colors
    private static final Color COLOR_DEFAULT = new Color(70, 80, 95);       // Idle / Unvisited
    private static final Color COLOR_DOWNWARDS = new Color(220, 53, 69);     // Downward traversal (Red)
    private static final Color COLOR_BOTTOM_REACHED = new Color(255, 193, 7); // Leaf level active (Yellow)
    private static final Color COLOR_UPWARDS = new Color(40, 167, 69);      // Upward traversal (Green)

    private final int maxDepth;
    private final Map<String, Color> nodeStatusMap = new ConcurrentHashMap<>();
    private final TreePanel treePanel;
    private volatile boolean running = true;

    public BinaryTreeInfiniteMultithreading(int maxDepth) {
        this.maxDepth = maxDepth;
        setTitle("Infinite Binary Tree Multithreading Simulation");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        treePanel = new TreePanel();
        add(new JScrollPane(treePanel), BorderLayout.CENTER);

        // Control panel for stopping the loop
        JPanel controlPanel = new JPanel();
        JButton stopButton = new JButton("Stop Simulation");
        stopButton.addActionListener(e -> running = false);
        controlPanel.add(stopButton);
        add(controlPanel, BorderLayout.SOUTH);

        // Start the continuous simulation loop on a background thread
        new Thread(this::runInfiniteSimulation, "Main-Control-Thread").start();
    }

    private void runInfiniteSimulation() {
        int iteration = 1;
        while (running) {
            System.out.printf("--- Starting Cycle %d (Top -> Bottom -> Top) ---%n", iteration++);
            
            // Clear status map for a fresh cycle
            nodeStatusMap.clear();
            treePanel.repaint();
            sleepUnchecked(500);

            // Execute the root node thread (this blocks until bottom-to-top traversal completes)
            executeNodeThread(0, "1");

            // Pause briefly between cycles
            sleepUnchecked(1000);
        }
        System.out.println("Simulation stopped.");
    }

    private void executeNodeThread(int currentDepth, String nodeId) {
        if (!running) return;

        // PHASE 1: TOP-TO-BOTTOM (Downward propagation)
        nodeStatusMap.put(nodeId, COLOR_DOWNWARDS);
        treePanel.repaint();
        sleepUnchecked(400);

        if (currentDepth < maxDepth) {
            String leftId = nodeId + ".1";
            String rightId = nodeId + ".2";

            // CountDownLatch forces the parent thread to wait until both children complete Phase 2
            CountDownLatch childrenLatch = new CountDownLatch(2);

            Thread leftThread = new Thread(() -> {
                executeNodeThread(currentDepth + 1, leftId);
                childrenLatch.countDown();
            }, "Node-" + leftId);

            Thread rightThread = new Thread(() -> {
                executeNodeThread(currentDepth + 1, rightId);
                childrenLatch.countDown();
            }, "Node-" + rightId);

            // Fork parallel children
            leftThread.start();
            rightThread.start();

            // Wait for both child subtrees to traverse back up
            try {
                childrenLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        } else {
            // Leaf node reached: highlight transition
            nodeStatusMap.put(nodeId, COLOR_BOTTOM_REACHED);
            treePanel.repaint();
            sleepUnchecked(500);
        }

        if (!running) return;

        // PHASE 2: BOTTOM-TO-TOP (Upward aggregation)
        nodeStatusMap.put(nodeId, COLOR_UPWARDS);
        treePanel.repaint();
        sleepUnchecked(400);
    }

    private void sleepUnchecked(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Canvas panel for rendering the tree structure and node colors
    private class TreePanel extends JPanel {
        public TreePanel() {
            setBackground(new Color(30, 32, 40));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawTreeLegend(g2);
            drawNode(g2, 0, "1", getWidth() / 2, 80, getWidth() / 4);
        }

        private void drawTreeLegend(Graphics2D g2) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            
            g2.setColor(COLOR_DOWNWARDS);
            g2.fillRect(20, 20, 15, 15);
            g2.setColor(Color.WHITE);
            g2.drawString("Top -> Bottom (Forking Down)", 42, 33);

            g2.setColor(COLOR_BOTTOM_REACHED);
            g2.fillRect(260, 20, 15, 15);
            g2.setColor(Color.WHITE);
            g2.drawString("Leaf Node (Turnaround)", 282, 33);

            g2.setColor(COLOR_UPWARDS);
            g2.fillRect(480, 20, 15, 15);
            g2.setColor(Color.WHITE);
            g2.drawString("Bottom -> Top (Joining Up)", 502, 33);
        }

        private void drawNode(Graphics2D g2, int currentDepth, String nodeId, int x, int y, int xOffset) {
            if (currentDepth > maxDepth) return;

            int nextY = y + 75;

            // Draw branch lines to child nodes
            if (currentDepth < maxDepth) {
                g2.setColor(new Color(100, 110, 125));
                g2.setStroke(new BasicStroke(2));

                g2.drawLine(x, y, x - xOffset, nextY);
                g2.drawLine(x, y, x + xOffset, nextY);

                drawNode(g2, currentDepth + 1, nodeId + ".1", x - xOffset, nextY, Math.max(xOffset / 2, 20));
                drawNode(g2, currentDepth + 1, nodeId + ".2", x + xOffset, nextY, Math.max(xOffset / 2, 20));
            }

            // Draw node circle
            int radius = 18;
            Color statusColor = nodeStatusMap.getOrDefault(nodeId, COLOR_DEFAULT);
            g2.setColor(statusColor);
            g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - radius, y - radius, radius * 2, radius * 2);

            // Draw text label
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(nodeId);
            g2.drawString(nodeId, x - (textWidth / 2), y + 4);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String input = JOptionPane.showInputDialog(
                    null,
                    "Enter Binary Tree Depth (e.g., 1 to 4):",
                    "Infinite Multithreading Visualizer",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input == null || input.trim().isEmpty()) {
                System.exit(0);
            }

            try {
                int depth = Integer.parseInt(input.trim());
                if (depth < 0) {
                    JOptionPane.showMessageDialog(null, "Depth must be non-negative.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

                BinaryTreeInfiniteMultithreading frame = new BinaryTreeInfiniteMultithreading(depth);
                frame.setVisible(true);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid integer input.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
    }
}