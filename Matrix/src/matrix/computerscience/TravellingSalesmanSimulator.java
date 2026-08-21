package matrix.computerscience;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TravellingSalesmanSimulator extends JFrame {

    private int numCities;
    private List<Point> cities = new ArrayList<>();
    private List<Integer> currentRoute = new ArrayList<>();
    private double bestDistance = Double.MAX_VALUE;
    
    // Index within currentRoute representing the salesman's current location
    private int currentSalesmanIndex = 0;

    // Control flags for threads
    private volatile boolean isRunning = true;
    private volatile boolean isPaused = false;

    // Precision timing variables
    private long accumulatedNanos = 0;
    private long lastStartTimeNanos = 0;

    // UI Elements
    private JPanel canvasPanel;
    private JLabel chronoLabel;
    private JLabel distanceLabel;
    private JButton pauseResumeButton;

    public TravellingSalesmanSimulator(int numCities) {
        this.numCities = numCities;

        setTitle("TSP Simulator with High-Precision Chronograph");
        setSize(950, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initCitiesAndRoute();
        initUI();

        // Start execution timer and solver threads
        lastStartTimeNanos = System.nanoTime();
        startChronographThread();
        startTSPSolverThread();
    }

    private void initCitiesAndRoute() {
        Random random = new Random();
        int margin = 60;
        int width = 850 - 2 * margin;
        int height = 550 - 2 * margin;

        for (int i = 0; i < numCities; i++) {
            int x = margin + random.nextInt(width);
            int y = margin + random.nextInt(height);
            cities.add(new Point(x, y));
            currentRoute.add(i);
        }

        // Shuffle initial tour route
        Collections.shuffle(currentRoute);
        bestDistance = calculateTotalDistance(currentRoute);
    }

    private double calculateTotalDistance(List<Integer> route) {
        double dist = 0.0;
        for (int i = 0; i < route.size(); i++) {
            Point p1 = cities.get(route.get(i));
            Point p2 = cities.get(route.get((i + 1) % route.size()));
            dist += p1.distance(p2);
        }
        return dist;
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header Section (Chronograph & Statistics)
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(new Color(25, 25, 25));

        chronoLabel = new JLabel("", SwingConstants.CENTER);
        chronoLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
        chronoLabel.setForeground(Color.CYAN);

        distanceLabel = new JLabel("Best Distance: " + String.format("%.2f", bestDistance), SwingConstants.CENTER);
        distanceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        distanceLabel.setForeground(Color.WHITE);

        topPanel.add(chronoLabel);
        topPanel.add(distanceLabel);

        // Simulation Canvas
        canvasPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw tour route paths
                g2.setColor(new Color(180, 180, 180));
                g2.setStroke(new BasicStroke(2.0f));
                for (int i = 0; i < currentRoute.size(); i++) {
                    Point p1 = cities.get(currentRoute.get(i));
                    Point p2 = cities.get(currentRoute.get((i + 1) % currentRoute.size()));
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                }

                // Identify start city and current active salesman position
                int startCityIndex = 0; // City 0 is defined as the start city
                int currentSalesmanCityIndex = currentRoute.get(currentSalesmanIndex);

                // Draw city nodes
                for (int i = 0; i < cities.size(); i++) {
                    Point p = cities.get(i);
                    int radius = 16;
                    int x = p.x - radius / 2;
                    int y = p.y - radius / 2;

                    // Color assignment logic
                    if (i == currentSalesmanCityIndex) {
                        g2.setColor(Color.BLUE); // Current location of salesman
                    } else if (i == startCityIndex) {
                        g2.setColor(Color.RED); // Start city
                    } else {
                        g2.setColor(new Color(34, 139, 34)); // Rest of the cities (Green)
                    }

                    g2.fillOval(x, y, radius, radius);
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawOval(x, y, radius, radius);

                    // City labels
                    g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                    g2.drawString("C" + (i + 1), p.x + 10, p.y + 4);
                }
            }
        };
        canvasPanel.setBackground(new Color(245, 248, 250));

        // Control Panel
        JPanel bottomPanel = new JPanel();
        pauseResumeButton = new JButton("Pause");
        pauseResumeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        pauseResumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePauseResume();
            }
        });
        bottomPanel.add(pauseResumeButton);

        add(topPanel, BorderLayout.NORTH);
        add(canvasPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private synchronized void togglePauseResume() {
        if (isPaused) {
            isPaused = false;
            lastStartTimeNanos = System.nanoTime();
            pauseResumeButton.setText("Pause");
            notifyAll(); // Resume active thread loops
        } else {
            isPaused = true;
            accumulatedNanos += (System.nanoTime() - lastStartTimeNanos);
            pauseResumeButton.setText("Resume");
        }
    }

    private void startChronographThread() {
        Thread timerThread = new Thread(() -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            while (isRunning) {
                long currentElapsedNanos = accumulatedNanos;
                if (!isPaused) {
                    currentElapsedNanos += (System.nanoTime() - lastStartTimeNanos);
                }

                long totalMillis = currentElapsedNanos / 1_000_000;
                long totalSeconds = totalMillis / 1000;

                long hours = totalSeconds / 3600;
                long minutes = (totalSeconds % 3600) / 60;
                long seconds = totalSeconds % 60;
                long millis = totalMillis % 1000;
                long nanos = currentElapsedNanos % 1_000_000;

                String dateStr = dateFormat.format(new Date());
                String chronoText = String.format(
                        "Date: %s | Elapsed: %02dh %02dm %02ds %03dms %06dns",
                        dateStr, hours, minutes, seconds, millis, nanos
                );

                SwingUtilities.invokeLater(() -> chronoLabel.setText(chronoText));

                try {
                    Thread.sleep(25); // ~40 FPS refresh cycle for timer display
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    private void startTSPSolverThread() {
        Thread solverThread = new Thread(() -> {
            Random random = new Random();
            while (isRunning) {
                synchronized (this) {
                    while (isPaused) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }

                // Advance salesman along current optimal tour path
                currentSalesmanIndex = (currentSalesmanIndex + 1) % numCities;

                // 2-Opt Optimization step to attempt route improvement
                List<Integer> newRoute = new ArrayList<>(currentRoute);
                int i = random.nextInt(numCities);
                int j = random.nextInt(numCities);
                if (i != j) {
                    int start = Math.min(i, j);
                    int end = Math.max(i, j);
                    Collections.reverse(newRoute.subList(start, end + 1));

                    double newDist = calculateTotalDistance(newRoute);
                    if (newDist < bestDistance) {
                        bestDistance = newDist;
                        currentRoute = newRoute;
                    }
                }

                // Repaint UI state
                SwingUtilities.invokeLater(() -> {
                    distanceLabel.setText("Best Distance: " + String.format("%.2f", bestDistance));
                    canvasPanel.repaint();
                });

                try {
                    Thread.sleep(300); // Step delay for visualization speed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        solverThread.setDaemon(true);
        solverThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String input = JOptionPane.showInputDialog(
                    null,
                    "Enter the number of cities for TSP:",
                    "TSP Simulator Setup",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input != null && !input.trim().isEmpty()) {
                try {
                    int citiesCount = Integer.parseInt(input.trim());
                    if (citiesCount < 3) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Please enter a number greater than or equal to 3.",
                                "Invalid Input",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    TravellingSalesmanSimulator app = new TravellingSalesmanSimulator(citiesCount);
                    app.setVisible(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter a valid integer.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }
}