package matrix.computerscience;
import javax.swing.*;
import java.awt.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class BinaryTreeMultithreadingApp extends JFrame {

    // Status colors
    private static final Color COLOR_DEFAULT = new Color(70, 80, 95);       // Idle / Unvisited
    private static final Color COLOR_DOWNWARDS = new Color(220, 53, 69);     // Downward traversal (Red)
    private static final Color COLOR_BOTTOM_REACHED = new Color(255, 193, 7); // Leaf level active (Yellow)
    private static final Color COLOR_UPWARDS = new Color(40, 167, 69);      // Upward traversal (Green)

    private final int maxDepth;
    private final Map<String, Color> nodeStatusMap = new ConcurrentHashMap<>();
    private final TreePanel treePanel;

    // Control flags for Pause / Resume / Stop
    private volatile boolean isRunning = true;
    private volatile boolean isPaused = false;
    private final Object pauseLock = new Object();

    // Chronograph label
    private final JLabel clockLabel = new JLabel();

    public BinaryTreeMultithreadingApp(int maxDepth) {
        this.maxDepth = maxDepth;
        setTitle("Binary Tree Multithreading & Chronograph Simulation");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel: Chronograph Clock
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(20, 22, 28));
        clockLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
        clockLabel.setForeground(new Color(0, 230, 255));
        topPanel.add(clockLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Tree Visualizer
        treePanel = new TreePanel();
        add(new JScrollPane(treePanel), BorderLayout.CENTER);

        // Bottom Panel: Controls (Pause / Resume / Stop)
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(25, 27, 33));

        JButton pauseResumeBtn = new JButton("Pause");
        pauseResumeBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        pauseResumeBtn.addActionListener(e -> {
            if (isPaused) {
                resumeSimulation();
                pauseResumeBtn.setText("Pause");
            } else {
                pauseSimulation();
                pauseResumeBtn.setText("Resume");
            }
        });

        controlPanel.add(pauseResumeBtn);
        add(controlPanel, BorderLayout.SOUTH);

        // Start High-Precision Chronograph Timer (updates ~1000 times/sec)
        startChronographTimer();

        // Start Infinite Multithreaded Tree Simulation Thread
        new Thread(this::runInfiniteSimulation, "Tree-Simulation-Control").start();
    }

    // High-precision chronograph displaying Date, Hours, Minutes, Seconds, Milliseconds, and Nanoseconds
    private void startChronographTimer() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        
        Timer timer = new Timer(1, e -> {
            ZonedDateTime now = ZonedDateTime.now();
            String formattedDate = now.format(formatter);
            long nanoOfSecond = now.getNano();
            long remainingNanos = nanoOfSecond % 1_000_000;

            String clockText = String.format(" CHRONOGRAPH: %s | Nanos: %06d ",
                    formattedDate, remainingNanos);
            clockLabel.setText(clockText);
        });
        timer.start();
    }

    // Infinite loop cycling Top -> Bottom -> Top
    private void runInfiniteSimulation() {
        int cycleCount = 1;
        while (isRunning) {
            checkPauseState();

            // Clear visual state for new cycle
            nodeStatusMap.clear();
            treePanel.repaint();
            sleepInterruptible(400);

            // Execute root thread (blocks until complete bottom-to-top traversal)
            executeNodeThread(0, "1");

            cycleCount++;
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

            try {
                childLatch.await(); // Parent blocks until both children complete Phase 2
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        } else {
            // Leaf node reached
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

    // Handles pause/resume synchronization across execution threads
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

    private void pauseSimulation() {
        synchronized (pauseLock) {
            isPaused = true;
        }
    }

    private void resumeSimulation() {
        synchronized (pauseLock) {
            isPaused = false;
            pauseLock.notifyAll();
        }
    }

    private void sleepInterruptible(long millis) {
        long slept = 0;
        long interval = 50;
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

    // Canvas panel for tree rendering
    private class TreePanel extends JPanel {
        public TreePanel() {
            setBackground(new Color(28, 30, 38));
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

            // Draw branch lines to children
            if (currentDepth < maxDepth) {
                g2.setColor(new Color(90, 100, 115));
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

            // Draw label
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
                    "Binary Tree Multithreading Visualizer",
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

                BinaryTreeMultithreadingApp app = new BinaryTreeMultithreadingApp(depth);
                app.setVisible(true);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid integer input.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
    }
}