import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NQueensVisualizer extends JFrame {
    private int n;
    private int[][] board;
    private JButton[][] buttons;
    private JLabel movesLabel;
    private JLabel timeLabel;
    private JButton pauseButton;
    private JButton stopButton;
    
    private int moveCount = 0;
    private long startTime;
    private long elapsedTime = 0;
    private Timer chronograph;
    private Thread solverThread;
    
    private volatile boolean isPaused = false;
    private volatile boolean isStopped = false;
    private final Object pauseLock = new Object();

    public NQueensVisualizer() {
        promptForN();
        if (n < 1) {
            System.exit(0);
        }

        board = new int[n][n];
        buttons = new JButton[n][n];

        setTitle("N-Queens Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Control Panel
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        movesLabel = new JLabel("Moves: 0", SwingConstants.CENTER);
        timeLabel = new JLabel("Time: 00:00.0", SwingConstants.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);

        topPanel.add(movesLabel);
        topPanel.add(timeLabel);
        topPanel.add(buttonPanel);
        add(topPanel, BorderLayout.NORTH);

        // Chessboard Panel
        JPanel boardPanel = new JPanel(new GridLayout(n, n));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Serif", Font.BOLD, 24));
                buttons[i][j].setFocusable(false);
                if ((i + j) % 2 == 0) {
                    buttons[i][j].setBackground(Color.WHITE);
                } else {
                    buttons[i][j].setBackground(Color.GRAY);
                }
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Action Listeners
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    isPaused = true;
                    pauseButton.setText("Resume");
                    chronograph.stop();
                } else {
                    synchronized (pauseLock) {
                        isPaused = false;
                        pauseLock.notifyAll();
                    }
                    pauseButton.setText("Pause");
                    startTime = System.currentTimeMillis() - elapsedTime;
                    chronograph.start();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isStopped = true;
                chronograph.stop();
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);
                synchronized (pauseLock) {
                    isPaused = false;
                    pauseLock.notifyAll();
                }
                JOptionPane.showMessageDialog(NQueensVisualizer.this, "Solving stopped by user.");
            }
        });

        // Initialize Chronograph Timer (updates every 100ms)
        chronograph = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime = System.currentTimeMillis() - startTime;
                long mins = (elapsedTime / 60000) % 60;
                long secs = (elapsedTime / 1000) % 60;
                long tenths = (elapsedTime / 100) % 10;
                timeLabel.setText(String.format("Time: %02d:%02d.%d", mins, secs, tenths));
            }
        });

        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        // Start background solving thread
        startSolving();
    }

    private void promptForN() {
        String input = JOptionPane.showInputDialog(this, "Enter the value of N for the chessboard:", "N-Queens Setup", JOptionPane.QUESTION_MESSAGE);
        try {
            n = Integer.parseInt(input);
            if (n <= 0) {
                JOptionPane.showMessageDialog(this, "N must be a positive integer.");
                n = 0;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Exiting application.");
            n = 0;
        }
    }

    private void startSolving() {
        startTime = System.currentTimeMillis();
        chronograph.start();

        solverThread = new Thread(() -> {
            if (solveNQueens(0)) {
                chronograph.stop();
                SwingUtilities.invokeLater(() -> {
                    pauseButton.setEnabled(false);
                    stopButton.setEnabled(false);
                    JOptionPane.showMessageDialog(this, "Solution Found completely!");
                });
            } else {
                chronograph.stop();
                SwingUtilities.invokeLater(() -> {
                    pauseButton.setEnabled(false);
                    stopButton.setEnabled(false);
                    if (!isStopped) {
                        JOptionPane.showMessageDialog(this, "No solution exists for N = " + n);
                    }
                });
            }
        });
        solverThread.start();
    }

    private boolean solveNQueens(int col) {
        if (col >= n) {
            return true;
        }

        for (int i = 0; i < n; i++) {
            if (isStopped) {
                return false;
            }

            checkPause();

            if (isSafe(i, col)) {
                board[i][col] = 1;
                incrementMoves();
                updateGuiBoard(i, col, "Q");

                try {
                    Thread.sleep(200); // Delay for visual representation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (solveNQueens(col + 1)) {
                    return true;
                }

                // Backtracking step
                board[i][col] = 0;
                incrementMoves();
                updateGuiBoard(i, col, "");
                
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }

    private boolean isSafe(int row, int col) {
        int i, j;
        for (i = 0; i < col; i++) {
            if (board[row][i] == 1) return false;
        }
        for (i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 1) return false;
        }
        for (i = row, j = col; j >= 0 && i < n; i++, j--) {
            if (board[i][j] == 1) return false;
        }
        return true;
    }

    private void checkPause() {
        synchronized (pauseLock) {
            while (isPaused) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void incrementMoves() {
        moveCount++;
        SwingUtilities.invokeLater(() -> movesLabel.setText("Moves: " + moveCount));
    }

    private void updateGuiBoard(int row, int col, String text) {
        SwingUtilities.invokeLater(() -> {
            buttons[row][col].setText(text);
            if (text.equals("Q")) {
                buttons[row][col].setForeground(Color.RED);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NQueensVisualizer());
    }
}
