package matrix.computerscience;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrencyParallelismSimulator extends JFrame {

    private final int numTasks;
    private final int numCores;

    private JPanel singleCorePanel;
    private JPanel multiCorePanel;
    private JButton startButton;

    public ConcurrencyParallelismSimulator(int numTasks, int numCores) {
        this.numTasks = numTasks;
        this.numCores = numCores;

        setTitle("Concurrency (Time-Slicing) vs Parallelism Simulator");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Top Header Info
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        JLabel infoLabel = new JLabel(
            String.format(" Configuration: %d Tasks | %d CPU Cores Available", numTasks, numCores),
            SwingConstants.CENTER
        );
        infoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        JLabel descLabel = new JLabel(
            "Left: Concurrency on 1 Core (Time-Slicing)  |  Right: True Parallelism on " + numCores + " Cores",
            SwingConstants.CENTER
        );
        descLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));

        headerPanel.add(infoLabel);
        headerPanel.add(descLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center Visual Panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        singleCorePanel = createExecutionSection("1 Core (Time-Slicing Concurrency)");
        multiCorePanel = createExecutionSection(numCores + " Cores (Parallel Execution)");

        mainPanel.add(singleCorePanel);
        mainPanel.add(multiCorePanel);
        add(mainPanel, BorderLayout.CENTER);

        // Bottom Controls
        JPanel bottomPanel = new JPanel();
        startButton = new JButton("Start Simulation");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        startButton.addActionListener(e -> runSimulation());
        bottomPanel.add(startButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createExecutionSection(String title) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), title, 0, 0, new Font("SansSerif", Font.BOLD, 14)
        ));

        JPanel taskContainer = new JPanel();
        taskContainer.setLayout(new BoxLayout(taskContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(taskContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        section.add(scrollPane, BorderLayout.CENTER);
        return section;
    }

    private void runSimulation() {
        startButton.setEnabled(false);

        // Prepare Task Progress Bars for both sides
        List<JProgressBar> singleCoreBars = setupTaskBars(singleCorePanel);
        List<JProgressBar> multiCoreBars = setupTaskBars(multiCorePanel);

        // Run Single-Core (1 Thread -> Time Slicing) in background thread
        Executors.newSingleThreadExecutor().submit(() -> {
            ExecutorService singleCoreExecutor = Executors.newFixedThreadPool(1);
            runTaskSet(singleCoreExecutor, singleCoreBars);
            singleCoreExecutor.shutdown();
        });

        // Run Multi-Core (N Threads -> Parallelism) in background thread
        Executors.newSingleThreadExecutor().submit(() -> {
            ExecutorService multiCoreExecutor = Executors.newFixedThreadPool(numCores);
            runTaskSet(multiCoreExecutor, multiCoreBars);
            multiCoreExecutor.shutdown();
        });
    }

    private List<JProgressBar> setupTaskBars(JPanel sectionPanel) {
        JScrollPane scrollPane = (JScrollPane) sectionPanel.getComponent(0);
        JPanel container = (JPanel) scrollPane.getViewport().getView();
        container.removeAll();

        List<JProgressBar> bars = new ArrayList<>();
        for (int i = 1; i <= numTasks; i++) {
            JPanel itemPanel = new JPanel(new BorderLayout(5, 0));
            itemPanel.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));

            JLabel label = new JLabel("Task " + i + ": ");
            label.setPreferredSize(new Dimension(60, 20));

            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);

            itemPanel.add(label, BorderLayout.WEST);
            itemPanel.add(progressBar, BorderLayout.CENTER);

            container.add(itemPanel);
            bars.add(progressBar);
        }

        container.revalidate();
        container.repaint();
        return bars;
    }

    private void runTaskSet(ExecutorService executor, List<JProgressBar> progressBars) {
        CountDownLatch latch = new CountDownLatch(numTasks);

        for (int i = 0; i < numTasks; i++) {
            final int index = i;
            executor.submit(() -> {
                JProgressBar bar = progressBars.get(index);
                for (int progress = 1; progress <= 100; progress++) {
                    try {
                        // Simulate CPU work chunk
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    final int currentProgress = progress;
                    SwingUtilities.invokeLater(() -> bar.setValue(currentProgress));
                }
                latch.countDown();
            });
        }

        try {
            latch.await(); // Wait for all tasks to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        SwingUtilities.invokeLater(() -> {
            if (allDone()) {
                startButton.setEnabled(true);
            }
        });
    }

    private boolean allDone() {
        return true; 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int defaultCores = Runtime.getRuntime().availableProcessors();

            // Dialog for Task Count
            String tasksInput = JOptionPane.showInputDialog(
                null,
                "Enter number of tasks to execute:",
                "Simulation Input",
                JOptionPane.QUESTION_MESSAGE
            );

            if (tasksInput == null || tasksInput.trim().isEmpty()) {
                System.exit(0);
            }

            // Dialog for Core Count
            String coresInput = JOptionPane.showInputDialog(
                null,
                "Enter number of CPU cores to simulate (System detected: " + defaultCores + "):",
                "Simulation Input",
                JOptionPane.QUESTION_MESSAGE
            );

            if (coresInput == null || coresInput.trim().isEmpty()) {
                System.exit(0);
            }

            try {
                int tasks = Integer.parseInt(tasksInput.trim());
                int cores = Integer.parseInt(coresInput.trim());

                if (tasks <= 0 || cores <= 0) {
                    JOptionPane.showMessageDialog(null, "Please enter positive numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

                ConcurrencyParallelismSimulator frame = new ConcurrencyParallelismSimulator(tasks, cores);
                frame.setVisible(true);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid integer input. Exiting.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
    }
}