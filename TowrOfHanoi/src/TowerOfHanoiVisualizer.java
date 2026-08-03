import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TowerOfHanoiVisualizer extends JFrame {

    private static final int MAX_DISKS = 22;
    private int numDisks = 5; // Default number of disks
    
    // UI Components
    private HanoiPanel visualizerPanel;
    private JComboBox<Integer> diskChooser;
    private JButton startButton, pauseButton, resumeButton, stopButton;
    private JLabel chronoLabel;

    // Simulation State
    private volatile boolean isRunning = false;
    private volatile boolean isPaused = false;
    private Thread simulationThread;
    
    // Chronograph Time Tracking (in nanoseconds)
    private long totalElapsedTime = 0;
    private long lastStartTime = 0;
    private Timer chronoUpdateTimer;

    // Tower representation
    private final Stack<Integer>[] towers = new Stack[3];

    public TowerOfHanoiVisualizer() {
        setTitle("Tower of Hanoi Visualizer (Up to 22 Disks)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeTowers();
        initUI();
    }

    private void initializeTowers() {
        for (int i = 0; i < 3; i++) {
            towers[i] = new Stack<>();
        }
        resetTowersState();
    }

    private void resetTowersState() {
        for (int i = 0; i < 3; i++) {
            towers[i].clear();
        }
        for (int i = numDisks; i >= 1; i--) {
            towers[0].push(i);
        }
    }

    private void initUI() {
        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(240, 240, 240));
        controlPanel.setBorder(BorderFactory.createEtchedBorder());

        controlPanel.add(new JLabel("Disks: "));
        Integer[] diskOptions = new Integer[MAX_DISKS];
        for (int i = 0; i < MAX_DISKS; i++) diskOptions[i] = i + 1;
        diskChooser = new JComboBox<>(diskOptions);
        diskChooser.setSelectedItem(numDisks);
        diskChooser.addActionListener(e -> {
            if (!isRunning) {
                numDisks = (int) diskChooser.getSelectedItem();
                resetTowersState();
                visualizerPanel.repaint();
            }
        });
        controlPanel.add(diskChooser);

        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resumeButton = new JButton("Resume");
        stopButton = new JButton("Stop");

        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        stopButton.setEnabled(false);

        controlPanel.add(startButton);
        controlPanel.add(pauseButton);
        controlPanel.add(resumeButton);
        controlPanel.add(stopButton);

        // Chronograph Panel
        JPanel chronoPanel = new JPanel();
        chronoPanel.setBackground(Color.BLACK);
        chronoLabel = new JLabel("00h : 00m : 00s : 000ms : 00qs : 00ps : 00hs : 000ns");
        chronoLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        chronoLabel.setForeground(Color.GREEN);
        chronoPanel.add(chronoLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(chronoPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Visualizer Panel
        visualizerPanel = new HanoiPanel();
        add(visualizerPanel, BorderLayout.CENTER);

        // Button Action Listeners
        startButton.addActionListener(e -> startSimulation());
        pauseButton.addActionListener(e -> pauseSimulation());
        resumeButton.addActionListener(e -> resumeSimulation());
        stopButton.addActionListener(e -> stopSimulation());

        // Setup Swing Timer to refresh Chronograph UI every 30ms
        chronoUpdateTimer = new Timer(30, e -> updateChronographDisplay());
    }

    private synchronized void startSimulation() {
        numDisks = (int) diskChooser.getSelectedItem();
        resetTowersState();
        
        isRunning = true;
        isPaused = false;
        totalElapsedTime = 0;
        lastStartTime = System.nanoTime();

        startButton.setEnabled(false);
        diskChooser.setEnabled(false);
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);
        stopButton.setEnabled(true);

        chronoUpdateTimer.start();

        simulationThread = new Thread(this::runHanoiAlgorithm, "Hanoi-Worker");
        simulationThread.start();
    }

    private synchronized void pauseSimulation() {
        if (isRunning && !isPaused) {
            isPaused = true;
            long now = System.nanoTime();
            totalElapsedTime += (now - lastStartTime);
            
            chronoUpdateTimer.stop();
            updateChronographDisplay(); // Final lock on precise pause time

            pauseButton.setEnabled(false);
            resumeButton.setEnabled(true);
        }
    }

    private synchronized void resumeSimulation() {
        if (isRunning && isPaused) {
            isPaused = false;
            lastStartTime = System.nanoTime();
            
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(false);
            
            chronoUpdateTimer.start();
            notifyAll(); // Wake up the execution thread
        }
    }

    private synchronized void stopSimulation() {
        isRunning = false;
        isPaused = false;
        chronoUpdateTimer.stop();
        
        if (simulationThread != null) {
            simulationThread.interrupt();
        }
        
        resetTowersState();
        updateChronographDisplay();
        visualizerPanel.repaint();

        startButton.setEnabled(true);
        diskChooser.setEnabled(true);
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    private void runHanoiAlgorithm() {
        try {
            moveDisks(numDisks, 0, 2, 1);
        } catch (InterruptedException e) {
            // Thread interrupted meant stop was pressed
        } finally {
            SwingUtilities.invokeLater(this::handleCompletion);
        }
    }

    private void moveDisks(int n, int from, int to, int aux) throws InterruptedException {
        if (n > 0 && isRunning) {
            moveDisks(n - 1, from, aux, to);
            
            checkPauseAndTrack();
            if (!isRunning) return;

            // Perform Move
            towers[to].push(towers[from].pop());
            visualizerPanel.repaint();

            // Dynamic delay calculation depending on disk volume to keep it responsive
            long delay = numDisks > 14 ? 1 : 300; 
            Thread.sleep(delay);

            moveDisks(n - 1, aux, to, from);
        }
    }

    private synchronized void checkPauseAndTrack() throws InterruptedException {
        while (isPaused && isRunning) {
            wait();
        }
    }

    private void handleCompletion() {
        if (isRunning && !isPaused) {
            // Legitimate finish
            long now = System.nanoTime();
            totalElapsedTime += (now - lastStartTime);
            chronoUpdateTimer.stop();
            updateChronographDisplay();
            JOptionPane.showMessageDialog(this, "Tower of Hanoi Completed successfully!");
        }
        stopSimulation();
    }

    private void updateChronographDisplay() {
        long currentRunTime = 0;
        if (isRunning && !isPaused) {
            currentRunTime = System.nanoTime() - lastStartTime;
        }
        long totalNanoseconds = totalElapsedTime + currentRunTime;

        // Metrics conversions based on structural fractions of a second
        long nano = totalNanoseconds % 1000;
        long totalMicro = totalNanoseconds / 1000;
        
        long hexa = totalMicro % 10;          // 10^-5 sec unit
        long penta = (totalMicro / 10) % 10;   // 10^-4 sec unit
        long quadra = (totalMicro / 100) % 10; // 10^-3 sec unit (fractional component remaining before whole ms)
        
        long totalMilli = totalMicro / 1000;
        long milli = totalMilli % 1000;
        
        long totalSeconds = totalMilli / 1000;
        long sec = totalSeconds % 60;
        long min = (totalSeconds / 60) % 60;
        long hrs = totalSeconds / 3600;

        String timeStr = String.format("%02dh : %02dm : %02ds : %03dms : %01dqs : %01dps : %01dhs : %03dns",
                hrs, min, sec, milli, quadra, penta, hexa, nano);
        chronoLabel.setText(timeStr);
    }

    // Panel handling structural math geometry for scaling custom disk counts gracefully
    private class HanoiPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int baseHeight = 30;
            int towerY = height - 50;

            // Draw Base
            g2.setColor(new Color(139, 69, 19));
            g2.fillRect(30, towerY, width - 60, baseHeight);

            // Peg measurements
            int pegWidth = 10;
            int pegHeight = height - 150;
            int spacing = width / 4;

            for (int i = 0; i < 3; i++) {
                int pegX = spacing * (i + 1) - (pegWidth / 2);
                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(pegX, towerY - pegHeight, pegWidth, pegHeight);

                // Safely render disks from snapshot to eliminate concurrent operations crash
                List<Integer> diskSnapshot;
                synchronized (TowerOfHanoiVisualizer.this) {
                 diskSnapshot = new ArrayList<>(towers[i]);
                }
                int diskHeight = Math.max(12, pegHeight / (MAX_DISKS + 2));
                int currentY = towerY - diskHeight;
                for (int disk : diskSnapshot)
                {// Normalize horizontal width scale down linearly per increment
                	int maxDiskWidth = spacing - 20;
                	int minDiskWidth = 25;
                	int diskWidth = minDiskWidth + (disk * (maxDiskWidth - minDiskWidth) / MAX_DISKS);
                	int diskX = spacing * (i + 1) - (diskWidth / 2);
                	// Multi-hue assignment index loops beautifully across spectrum scales
                	g2.setColor(Color.getHSBColor((disk * 0.75f) / MAX_DISKS, 0.85f, 0.85f));
                	g2.fillRoundRect(diskX, currentY, diskWidth, diskHeight, 6, 6);
                	g2.setColor(Color.BLACK);
                	g2.drawRoundRect(diskX, currentY, diskWidth, diskHeight, 6, 6);currentY -= diskHeight;
                	}
                }
            }
        }
     
    
                public static void main(String[] args) {
                	
                	SwingUtilities.invokeLater(() -> new TowerOfHanoiVisualizer().setVisible(true));
                	}
                
                
}