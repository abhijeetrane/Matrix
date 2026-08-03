import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

public class GraphColoringVisualizer extends JFrame {

    private int numVertices = 5;
    private int numColors = 3;

    // Graph Data Structure
    private boolean[][] adjMatrix;
    private int[] colorAssignment; // 0 = uncolored, 1..m = assigned color

    // Control & Threading Flags
    private Thread solverThread;
    private volatile boolean isRunning = false;
    private volatile boolean isPaused = false;
    private volatile boolean stopRequested = false;

    // Metrics
    private long moveCount = 0;
    private long startTime = 0;
    private long elapsedTime = 0;
    private Timer chronographTimer;

    // GUI Components
    private JLabel movesLabel;
    private JLabel timerLabel;
    private JLabel statusLabel;
    private JButton startBtn;
    private JButton pauseBtn;
    private JButton stopBtn;
    private JButton newGraphBtn;
    private GraphPanel graphPanel;

    // Color Palette for rendering vertices (up to 12 distinct colors)
    private static final Color[] COLOR_PALETTE = {
        Color.GRAY,         // 0: Uncolored
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.ORANGE,
        Color.MAGENTA,
        Color.CYAN,
        Color.PINK,
        Color.YELLOW,
        new Color(128, 0, 128),  // Purple
        new Color(0, 128, 128),  // Teal
        new Color(139, 69, 19)   // Brown
    };

    public GraphColoringVisualizer() {
        setTitle("Graph Coloring Problem Visualizer (Backtracking)");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        promptInputAndSetup();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Top Status Panel (Top-Left Moves, Top-Right Chronograph)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topPanel.setBackground(new Color(240, 243, 246));

        movesLabel = new JLabel("Moves: 0");
        movesLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        movesLabel.setForeground(new Color(40, 40, 40));

        timerLabel = new JLabel("Time: 00:00:000");
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        timerLabel.setForeground(new Color(20, 100, 180));

        topPanel.add(movesLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Center Graph Display Panel
        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);

        // Bottom Controls Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        statusLabel = new JLabel("Status: Ready");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        startBtn = new JButton("Start Solving");
        pauseBtn = new JButton("Pause");
        stopBtn = new JButton("Stop / Reset");
        newGraphBtn = new JButton("New Graph Settings");

        pauseBtn.setEnabled(false);
        stopBtn.setEnabled(false);

        startBtn.addActionListener(e -> startSolving());
        pauseBtn.addActionListener(e -> togglePause());
        stopBtn.addActionListener(e -> stopSolving());
        newGraphBtn.addActionListener(e -> promptInputAndSetup());

        bottomPanel.add(startBtn);
        bottomPanel.add(pauseBtn);
        bottomPanel.add(stopBtn);
        bottomPanel.add(newGraphBtn);
        bottomPanel.add(statusLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        // Setup Swing Timer for Chronograph Update
        chronographTimer = new Timer(10, e -> {
            if (isRunning && !isPaused) {
                elapsedTime = System.currentTimeMillis() - startTime;
                updateTimerDisplay(elapsedTime);
            }
        });
    }

    private void promptInputAndSetup() {
        if (isRunning) {
            stopSolving();
        }

        JTextField nInput = new JTextField(String.valueOf(numVertices), 5);
        JTextField mInput = new JTextField(String.valueOf(numColors), 5);

        JPanel dialogPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        dialogPanel.add(new JLabel("Number of Vertices (n):"));
        dialogPanel.add(nInput);
        dialogPanel.add(new JLabel("Number of Colors (m):"));
        dialogPanel.add(mInput);

        int result = JOptionPane.showConfirmDialog(
            this, dialogPanel, "Graph Coloring Input Parameters",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                int parsedN = Integer.parseInt(nInput.getText().trim());
                int parsedM = Integer.parseInt(mInput.getText().trim());

                if (parsedN <= 0 || parsedM <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter positive integers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                this.numVertices = parsedN;
                this.numColors = parsedM;
                generateRandomGraph();
                resetMetrics();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generateRandomGraph() {
        adjMatrix = new boolean[numVertices][numVertices];
        colorAssignment = new int[numVertices];
        Random rand = new Random();

        // Generate an undirected graph with ~35% edge probability
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                boolean edge = rand.nextDouble() < 0.35;
                adjMatrix[i][j] = edge;
                adjMatrix[j][i] = edge;
            }
        }
        graphPanel.repaint();
    }

    private void resetMetrics() {
        moveCount = 0;
        elapsedTime = 0;
        movesLabel.setText("Moves: 0");
        updateTimerDisplay(0);
        statusLabel.setText("Status: Ready (n=" + numVertices + ", m=" + numColors + ")");
        Arrays.fill(colorAssignment, 0);
        graphPanel.repaint();
    }

    private void updateTimerDisplay(long millis) {
        long minutes = (millis / 60000) % 60;
        long seconds = (millis / 1000) % 60;
        long ms = millis % 1000;
        timerLabel.setText(String.format("Time: %02d:%02d:%03d", minutes, seconds, ms));
    }

    private void startSolving() {
        if (isRunning) return;

        resetMetrics();
        isRunning = true;
        isPaused = false;
        stopRequested = false;

        startBtn.setEnabled(false);
        newGraphBtn.setEnabled(false);
        pauseBtn.setEnabled(true);
        stopBtn.setEnabled(true);
        pauseBtn.setText("Pause");
        statusLabel.setText("Status: Solving...");

        startTime = System.currentTimeMillis();
        chronographTimer.start();

        solverThread = new Thread(() -> {
            boolean success = solveGraphColoring(0);
            chronographTimer.stop();
            isRunning = false;

            SwingUtilities.invokeLater(() -> {
                startBtn.setEnabled(true);
                newGraphBtn.setEnabled(true);
                pauseBtn.setEnabled(false);
                stopBtn.setEnabled(false);

                if (stopRequested) {
                    statusLabel.setText("Status: Stopped by user.");
                } else if (success) {
                    statusLabel.setText("Status: Solution Found!");
                    JOptionPane.showMessageDialog(this, 
                        "Solved in " + moveCount + " moves!\nTime: " + timerLabel.getText().replace("Time: ", ""),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    statusLabel.setText("Status: No solution exists for m = " + numColors);
                    JOptionPane.showMessageDialog(this, 
                        "No valid coloring found with " + numColors + " colors.", 
                        "No Solution", JOptionPane.WARNING_MESSAGE);
                }
            });
        });

        solverThread.start();
    }

    private boolean solveGraphColoring(int vertex) {
        if (stopRequested) return false;

        // Base Case: All vertices are colored
        if (vertex == numVertices) {
            return true;
        }

        // Try candidate colors 1 to m
        for (int c = 1; c <= numColors; c++) {
            handlePauseAndStop();
            if (stopRequested) return false;

            moveCount++;
            SwingUtilities.invokeLater(() -> movesLabel.setText("Moves: " + moveCount));

            if (isSafe(vertex, c)) {
                colorAssignment[vertex] = c;
                graphPanel.repaint();

                // Execution delay for visualization (100ms)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }

                if (solveGraphColoring(vertex + 1)) {
                    return true;
                }

                // Backtrack
                colorAssignment[vertex] = 0;
                graphPanel.repaint();
            }
        }

        return false;
    }

    private boolean isSafe(int v, int color) {
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[v][i] && colorAssignment[i] == color) {
                return false;
            }
        }
        return true;
    }

    private void handlePauseAndStop() {
        while (isPaused && !stopRequested) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void togglePause() {
        if (!isRunning) return;
        isPaused = !isPaused;
        if (isPaused) {
            pauseBtn.setText("Resume");
            statusLabel.setText("Status: Paused");
        } else {
            pauseBtn.setText("Pause");
            statusLabel.setText("Status: Solving...");
        }
    }

    private void stopSolving() {
        stopRequested = true;
        isPaused = false;
        isRunning = false;
        if (chronographTimer != null) chronographTimer.stop();

        if (solverThread != null && solverThread.isAlive()) {
            solverThread.interrupt();
        }

        startBtn.setEnabled(true);
        newGraphBtn.setEnabled(true);
        pauseBtn.setEnabled(false);
        stopBtn.setEnabled(false);
        statusLabel.setText("Status: Reset");
        resetMetrics();
    }

    // Graph Panel for Custom Drawing
    private class GraphPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (numVertices == 0 || adjMatrix == null) return;

            int width = getWidth();
            int height = getHeight();
            int centerX = width / 2;
            int centerY = height / 2;
            int radius = Math.min(width, height) / 3;

            Point[] points = new Point[numVertices];
            for (int i = 0; i < numVertices; i++) {
                double angle = 2 * Math.PI * i / numVertices - Math.PI / 2;
                int x = (int) (centerX + radius * Math.cos(angle));
                int y = (int) (centerY + radius * Math.sin(angle));
                points[i] = new Point(x, y);
            }

            // Draw Edges
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.setColor(new Color(180, 180, 180));
            for (int i = 0; i < numVertices; i++) {
                for (int j = i + 1; j < numVertices; j++) {
                    if (adjMatrix[i][j]) {
                        g2d.drawLine(points[i].x, points[i].y, points[j].x, points[j].y);
                    }
                }
            }

            // Draw Vertices
            int nodeRadius = 24;
            for (int i = 0; i < numVertices; i++) {
                int colorIdx = colorAssignment[i];
                Color nodeColor = (colorIdx < COLOR_PALETTE.length) ? COLOR_PALETTE[colorIdx] : Color.LIGHT_GRAY;

                g2d.setColor(nodeColor);
                g2d.fillOval(points[i].x - nodeRadius, points[i].y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2.0f));
                g2d.drawOval(points[i].x - nodeRadius, points[i].y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

                // Vertex label
                g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
                String label = String.valueOf(i);
                FontMetrics fm = g2d.getFontMetrics();
                int labelX = points[i].x - fm.stringWidth(label) / 2;
                int labelY = points[i].y + fm.getAscent() / 2 - 2;

                g2d.setColor(colorIdx == 0 || colorIdx == 8 ? Color.BLACK : Color.WHITE);
                g2d.drawString(label, labelX, labelY);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphColoringVisualizer app = new GraphColoringVisualizer();
            app.setVisible(true);
        });
    }
}