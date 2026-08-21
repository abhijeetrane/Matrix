package matrix.uitility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class LudoSimulation extends JFrame {
    private static final int BOARD_SIZE = 15;
    private static final int CELL_SIZE = 40;
    
    // Game State
    private int redTokenPos = -1;  // -1 means in base
    private int blueTokenPos = -1;
    private int currentTurn = 0;   // 0 = Red, 1 = Blue
    private int diceValue = 1;
    
    // Control variables
    private Timer gameTimer;
    private boolean isPaused = false;
    private int simulationDelay = 300; // ms per move
    
    private LudoBoardPanel boardPanel;
    private JLabel statusLabel;
    private JLabel diceLabel;
    private Random random = new Random();

    // Simplified track coordinates (row, col) for a 15x15 board
    private final Point[] redTrack = {
        new Point(6, 1), new Point(6, 2), new Point(6, 3), new Point(6, 4), new Point(6, 5),
        new Point(5, 6), new Point(4, 6), new Point(3, 6), new Point(2, 6), new Point(1, 6), new Point(0, 6),
        new Point(0, 7), new Point(0, 8),
        new Point(1, 8), new Point(2, 8), new Point(3, 8), new Point(4, 8), new Point(5, 8),
        new Point(6, 9), new Point(6, 10), new Point(6, 11), new Point(6, 12), new Point(6, 13), new Point(6, 14),
        new Point(7, 14), new Point(8, 14),
        new Point(8, 13), new Point(8, 12), new Point(8, 11), new Point(8, 10), new Point(8, 9),
        new Point(9, 8), new Point(10, 8), new Point(11, 8), new Point(12, 8), new Point(13, 8), new Point(14, 8),
        new Point(14, 7), new Point(14, 6),
        new Point(13, 6), new Point(12, 6), new Point(11, 6), new Point(10, 6), new Point(9, 6),
        new Point(8, 5), new Point(8, 4), new Point(8, 3), new Point(8, 2), new Point(8, 1), new Point(8, 0),
        new Point(7, 0),
        // Home stretch for Red
        new Point(7, 1), new Point(7, 2), new Point(7, 3), new Point(7, 4), new Point(7, 5), new Point(7, 6)
    };

    private final Point[] blueTrack = {
        new Point(8, 13), new Point(8, 12), new Point(8, 11), new Point(8, 10), new Point(8, 9),
        new Point(9, 8), new Point(10, 8), new Point(11, 8), new Point(12, 8), new Point(13, 8), new Point(14, 8),
        new Point(14, 7), new Point(14, 6),
        new Point(13, 6), new Point(12, 6), new Point(11, 6), new Point(10, 6), new Point(9, 6),
        new Point(8, 5), new Point(8, 4), new Point(8, 3), new Point(8, 2), new Point(8, 1), new Point(8, 0),
        new Point(7, 0), new Point(6, 0),
        new Point(6, 1), new Point(6, 2), new Point(6, 3), new Point(6, 4), new Point(6, 5),
        new Point(5, 6), new Point(4, 6), new Point(3, 6), new Point(2, 6), new Point(1, 6), new Point(0, 6),
        new Point(0, 7), new Point(0, 8),
        new Point(1, 8), new Point(2, 8), new Point(3, 8), new Point(4, 8), new Point(5, 8),
        new Point(6, 9), new Point(6, 10), new Point(6, 11), new Point(6, 12), new Point(6, 13), new Point(6, 14),
        new Point(7, 14),
        // Home stretch for Blue
        new Point(7, 13), new Point(7, 12), new Point(7, 11), new Point(7, 10), new Point(7, 9), new Point(7, 8)
    };

    public LudoSimulation() {
        setTitle("Ludo Infinite Simulation");
        setSize(BOARD_SIZE * CELL_SIZE + 40, BOARD_SIZE * CELL_SIZE + 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new LudoBoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JButton pauseButton = new JButton("Pause");
        JButton resumeButton = new JButton("Resume");
        statusLabel = new JLabel("Status: Running | ");
        diceLabel = new JLabel("Dice: -");

        JSlider speedSlider = new JSlider(50, 1000, simulationDelay);
        speedSlider.setToolTipText("Adjust Speed");
        speedSlider.addChangeListener(e -> {
            simulationDelay = speedSlider.getValue();
            if (gameTimer != null) {
                gameTimer.setDelay(simulationDelay);
            }
        });

        pauseButton.addActionListener(e -> {
            isPaused = true;
            statusLabel.setText("Status: Paused | ");
        });

        resumeButton.addActionListener(e -> {
            isPaused = false;
            statusLabel.setText("Status: Running | ");
        });

        controlPanel.add(statusLabel);
        controlPanel.add(diceLabel);
        controlPanel.add(pauseButton);
        controlPanel.add(resumeButton);
        controlPanel.add(new JLabel("Speed:"));
        controlPanel.add(speedSlider);

        add(controlPanel, BorderLayout.SOUTH);

        // Timer acting as the main simulation loop
        gameTimer = new Timer(simulationDelay, e -> {
            if (!isPaused) {
                stepSimulation();
            }
        });
        gameTimer.start();
    }

    private void stepSimulation() {
        diceValue = random.nextInt(6) + 1;
        diceLabel.setText("Dice: " + diceValue + " (" + (currentTurn == 0 ? "Red" : "Blue") + ")");

        if (currentTurn == 0) { // Red's Turn
            if (redTokenPos == -1) {
                if (diceValue == 6) {
                    redTokenPos = 0; // Move out of base
                }
            } else {
                if (redTokenPos + diceValue < redTrack.length) {
                    redTokenPos += diceValue;
                }
            }

            // Check Win Condition
            if (redTokenPos == redTrack.length - 1) {
                JOptionPane.showMessageDialog(this, "Red Won! Resetting for Next Game...");
                resetGame();
                return;
            }

            // Capture Blue
            if (redTokenPos >= 0 && redTokenPos < redTrack.length - 6) {
                Point redPt = redTrack[redTokenPos];
                if (blueTokenPos >= 0 && blueTokenPos < blueTrack.length - 6) {
                    Point bluePt = blueTrack[blueTokenPos];
                    if (redPt.equals(bluePt)) {
                        blueTokenPos = -1; // Send Blue back to base
                    }
                }
            }
        } else { // Blue's Turn
            if (blueTokenPos == -1) {
                if (diceValue == 6) {
                    blueTokenPos = 0; // Move out of base
                }
            } else {
                if (blueTokenPos + diceValue < blueTrack.length) {
                    blueTokenPos += diceValue;
                }
            }

            // Check Win Condition
            if (blueTokenPos == blueTrack.length - 1) {
                JOptionPane.showMessageDialog(this, "Blue Won! Resetting for Next Game...");
                resetGame();
                return;
            }

            // Capture Red
            if (blueTokenPos >= 0 && blueTokenPos < blueTrack.length - 6) {
                Point bluePt = blueTrack[blueTokenPos];
                if (redTokenPos >= 0 && redTokenPos < redTrack.length - 6) {
                    Point redPt = redTrack[redTokenPos];
                    if (bluePt.equals(redPt)) {
                        redTokenPos = -1; // Send Red back to base
                    }
                }
            }
        }

        // Switch turn if not a 6
        if (diceValue != 6) {
            currentTurn = 1 - currentTurn;
        }

        boardPanel.repaint();
    }

    private void resetGame() {
        redTokenPos = -1;
        blueTokenPos = -1;
        currentTurn = 0;
        diceValue = 1;
        boardPanel.repaint();
    }

    // Custom Panel to draw the board and tokens
    private class LudoBoardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Draw Grid
            for (int r = 0; r < BOARD_SIZE; r++) {
                for (int c = 0; c < BOARD_SIZE; c++) {
                    int x = c * CELL_SIZE;
                    int y = r * CELL_SIZE;

                    // Base zones
                    if (r < 6 && c < 6) {
                        g2.setColor(new Color(255, 102, 102));
                    } else if (r < 6 && c >= 9) {
                        g2.setColor(new Color(102, 255, 102));
                    } else if (r >= 9 && c < 6) {
                        g2.setColor(new Color(255, 255, 102));
                    } else if (r >= 9 && c >= 9) {
                        g2.setColor(new Color(102, 102, 255));
                    } else if (r >= 6 && r <= 8 && c >= 6 && c <= 8) {
                        g2.setColor(Color.LIGHT_GRAY); // Center Home
                    } else {
                        g2.setColor(Color.WHITE);
                    }

                    g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }

            // Draw Red Home Stretch
            g2.setColor(Color.RED);
            for (int c = 1; c <= 5; c++) {
                g2.fillRect(c * CELL_SIZE, 7 * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g2.setColor(Color.BLACK);
                g2.drawRect(c * CELL_SIZE, 7 * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g2.setColor(Color.RED);
            }

            // Draw Blue Home Stretch
            g2.setColor(Color.BLUE);
            for (int c = 9; c <= 13; c++) {
                g2.fillRect(c * CELL_SIZE, 7 * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g2.setColor(Color.BLACK);
                g2.drawRect(c * CELL_SIZE, 7 * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g2.setColor(Color.BLUE);
            }

            // Draw Red Token
            g2.setColor(Color.RED);
            if (redTokenPos == -1) {
                g2.fillOval(2 * CELL_SIZE + 5, 2 * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            } else {
                Point p = redTrack[redTokenPos];
                g2.fillOval(p.y * CELL_SIZE + 5, p.x * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            }

            // Draw Blue Token
            g2.setColor(Color.BLUE);
            if (blueTokenPos == -1) {
                g2.fillOval(12 * CELL_SIZE + 5, 12 * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            } else {
                Point p = blueTrack[blueTokenPos];
                g2.fillOval(p.y * CELL_SIZE + 5, p.x * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LudoSimulation frame = new LudoSimulation();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}