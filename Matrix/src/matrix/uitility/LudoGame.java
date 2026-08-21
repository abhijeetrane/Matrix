package matrix.uitility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class LudoGame extends JFrame {
    private BoardPanel boardPanel;
    private JButton pauseBtn, resumeBtn, diceBtn;
    private JLabel statusLabel, diceLabel;
    private volatile boolean paused = false;
    private volatile boolean running = true;
    private Thread gameThread;

    // Game State
    private int currentPlayer = 0; // 0=Red, 1=Green, 2=Yellow, 3=Blue
    private int diceValue = 1;
    private Random rand = new Random();
    private Token[][] tokens = new Token[4][4];
    private Color[] playerColors = {Color.RED, new Color(0,150,0), new Color(220,220,0), Color.BLUE};
    private String[] playerNames = {"RED", "GREEN", "YELLOW", "BLUE"};

    // Simple path - 52 main squares + 6 home squares per player
    private Point[] mainPath = new Point[52];
    private Point[][] homePath = new Point[4][6];

    public LudoGame() {
        setTitle("Ludo Simulation - Infinite Loop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initPaths();
        initTokens();

        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(240,240,240));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        pauseBtn = new JButton("PAUSE");
        resumeBtn = new JButton("RESUME");
        diceBtn = new JButton("Roll Dice");
        statusLabel = new JLabel("Turn: RED");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        diceLabel = new JLabel("Dice: 1");
        diceLabel.setFont(new Font("Arial", Font.BOLD, 18));

        pauseBtn.setBackground(new Color(255,100,100));
        resumeBtn.setBackground(new Color(100,255,100));
        resumeBtn.setEnabled(false);

        controlPanel.add(diceLabel);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(diceBtn);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(pauseBtn);
        controlPanel.add(resumeBtn);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(statusLabel);

        add(controlPanel, BorderLayout.SOUTH);

        pauseBtn.addActionListener(e -> {
            paused = true;
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(true);
            statusLabel.setText("PAUSED - " + playerNames[currentPlayer] + "'s turn");
        });

        resumeBtn.addActionListener(e -> {
            synchronized (this) {
                paused = false;
                this.notifyAll();
            }
            pauseBtn.setEnabled(true);
            resumeBtn.setEnabled(false);
            statusLabel.setText("Turn: " + playerNames[currentPlayer]);
        });

        diceBtn.addActionListener(e -> manualRoll());

        setSize(750, 800);
        setLocationRelativeTo(null);
        setVisible(true);

        startInfiniteLoop();
    }

    private void initPaths() {
        // Create a circular path around board (simplified coordinates for 600x600 board)
        // This is a visual approximation - 52 points around
        int size = 600;
        int step = size / 15;
        int idx = 0;
        // bottom row left to right
        for (int i = 0; i < 6; i++) mainPath[idx++] = new Point(1*step + i*step, 9*step);
        for (int i = 0; i < 6; i++) mainPath[idx++] = new Point(6*step, 9*step - (i+1)*step);
        for (int i = 0; i < 3; i++) mainPath[idx++] = new Point(6*step + (i+1)*step, 3*step);
        for (int i = 0; i < 6; i++) mainPath[idx++] = new Point(9*step, 3*step + i*step);
        for (int i = 0; i < 6; i++) mainPath[idx++] = new Point(9*step + (i+1)*step, 9*step);
        for (int i = 0; i < 6; i++) mainPath[idx++] = new Point(14*step - i*step, 10*step);
        for (int i = 0; i < 6; i++) mainPath[idx++] = new Point(9*step, 10*step + (i+1)*step);
        for (int i = 0; i < 3; i++) mainPath[idx++] = new Point(9*step - (i+1)*step, 15*step - step);
        for (int i = 0; i < 6; i++) mainPath[idx++] = new Point(6*step, 15*step - step - i*step);
        for (int i = 0; i < 4; i++) mainPath[idx++] = new Point(6*step - (i+1)*step, 10*step);

        // Home paths
        for (int p = 0; p < 4; p++) {
            for (int j = 0; j < 6; j++) {
                if (p == 0) homePath[p][j] = new Point(2*step + j*step, 8*step); // Red
                if (p == 1) homePath[p][j] = new Point(7*step, 4*step + j*step); // Green
                if (p == 2) homePath[p][j] = new Point(13*step - j*step, 8*step); // Yellow
                if (p == 3) homePath[p][j] = new Point(7*step, 13*step - j*step); // Blue
            }
        }
    }

    private void initTokens() {
        for (int p = 0; p < 4; p++) {
            for (int t = 0; t < 4; t++) {
                tokens[p][t] = new Token(p, t);
            }
        }
    }

    private void manualRoll() {
        if (paused) return;
        doTurn();
    }

    private void startInfiniteLoop() {
        gameThread = new Thread(() -> {
            // INFINITE LOOP as requested
            while (running) {
                try {
                    synchronized (LudoGame.this) {
                        while (paused) {
                            LudoGame.this.wait();
                        }
                    }

                    SwingUtilities.invokeLater(() -> doTurn());
                    Thread.sleep(1200); // speed of simulation

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        gameThread.start();
    }

    private void doTurn() {
        diceValue = rand.nextInt(6) + 1;
        diceLabel.setText("Dice: " + diceValue);

        // Pick a movable token
        List<Token> movable = new ArrayList<>();
        for (Token tk : tokens[currentPlayer]) {
            if (tk.canMove(diceValue)) movable.add(tk);
        }

        if (!movable.isEmpty()) {
            Token chosen = movable.get(rand.nextInt(movable.size()));
            chosen.move(diceValue);
            
            // Capture logic
            if (!chosen.inHome && !chosen.finished) {
                for (int p = 0; p < 4; p++) {
                    if (p == currentPlayer) continue;
                    for (Token other : tokens[p]) {
                        if (!other.inHome && !other.finished && other.position == chosen.position) {
                            other.goHome();
                        }
                    }
                }
            }
        }

        boardPanel.repaint();

        // Check win
        boolean won = true;
        for (Token tk : tokens[currentPlayer]) if (!tk.finished) won = false;
        if (won) {
            statusLabel.setText(playerNames[currentPlayer] + " WINS! Restarting...");
            JOptionPane.showMessageDialog(this, playerNames[currentPlayer] + " wins the game!");
            resetGame();
            return;
        }

        // Next turn: if dice is 6, same player continues (Ludo rule)
        if (diceValue != 6) {
            currentPlayer = (currentPlayer + 1) % 4;
        }
        statusLabel.setText("Turn: " + playerNames[currentPlayer] + " | Dice: " + diceValue);
    }

    private void resetGame() {
        initTokens();
        currentPlayer = 0;
        boardPanel.repaint();
    }

    class Token {
        int player, id;
        int position = -1; // -1 = at home base, 0-51 = on main track, 100+ = in home stretch
        int homeSteps = -1;
        boolean inHome = true;
        boolean finished = false;

        Token(int p, int i) { player = p; id = i; }

        boolean canMove(int dice) {
            if (finished) return false;
            if (inHome) return dice == 6;
            if (homeSteps >= 0) return homeSteps + dice <= 6;
            return true;
        }

        void move(int dice) {
            if (inHome && dice == 6) {
                inHome = false;
                position = player * 13; // starting point
                return;
            }
            if (homeSteps >= 0) {
                homeSteps += dice;
                if (homeSteps == 6) finished = true;
                return;
            }
            position = (position + dice) % 52;
            // Check if entering home
            int entry = (player * 13 + 50) % 52;
            if (position == entry) {
                // chance to enter home stretch next time around - simplified: enter if close to finish
                // For simplicity, after one full round allow home entry
                if (rand.nextBoolean()) {
                    homeSteps = 0;
                }
            }
        }

        void goHome() {
            inHome = true;
            position = -1;
            homeSteps = -1;
        }

        Point getDrawPos() {
            if (finished) {
                return homePath[player][5];
            }
            if (inHome) {
                // Base positions
                int baseX = (player % 2 == 0) ? 1 : 10;
                int baseY = (player < 2) ? 1 : 10;
                int offsetX = (id % 2) * 2;
                int offsetY = (id / 2) * 2;
                return new Point((baseX + offsetX) * 40 + 30, (baseY + offsetY) * 40 + 30);
            }
            if (homeSteps >= 0) {
                return homePath[player][homeSteps];
            }
            if (position >= 0 && position < mainPath.length) {
                return mainPath[position];
            }
            return new Point(0,0);
        }
    }

    class BoardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2.setColor(new Color(250, 245, 230));
            g2.fillRect(0,0,getWidth(), getHeight());

            // Draw Ludo board outline
            int s = getWidth() / 15;
            // Bases
            g2.setColor(new Color(255,200,200));
            g2.fillRect(0,0,6*s,6*s);
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(0,0,6*s,6*s);

            g2.setColor(new Color(200,255,200));
            g2.fillRect(9*s,0,6*s,6*s);
            g2.setColor(new Color(0,150,0));
            g2.drawRect(9*s,0,6*s,6*s);

            g2.setColor(new Color(255,255,180));
            g2.fillRect(0,9*s,6*s,6*s);
            g2.setColor(Color.ORANGE);
            g2.drawRect(0,9*s,6*s,6*s);

            g2.setColor(new Color(180,200,255));
            g2.fillRect(9*s,9*s,6*s,6*s);
            g2.setColor(Color.BLUE);
            g2.drawRect(9*s,9*s,6*s,6*s);

            // Main track
            g2.setColor(Color.WHITE);
            for (Point p : mainPath) {
                if (p == null) continue;
                g2.setColor(Color.WHITE);
                g2.fillRect(p.x, p.y, s-2, s-2);
                g2.setColor(Color.BLACK);
                g2.drawRect(p.x, p.y, s-2, s-2);
            }

            // Highlight start points
            int[] starts = {0,13,26,39};
            for (int i=0;i<4;i++) {
                Point p = mainPath[starts[i]];
                g2.setColor(playerColors[i]);
                g2.fillOval(p.x+5, p.y+5, s-12, s-12);
            }

            // Draw home paths
            for (int p =0; p<4; p++) {
                for (Point hp : homePath[p]) {
                    g2.setColor(playerColors[p].brighter());
                    g2.fillRect(hp.x, hp.y, s-2, s-2);
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawRect(hp.x, hp.y, s-2, s-2);
                }
            }

            // Center home
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(6*s,6*s,3*s,3*s);
            g2.setColor(Color.BLACK);
            g2.drawRect(6*s,6*s,3*s,3*s);
            g2.drawString("HOME", 7*s+5, 7*s+15);

            // Draw Tokens
            for (int p=0;p<4;p++) {
                for (Token tk : tokens[p]) {
                    Point pos = tk.getDrawPos();
                    g2.setColor(playerColors[p]);
                    g2.fillOval(pos.x, pos.y, 28, 28);
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(pos.x, pos.y, 28, 28);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 12));
                    g2.drawString(String.valueOf(tk.id+1), pos.x+9, pos.y+18);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LudoGame());
    }
}
