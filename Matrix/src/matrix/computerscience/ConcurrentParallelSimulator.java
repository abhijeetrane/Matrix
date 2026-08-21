package matrix.computerscience;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentParallelSimulator extends JFrame {

    private final int numTasks;
    private final int numCores;

    private JPanel coresPanel;
    private JButton startButton;
    private JLabel statusLabel;

    public ConcurrentParallelSimulator(int numTasks, int numCores) {
        this.numTasks = numTasks;
        this.numCores = numCores;

        setTitle("Combination of Concurrency (Time-Slicing) & Parallelism Simulator");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Header Section
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel(
            String.format(" Simulation Setup: %d Tasks distributed over %d CPU Core Worker Threads", numTasks, numCores),
            SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        statusLabel = new JLabel(
            "Click 'Start Simulation' to visualize parallel execution with thread time-slicing.",
            SwingConstants.CENTER
        );
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));

        headerPanel.add(titleLabel);
        headerPanel.add(statusLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Grid of Cores (Parallel Containers)
        int cols = Math.min(numCores, 4);
        int rows = (int) Math.ceil((double) numCores / cols);
        coresPanel = new JPanel(new GridLayout(rows, cols, 15, 15));
        coresPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JScrollPane mainScrollPane = new JScrollPane(coresPanel);
        add(mainScrollPane, BorderLayout.CENTER);

        // Bottom Controls
        JPanel bottomPanel = new JPanel();
        startButton = new JButton("Start Simulation");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        startButton.addActionListener(e -> startSimulation());
        bottomPanel.add(startButton);
        add(bottomPanel, BorderLayout.SOUTH);

        buildCorePanels();
    }

    private void buildCorePanels() {
        coresPanel.removeAll();
        for (int i = 1; i <= numCores; i++) {
            JPanel coreBox = new JPanel(new BorderLayout());
            coreBox.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "CPU Core Thread #" + i, 0, 0, new Font("SansSerif", Font.BOLD, 13)
            ));

            JPanel taskListPanel = new JPanel();
            taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));

            JScrollPane scrollPane = new JScrollPane(taskListPanel);
            coreBox.add(scrollPane, BorderLayout.CENTER);

            coresPanel.add(coreBox);
        }
        coresPanel.revalidate();
        coresPanel.repaint();
    }

    private void startSimulation() {
        startButton.setEnabled(false);
        statusLabel.setText("Simulation Running...");

        buildCorePanels();

        // Create Task progress bars and allocate them across core panels
        List<JProgressBar> taskBars = new ArrayList<>();
        List<JPanel> coreContainers = new ArrayList<>();

        for (Component comp : coresPanel.getComponents()) {
            JPanel coreBox = (JPanel) comp;
            JScrollPane scrollPane = (JScrollPane) coreBox.getComponent(0);
            JPanel taskListPanel = (JPanel) scrollPane.getViewport().getView();
            coreContainers.add(taskListPanel);
        }

        for (int i = 0; i < numTasks; i++) {
            int assignedCoreIndex = i % numCores;
            JPanel targetCorePanel = coreContainers.get(assignedCoreIndex);

            JPanel itemPanel = new JPanel(new BorderLayout(5, 0));
            itemPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

            JLabel label = new JLabel("Task " + (i + 1));
            label.setPreferredSize(new Dimension(50, 20));

            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);

            itemPanel.add(label, BorderLayout.WEST);
            itemPanel.add(progressBar, BorderLayout.CENTER);

            targetCorePanel.add(itemPanel);
            taskBars.add(progressBar);
        }

        coresPanel.revalidate();
        coresPanel.repaint();

        // Run simulation using an ExecutorService with numCores threads (Parallelism)
        Executors.newSingleThreadExecutor().submit(() -> {
            ExecutorService executor = Executors.newFixedThreadPool(numCores);
            CountDownLatch latch = new CountDownLatch(numTasks);

            long startTime = System.currentTimeMillis();

            for (int i = 0; i < numTasks; i++) {
                final int taskIndex = i;
                executor.submit(() -> {
                    JProgressBar progressBar = taskBars.get(taskIndex);

                    // Work loop with small time-slicing chunks
                    for (int progress = 1; progress <= 100; progress++) {
                        try {
                            // Simulates CPU execution slice on thread
                            Thread.sleep(25);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                        final int currentProgress = progress;
                        SwingUtilities.invokeLater(() -> progressBar.setValue(currentProgress));
                    }
                    latch.countDown();
                });
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long totalTime = System.currentTimeMillis() - startTime;
            executor.shutdown();

            SwingUtilities.invokeLater(() -> {
                statusLabel.setText(String.format("Completed %d tasks in %d ms across %d parallel cores.", numTasks, totalTime, numCores));
                startButton.setEnabled(true);
            });
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int detectedCores = Runtime.getRuntime().availableProcessors();

            String tasksInput = JOptionPane.showInputDialog(
                null,
                "Enter total number of tasks:",
                "Simulation Setup",
                JOptionPane.QUESTION_MESSAGE
            );

            if (tasksInput == null || tasksInput.trim().isEmpty()) {
                System.exit(0);
            }

            String coresInput = JOptionPane.showInputDialog(
                null,
                "Enter number of CPU cores/threads (System detected: " + detectedCores + "):",
                "Simulation Setup",
                JOptionPane.QUESTION_MESSAGE
            );

            if (coresInput == null || coresInput.trim().isEmpty()) {
                System.exit(0);
            }

            try {
                int tasks = Integer.parseInt(tasksInput.trim());
                int cores = Integer.parseInt(coresInput.trim());

                if (tasks <= 0 || cores <= 0) {
                    JOptionPane.showMessageDialog(null, "Task and Core counts must be positive integers.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

                ConcurrentParallelSimulator frame = new ConcurrentParallelSimulator(tasks, cores);
                frame.setVisible(true);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid integer input provided. Exiting.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
    }
}