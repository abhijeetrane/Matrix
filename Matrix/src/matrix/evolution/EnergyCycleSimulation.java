package matrix.evolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

public class EnergyCycleSimulation extends JFrame {

    public EnergyCycleSimulation() {
        setTitle("Carbon & Energy Cycle Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        CyclePanel cyclePanel = new CyclePanel();
        add(cyclePanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EnergyCycleSimulation().setVisible(true);
        });
    }
}

class CyclePanel extends JPanel implements ActionListener {

    private final Timer animationTimer;
    private final Timer chronographTimer;

    private boolean isRunning = true;

    // Chronograph variables
    private int elapsedSeconds = 0;
    private final JLabel timeLabel;
    private final JButton pauseResumeButton;

    // Animation progress variables (0.0 to 1.0 for each stage)
    private double progress = 0.0;
    private int currentStage = 0; // 0: Photosynthesis, 1: Herbivory, 2: Predation, 3: Decomposition

    // Node Positions
    private final Point sunPos = new Point(120, 100);
    private final Point co2Pos = new Point(280, 100);
    private final Point o2Pos = new Point(440, 100);
    private final Point plantPos = new Point(200, 420);
    private final Point herbivorePos = new Point(450, 420);
    private final Point carnivorePos = new Point(700, 420);
    private final Point soilPos = new Point(450, 620);

    public CyclePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Top Control & Chronograph Panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(30, 41, 59));
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        timeLabel = new JLabel("Elapsed Time: 00:00");
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        timeLabel.setForeground(Color.WHITE);

        pauseResumeButton = new JButton("Pause");
        pauseResumeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        pauseResumeButton.setFocusable(false);
        pauseResumeButton.addActionListener(e -> togglePauseResume());

        topPanel.add(timeLabel);
        topPanel.add(pauseResumeButton);
        add(topPanel, BorderLayout.NORTH);

        // Animation Timer (~60 FPS)
        animationTimer = new Timer(16, this);
        animationTimer.start();

        // Chronograph Timer (1 Second updates)
        chronographTimer = new Timer(1000, e -> {
            elapsedSeconds++;
            int mins = elapsedSeconds / 60;
            int secs = elapsedSeconds % 60;
            timeLabel.setText(String.format("Elapsed Time: %02d:%02d", mins, secs));
        });
        chronographTimer.start();
    }

    private void togglePauseResume() {
        if (isRunning) {
            animationTimer.stop();
            chronographTimer.stop();
            pauseResumeButton.setText("Resume");
        } else {
            animationTimer.start();
            chronographTimer.start();
            pauseResumeButton.setText("Pause");
        }
        isRunning = !isRunning;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        progress += 0.008; // Control animation speed
        if (progress >= 1.0) {
            progress = 0.0;
            currentStage = (currentStage + 1) % 4;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw Sky and Ground
        g2d.setColor(new Color(219, 234, 254));
        g2d.fillRect(0, 0, getWidth(), 500);
        g2d.setColor(new Color(187, 247, 208));
        g2d.fillRect(0, 500, getWidth(), getHeight() - 500);

        // 1. Draw Static Elements (Nodes)
        drawSun(g2d, sunPos.x, sunPos.y);
        drawGasCloud(g2d, co2Pos.x, co2Pos.y, "CO₂", new Color(148, 163, 184));
        drawGasCloud(g2d, o2Pos.x, o2Pos.y, "O₂ (Released)", new Color(56, 189, 248));
        drawPlant(g2d, plantPos.x, plantPos.y);
        drawAnimal(g2d, herbivorePos.x, herbivorePos.y, "Herbivore (Rabbit)", new Color(217, 119, 6));
        drawAnimal(g2d, carnivorePos.x, carnivorePos.y, "Carnivore (Wolf)", new Color(220, 38, 38));
        drawSoil(g2d, soilPos.x, soilPos.y);

        // 2. Draw Dynamic Carbon/Energy Flow Particles
        drawFlows(g2d);

        // 3. Status Legend Banner
        drawStatusBanner(g2d);
    }

    private void drawFlows(Graphics2D g2d) {
        switch (currentStage) {
            case 0: // Photosynthesis: Sun & CO2 -> Plant -> O2 released
                drawMovingParticle(g2d, sunPos, plantPos, progress, Color.YELLOW, 12, "Sunlight");
                drawMovingParticle(g2d, co2Pos, plantPos, progress, Color.GRAY, 10, "CO₂");
                if (progress > 0.4) {
                    drawMovingParticle(g2d, plantPos, o2Pos, (progress - 0.4) / 0.6, new Color(56, 189, 248), 10, "O₂");
                }
                break;
            case 1: // Herbivory: Plant Leaves -> Herbivore
                drawMovingParticle(g2d, plantPos, herbivorePos, progress, new Color(34, 197, 94), 14, "Organic Matter (Leaves)");
                break;
            case 2: // Predation: Herbivore -> Carnivore
                drawMovingParticle(g2d, herbivorePos, carnivorePos, progress, new Color(249, 115, 22), 14, "Energy/Carbon Transfer");
                break;
            case 3: // Decomposition: Carnivore -> Soil -> Plant
                drawMovingParticle(g2d, carnivorePos, soilPos, progress, new Color(120, 53, 15), 14, "Decomposition");
                drawMovingParticle(g2d, soilPos, plantPos, progress, new Color(161, 98, 7), 10, "Nutrients");
                break;
        }
    }

    private void drawMovingParticle(Graphics2D g2d, Point start, Point end, double p, Color color, int size, String label) {
        int x = (int) (start.x + p * (end.x - start.x));
        int y = (int) (start.y + p * (end.y - start.y));

        g2d.setColor(color);
        g2d.fill(new Ellipse2D.Double(x - size / 2.0, y - size / 2.0, size, size));
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 11));
        g2d.drawString(label, x + 10, y + 4);
    }

    private void drawSun(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(253, 224, 71));
        g2d.fillOval(x - 30, y - 30, 60, 60);
        g2d.setColor(new Color(234, 179, 8));
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.drawString("Sunlight", x - 22, y + 45);
    }

    private void drawGasCloud(Graphics2D g2d, int x, int y, String label, Color color) {
        g2d.setColor(color);
        g2d.fillOval(x - 35, y - 20, 70, 40);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2d.drawString(label, x - 18, y + 5);
    }

    private void drawPlant(Graphics2D g2d, int x, int y) {
        // Stem
        g2d.setColor(new Color(22, 101, 52));
        g2d.setStroke(new BasicStroke(6));
        g2d.drawLine(x, y, x, y - 60);

        // Leaves (Green Chlorophyll)
        g2d.setColor(new Color(34, 197, 94));
        g2d.fillOval(x - 40, y - 80, 40, 25);
        g2d.fillOval(x, y - 70, 40, 25);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2d.drawString("Green Plants", x - 40, y + 20);
        g2d.setFont(new Font("SansSerif", Font.ITALIC, 11));
        g2d.drawString("(Chlorophyll Synthesis)", x - 55, y + 35);
    }

    private void drawAnimal(Graphics2D g2d, int x, int y, String label, Color color) {
        g2d.setColor(color);
        g2d.fillRect(x - 35, y - 35, 70, 50);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2d.drawString(label, x - 50, y + 30);
    }

    private void drawSoil(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(120, 53, 15));
        g2d.fillRect(x - 100, y - 15, 200, 30);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2d.drawString("Soil & Decomposers", x - 65, y + 5);
    }

    private void drawStatusBanner(Graphics2D g2d) {
        String[] stageDescriptions = {
            "Stage 1: Photosynthesis — Sunlight + CO₂ convert into Green Leaves & O₂ is released.",
            "Stage 2: Herbivory — Herbivorous animals consume green plant leaves.",
            "Stage 3: Predation — Carnivorous animals consume Herbivorous animals.",
            "Stage 4: Decomposition — Carnivores die; decomposers turn organic matter into rich soil."
        };

        g2d.setColor(new Color(15, 23, 42, 220));
        g2d.fillRect(20, 640, getWidth() - 55, 40);

        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2d.drawString(stageDescriptions[currentStage], 35, 665);
    }
}