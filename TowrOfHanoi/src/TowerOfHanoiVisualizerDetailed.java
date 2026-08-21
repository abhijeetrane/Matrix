import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TowerOfHanoiVisualizerDetailed extends JFrame {

    // GUI Components
    private JComboBox<Integer> diskLabelCombo;
    private JButton startBtn;
    private JButton pauseBtn;
    private JButton resumeBtn;
    private JButton stopBtn;
    private JLabel chronoLabel;
    private HanoiPanel hanoiPanel;

    // Game State Variables
    private int numDisks = 3;
    @SuppressWarnings("unchecked")
    private final Stack<Integer>[] towers = new Stack[3];
    private final List<Move> moveSequence = new ArrayList<>();
    private int currentMoveIndex = 0;
    
    // Thread Control Flags
    private volatile boolean running = false;
    private volatile boolean paused = false;
    private Thread gameThread;
    private Thread chronoThread;

    // Chronograph Time Tracking (in Nanoseconds)
    private long totalElapsedNs = 0;
    private long lastStartTimeNs = 0;

    // Global speed configuration (milliseconds per move animation)
    private static final int DELAY_MS = 400; 

    public TowerOfHanoiVisualizerDetailed() {
        setTitle("Tower of Hanoi Chrono-Visualizer Detailed");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        towers[0] = new Stack<>();
        towers[1] = new Stack<>();
        towers[2] = new Stack<>();

        initControlPanel();
        initVisualPanel();
        resetGame();
    }

    private void initControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(245, 245, 245));
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 10));

        controlPanel.add(new JLabel("Disks (1-22):"));
        Integer[] diskOptions = new Integer[22];
        for (int i = 0; i < 22; i++) diskOptions[i] = i + 1;
        diskLabelCombo = new JComboBox<>(diskOptions);
        diskLabelCombo.setSelectedItem(5); // Default to a visually clear number
        controlPanel.add(diskLabelCombo);

        startBtn = new JButton("Start");
        pauseBtn = new JButton("Pause");
        resumeBtn = new JButton("Resume");
        stopBtn = new JButton("Stop");

        pauseBtn.setEnabled(false);
        resumeBtn.setEnabled(false);
        stopBtn.setEnabled(false);

        controlPanel.add(startBtn);
        controlPanel.add(pauseBtn);
        controlPanel.add(resumeBtn);
        controlPanel.add(stopBtn);

        chronoLabel = new JLabel("00:00:00.000 | Q:0000 | P:00000 | H:000000 | Ns:000000000");
        chronoLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        chronoLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        controlPanel.add(chronoLabel);

        add(controlPanel, BorderLayout.NORTH);

        // UI Event Hooks
        startBtn.addActionListener(e -> startSimulation());
        pauseBtn.addActionListener(e -> pauseSimulation());
        resumeBtn.addActionListener(e -> resumeSimulation());
        stopBtn.addActionListener(e -> stopSimulation());
    }

    private void initVisualPanel() {
        hanoiPanel = new HanoiPanel();
        add(hanoiPanel, BorderLayout.CENTER);
    }

    private void resetGame() {
        running = false;
        paused = false;
        numDisks = (Integer) diskLabelCombo.getSelectedItem();
        
        towers[0].clear();
        towers[1].clear();
        towers[2].clear();

        // Push disks onto the first peg (Largest at the bottom)
        for (int i = numDisks; i > 0; i--) {
            towers[0].push(i);
        }

        moveSequence.clear();
        currentMoveIndex = 0;
        totalElapsedNs = 0;
        
        updateChronoDisplay(0);
        hanoiPanel.repaint();

        diskLabelCombo.setEnabled(true);
        startBtn.setEnabled(true);
        pauseBtn.setEnabled(false);
        resumeBtn.setEnabled(false);
        stopBtn.setEnabled(false);
    }

    private void startSimulation() {
        resetGame();
        diskLabelCombo.setEnabled(false);
        startBtn.setEnabled(false);
        pauseBtn.setEnabled(true);
        stopBtn.setEnabled(true);

        // Build iterative move map to eliminate deep call stack recursion constraints at depth 22
        generateHanoiMoves(numDisks, 0, 2, 1);

        running = true;
        paused = false;
        lastStartTimeNs = System.nanoTime();

        startThreads();
    }

    private void pauseSimulation() {
        if (running && !paused) {
            paused = true;
            totalElapsedNs += (System.nanoTime() - lastStartTimeNs);
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(true);
        }
    }

    private void resumeSimulation() {
        if (running && paused) {
            paused = false;
            lastStartTimeNs = System.nanoTime();
            pauseBtn.setEnabled(true);
            resumeBtn.setEnabled(false);
        }
    }

    private void stopSimulation() {
        running = false;
        paused = false;
        resetGame();
    }

    private void startThreads() {
        // Animation Loop Thread
        gameThread = new Thread(() -> {
            while (running && currentMoveIndex < moveSequence.size()) {
                if (!paused) {
                    Move nextMove = moveSequence.get(currentMoveIndex);
                    towers[nextMove.to].push(towers[nextMove.from].pop());
                    currentMoveIndex++;
                    hanoiPanel.repaint();

                    try {
                        Thread.sleep(DELAY_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            if (currentMoveIndex >= moveSequence.size() && running) {
                totalElapsedNs += (System.nanoTime() - lastStartTimeNs);
                running = false;
                SwingUtilities.invokeLater(() -> {
                    pauseBtn.setEnabled(false);
                    resumeBtn.setEnabled(false);
                    JOptionPane.showMessageDialog(this, "Tower of Hanoi completed successfully!");
                });
            }
        });

        // Precision Chronograph Management Thread
        chronoThread = new Thread(() -> {
            while (running) {
                if (!paused) {
                    long currentSessionNs = System.nanoTime() - lastStartTimeNs;
                    long liveTotalNs = totalElapsedNs + currentSessionNs;
                    SwingUtilities.invokeLater(() -> updateChronoDisplay(liveTotalNs));
                }
                try {
                    Thread.sleep(1); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        gameThread.start();
        chronoThread.start();
    }

    private void generateHanoiMoves(int n, int from, int to, int aux) {
        Stack<HanoiStep> stepStack = new Stack<>();
        stepStack.push(new HanoiStep(n, from, to, aux));

        while (!stepStack.isEmpty()) {
            HanoiStep current = stepStack.pop();
            if (current.n == 1) {
                moveSequence.add(new Move(current.from, current.to));
            } else {
                stepStack.push(new HanoiStep(current.n - 1, current.aux, current.to, current.from));
                stepStack.push(new HanoiStep(1, current.from, current.to, current.aux));
                stepStack.push(new HanoiStep(current.n - 1, current.from, current.aux, current.to));
            }
        }
    }

    private void updateChronoDisplay(long totalNs) {
        long hr = totalNs / 3600000000000L;
        long rem = totalNs % 3600000000000L;
        
        long min = rem / 60000000000L;
        rem %= 60000000000L;
        
        long sec = rem / 1000000000L;
        rem %= 1000000000L;
        
        long ms = rem / 1000000L;
        
        // Calculations for fractional seconds metrics request
        long quadSec = totalNs / 250000000L;   // 1/4 second unit
        long pentaSec = totalNs / 200000000L;  // 1/5 second unit
        long hexaSec = totalNs / 166666666L;   // 1/6 second unit

        String timeStr = String.format(
            "%02d:%02d:%02d.%03d | Q:%04d | P:%05d | H:%06d | Ns:%09d",
            hr, min, sec, ms, quadSec % 10000, pentaSec % 100000, hexaSec % 1000000, totalNs % 1000000000
        );
        chronoLabel.setText(timeStr);
    }

    // Helper structures
    private static class Move {
        int from, to;
        Move(int f, int t) { this.from = f; this.to = t; }
    }

    private static class HanoiStep {
        int n, from, to, aux;
        HanoiStep(int n, int f, int t, int a) {
            this.n = n; this.from = f; this.to = t; this.aux = a;
        }
    }

    // Interactive Render Engine
    private class HanoiPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int baseWidth = width - 240; 
            int baseY = height - 60;

            // Draw Real-time Moves Count in Top Left Corner
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("Current Move Count: " + currentMoveIndex, 25, 30);

            // Draw Base
            g2.setColor(new Color(110, 75, 45));
            g2.fillRect(180, baseY, baseWidth, 22);
            int pegSpacing = baseWidth / 3;
            int[] pegX = {180 + pegSpacing / 2,180 + pegSpacing / 2 + pegSpacing,180 + pegSpacing / 2 + pegSpacing * 2};
            
            // Draw Vertical Pegs
            g2.setColor(new Color(70, 70, 70));
            for (int x : pegX) {
            	g2.fillRect(x - 6, baseY - 440, 12, 440);
            	}
            // Global sizing rules based on maximum disk limits selected
            int maxDiskWidth = pegSpacing - 25;
            int diskHeight = Math.max(12, 420 / Math.max(numDisks, 1));
            // Paint active elements
            for (int pegIdx = 0; pegIdx < 3; pegIdx++) {
            	Stack pegStack = towers[pegIdx];
            	for (int hIdx = 0; hIdx < pegStack.size(); hIdx++)
            	{
            		int diskValue = (int) pegStack.get(hIdx);
            		
                    // Dimensions mapping logic
            		int diskWidth = (int) (((double) diskValue / 22) * maxDiskWidth + 30);
            		int x = pegX[pegIdx] - (diskWidth / 2);
            		int y = baseY - (hIdx * diskHeight) - diskHeight;
            		
            		// Resolve label tier based on bottom-up disk index
            		// Global maximum tracking definitions: 1-22 range values// 1-8: Heaven (Top 8), 9-12: Earth (Middle 4), 13-20: Hell (Lower 8), 21-22: Parking (Bottom 2)
            		
            		String tierName = "";
            		Color diskColor;int invertedRank = 22 - diskValue + 1; 
            		
            		// 1 is largest disk, 22 is smallest disk
            		if (invertedRank <= 2) {
            			tierName = "Parking";
            			diskColor = new Color(215, 60, 60); 
            			// Red Crimson
            			} 
            			else if (invertedRank <= 10) {
            				tierName = "Hell";
            				diskColor = new Color(230, 126, 34); 
            				// Hellfire Orange
            				} 
            				else if (invertedRank <= 14) {
            					tierName = "Earth";
            					diskColor = new Color(46, 204, 113); 
            					// Grass Earth Green
            					} 
            					else {
            						tierName = "Heaven";
            						diskColor = new Color(52, 152, 219); 
            						// Sky Blue
            						}
            					// Draw graphical disk asset bounds
            					g2.setColor(diskColor);
            					g2.fillRect(x, y, diskWidth, diskHeight - 2);
            					g2.setColor(new Color(25, 25, 25));
            					g2.drawRect(x, y, diskWidth, diskHeight - 2);
            					// Print context category descriptor strings inside elements if space allows
            					if (diskHeight >= 14 && diskWidth > 75) {
            						g2.setColor(Color.WHITE);
            						g2.setFont(new Font("SansSerif", Font.BOLD, Math.min(11, diskHeight - 4)
            								)
            								);
            						FontMetrics fm = g2.getFontMetrics();
            						String displayText = tierName + " (" + diskValue + ")";
            						int textX = x + (diskWidth - fm.stringWidth(displayText)) / 2;
            						int textY = y + ((diskHeight - fm.getHeight()) / 2) + fm.getAscent();
            						g2.drawString(displayText, textX, textY);
            						}
            					}
            				}
            			}
            		}
            	public static void main(String[] args) {
            		SwingUtilities.invokeLater(() -> new TowerOfHanoiVisualizerDetailed().setVisible(true));
            		}
 }
            				