package matrix.computerscience;

import javax.swing.*;
import java.awt.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class BinaryTreeThreadSimulation extends JFrame {

    // Visual indicators for traversal stages
    private static final Color COLOR_DEFAULT = new Color(60, 68, 81);        // Idle / Unvisited
    private static final Color COLOR_DOWNWARDS = new Color(230, 57, 70);      // Top -> Bottom Phase (Red)
    private static final Color COLOR_BOTTOM_REACHED = new Color(255, 183, 3); // Leaf Turnaround (Yellow)
    private static final Color COLOR_UPWARDS = new Color(42, 157, 143);      // Bottom -> Top Phase (Green)

    private final int maxDepth;
    private final Map<String, Color> nodeStatusMap = new ConcurrentHashMap<>();
    private final TreePanel treePanel;
    private final JLabel clockLabel = new JLabel();

    // Synchronization & Control Flags for Traversal and Chronograph
    private volatile boolean isRunning = true;
    private volatile boolean isPaused = false;
    private final Object pauseLock = new Object();

    public BinaryTreeThreadSimulation(int maxDepth) {
        this.maxDepth = maxDepth;
        setTitle("Binary Tree Multithreading Simulation & Synchronized Chronograph");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel: Chronograph Clock
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(18, 20, 26));
        clockLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
        clockLabel.setForeground(new Color(0, 220, 255));
        topPanel.add(clockLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Tree Visualization Canvas
        treePanel = new TreePanel();
        add(new JScrollPane(treePanel), BorderLayout.CENTER);

        // Bottom Panel: Control Buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(24, 26, 33));

        JButton pauseResumeBtn = new JButton("Pause");
        pauseResumeBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        pauseResumeBtn.setFocusPainted(false);
        
        pauseResumeBtn.addActionListener(e -> {
            if (isPaused) {
                resumeSimulationAndClock();
                pauseResumeBtn.setText("Pause");
            } else {
                pauseSimulationAndClock();
                pauseResumeBtn.setText("Resume");
            }
        });

        controlPanel.add(pauseResumeBtn);
        add(controlPanel, BorderLayout.SOUTH);

        // Launch Synchronized Chronograph Engine
        startChronograph();

        // Launch Infinite Binary Tree Multithreading Engine
        new Thread(this::runInfiniteTraversalLoop, "Master-Traversal-Thread").start();
    }

    // High-Precision Chronograph: Displays Date, Hours, Minutes, Seconds, Milliseconds, and Nanoseconds
    private void startChronograph() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        // High-frequency UI update loop (~1 ms refresh rate)
        Timer timer = new Timer(1, e -> {
            if (!isPaused) {
                ZonedDateTime now = ZonedDateTime.now();
                String formattedDate = now.format(formatter);
                long nanoOfSecond = now.getNano();
                long remainingNanos = nanoOfSecond % 1_000_000;

                String clockText = String.format(" CHRONOGRAPH: %s | Nanos: %06d ",
                        formattedDate, remainingNanos);
                clockLabel.setText(clockText);
            }
        });
        timer.start();
    }

    // Infinite Loop: Continuous execution of Top -> Bottom -> Top multithreaded traversal
    private void runInfiniteTraversalLoop() {
        while (isRunning) {
            checkPauseState();

            // Reset canvas state for a new iteration
            nodeStatusMap.clear();
            treePanel.repaint();
            sleepInterruptible(400);

            // Execute Root Thread (Blocks until entire tree tree-join completes bottom-up)
            executeNodeThread(0, "1");

            sleepInterruptible(800);
        }
    }

    private void executeNodeThread(int currentDepth, String nodeId) {
        if (!isRunning) return;
        checkPauseState();

        // PHASE 1: TOP-TO-BOTTOM (Forking Downwards)
        nodeStatusMap.put(nodeId, COLOR_DOWNWARDS);
        treePanel.repaint();
        sleepInterruptible(350);

        if (currentDepth < maxDepth) {
            String leftId = nodeId + ".1";
            String rightId = nodeId + ".2";

            CountDownLatch childLatch = new CountDownLatch(2);

            // Fork parallel worker threads for left and right branches
            Thread leftThread = new Thread(() -> {
                executeNodeThread(currentDepth + 1, leftId);
                childLatch.countDown();
            }, "NodeThread-" + leftId);

            Thread rightThread = new Thread(() -> {
                executeNodeThread(currentDepth + 1, rightId);
                childLatch.countDown();
            }, "NodeThread-" + rightId);

            leftThread.start();
            rightThread.start();

            // Wait for both subtrees to complete their upward traversal phase
            try {
                childLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        } else {
            // Reached leaf node (turnaround point)
            nodeStatusMap.put(nodeId, COLOR_BOTTOM_REACHED);
            treePanel.repaint();
            sleepInterruptible(450);
        }

        if (!isRunning) return;
        checkPauseState();

        // PHASE 2: BOTTOM-TO-TOP (Joining Upwards)
        nodeStatusMap.put(nodeId, COLOR_UPWARDS);
        treePanel.repaint();
        sleepInterruptible(350);
    }

    // Monitor synchronization for Pause / Resume state controls
    private void checkPauseState() {
        synchronized (pauseLock) {
            while (isPaused && isRunning) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void pauseSimulationAndClock() {
        synchronized (pauseLock) {
            isPaused = true;
        }
    }

    private void resumeSimulationAndClock() {
        synchronized (pauseLock) {
            isPaused = false;
            pauseLock.notifyAll();
        }
    }

    private void sleepInterruptible(long millis) {
        long slept = 0;
        long interval = 40;
        while (slept < millis && isRunning) {
            checkPauseState();
            try {
                Thread.sleep(Math.min(interval, millis - slept));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            slept += interval;
        }
    }

    // Canvas panel for rendering the tree structure and node colors
    private class TreePanel extends JPanel {
        public TreePanel() {
            setBackground(new Color(25, 28, 36));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawLegend(g2);
            drawNode(g2, 0, "1", getWidth() / 2, 80, getWidth() / 4);
        }

        private void drawLegend(Graphics2D g2) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));

            g2.setColor(COLOR_DOWNWARDS);
            g2.fillRect(20, 20, 14, 14);
            g2.setColor(Color.WHITE);
            g2.drawString("Top -> Bottom (Forking Down)", 40, 32);

            g2.setColor(COLOR_BOTTOM_REACHED);
            g2.fillRect(260, 20, 14, 14);
            g2.setColor(Color.WHITE);
            g2.drawString("Leaf Node (Turnaround)", 280, 32);

            g2.setColor(COLOR_UPWARDS);
            g2.fillRect(470, 20, 14, 14);
            g2.setColor(Color.WHITE);
            g2.drawString("Bottom -> Top (Joining Up)", 490, 32);
        }

        private void drawNode(Graphics2D g2, int currentDepth, String nodeId, int x, int y, int xOffset) {
            if (currentDepth > maxDepth) return;

            int nextY = y + 75;

            // Draw connecting edges
            if (currentDepth < maxDepth) {
                g2.setColor(new Color(80, 90, 105));
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

            // Render node ID label
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
                    "Multithreading Simulation Setup",
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

                BinaryTreeThreadSimulation app = new BinaryTreeThreadSimulation(depth);
                app.setVisible(true);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
    }
}