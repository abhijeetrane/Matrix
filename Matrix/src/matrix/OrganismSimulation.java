package matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Single-file Java Swing & Multithreading Simulation
 * Simulates Asexual Reproduction (Binary Fission / Mitosis) and Transition 
 * between Prokaryotes, Single-cell Eukaryotes, and Multicellular Organisms.
 */
public class OrganismSimulation extends JFrame {

    private final OrganismSimulationPanel simPanel;
    private final JLabel statsLabel;
    private final JButton pauseButton;
    private boolean isPaused = false;

    public OrganismSimulation() {
        setTitle("Asexual Reproduction & Evolution Simulation");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Simulation Canvas
        simPanel = new OrganismSimulationPanel();
        add(simPanel, BorderLayout.CENTER);

        // Top Control Panel
        JPanel controlPanel = new JPanel();
        
        JButton addEukaryoteBtn = new JButton("+ Eukaryote");
        addEukaryoteBtn.addActionListener(e -> simPanel.spawnEukaryote());

        JButton addProkaryoteBtn = new JButton("+ Prokaryote");
        addProkaryoteBtn.addActionListener(e -> simPanel.spawnProkaryote());

        JButton addMulticellularBtn = new JButton("+ Multicellular Cluster");
        addMulticellularBtn.addActionListener(e -> simPanel.spawnMulticellular());

        JButton clearBtn = new JButton("Clear All");
        clearBtn.addActionListener(e -> simPanel.clearOrganisms());

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> togglePause());

        controlPanel.add(addEukaryoteBtn);
        controlPanel.add(addProkaryoteBtn);
        controlPanel.add(addMulticellularBtn);
        controlPanel.add(clearBtn);
        controlPanel.add(pauseButton);

        add(controlPanel, BorderLayout.NORTH);

        // Bottom Stats Bar
        statsLabel = new JLabel(" Population Stats: ");
        statsLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        add(statsLabel, BorderLayout.SOUTH);

        // Timer to update stats bar in GUI thread
        Timer statsTimer = new Timer(200, e -> updateStats());
        statsTimer.start();
    }

    private void togglePause() {
        isPaused = !isPaused;
        simPanel.setPaused(isPaused);
        pauseButton.setText(isPaused ? "Resume" : "Pause");
    }

    private void updateStats() {
        int prokCount = 0;
        int eukCount = 0;
        int multiCount = 0;

        for (Organism org : simPanel.getOrganisms()) {
            switch (org.getType()) {
                case PROKARYOTE -> prokCount++;
                case EUKARYOTE -> eukCount++;
                case MULTICELLULAR -> multiCount++;
            }
        }

        statsLabel.setText(String.format(
            " Prokaryotes: %d  |  Eukaryotes: %d  |  Multicellular Clusters: %d  |  Total: %d",
            prokCount, eukCount, multiCount, simPanel.getOrganisms().size()
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrganismSimulation app = new OrganismSimulation();
            app.setVisible(true);
        });
    }
}

enum OrganismType {
    PROKARYOTE,
    EUKARYOTE,
    MULTICELLULAR
}

/**
 * Thread-safe Simulation Canvas running the core simulation thread loop.
 */
class OrganismSimulationPanel extends JPanel implements Runnable {

    private final List<Organism> organisms = new CopyOnWriteArrayList<>();
    private final Random random = new Random();
    private Thread simThread;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final int MAX_POPULATION = 150;

    public OrganismSimulationPanel() {
        setBackground(new Color(15, 23, 42)); // Dark oceanic background

        // Seed initial population
        for (int i = 0; i < 5; i++) spawnEukaryote();
        for (int i = 0; i < 5; i++) spawnProkaryote();

        // Start Multithreaded Simulation Engine
        simThread = new Thread(this, "Simulation-Engine");
        simThread.start();
    }

    public List<Organism> getOrganisms() {
        return organisms;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void spawnEukaryote() {
        if (organisms.size() < MAX_POPULATION) {
            int x = random.nextInt(Math.max(1, getWidth() - 40)) + 20;
            int y = random.nextInt(Math.max(1, getHeight() - 40)) + 20;
            organisms.add(new Organism(x, y, OrganismType.EUKARYOTE));
        }
    }

    public void spawnProkaryote() {
        if (organisms.size() < MAX_POPULATION) {
            int x = random.nextInt(Math.max(1, getWidth() - 40)) + 20;
            int y = random.nextInt(Math.max(1, getHeight() - 40)) + 20;
            organisms.add(new Organism(x, y, OrganismType.PROKARYOTE));
        }
    }

    public void spawnMulticellular() {
        if (organisms.size() < MAX_POPULATION) {
            int x = random.nextInt(Math.max(1, getWidth() - 40)) + 20;
            int y = random.nextInt(Math.max(1, getHeight() - 40)) + 20;
            organisms.add(new Organism(x, y, OrganismType.MULTICELLULAR));
        }
    }

    public void clearOrganisms() {
        organisms.clear();
    }

    @Override
    public void run() {
        while (running) {
            if (!paused) {
                int width = getWidth();
                int height = getHeight();

                // Process growth, movement, division, and death
                List<Organism> offspring = new CopyOnWriteArrayList<>();

                for (Organism org : organisms) {
                    org.update(width, height);

                    // Multithreaded lifecycle check: Division (Asexual Reproduction)
                    if (org.canDivide() && organisms.size() + offspring.size() < MAX_POPULATION) {
                        Organism child = org.reproduce();
                        if (child != null) {
                            offspring.add(child);
                        }
                    }
                }

                // Remove dead organisms and add offspring
                organisms.removeIf(Organism::isDead);
                organisms.addAll(offspring);
            }

            repaint(); // Trigger Swing GUI refresh

            try {
                Thread.sleep(30); // ~33 FPS simulation tick rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw organisms
        for (Organism org : organisms) {
            org.draw(g2);
        }
    }
}

/**
 * Organism Entity supporting movement, metabolic growth, and splitting (asexual reproduction).
 */
class Organism {
    private double x, y;
    private double dx, dy;
    private double radius;
    private double maxRadius;
    private int age = 0;
    private int maxAge;
    private OrganismType type;
    private final Random random = new Random();
    private int cellClusterCount = 1; // Used for multicellular growth stage

    public Organism(double x, double y, OrganismType type) {
        this.x = x;
        this.y = y;
        this.type = type;

        // Randomize initial velocities
        this.dx = (random.nextDouble() - 0.5) * 2.5;
        this.dy = (random.nextDouble() - 0.5) * 2.5;

        // Attributes based on biological complexity
        switch (type) {
            case PROKARYOTE -> {
                this.radius = 6;
                this.maxRadius = 14;
                this.maxAge = 400 + random.nextInt(200);
            }
            case EUKARYOTE -> {
                this.radius = 12;
                this.maxRadius = 24;
                this.maxAge = 600 + random.nextInt(300);
            }
            case MULTICELLULAR -> {
                this.radius = 18;
                this.maxRadius = 35;
                this.maxAge = 900 + random.nextInt(400);
                this.cellClusterCount = 3 + random.nextInt(4);
            }
        }
    }

    public OrganismType getType() {
        return type;
    }

    public void update(int panelWidth, int panelHeight) {
        age++;

        // Growth via nutrient absorption
        if (radius < maxRadius) {
            radius += 0.03;
        }

        // Brownian / Flagellar movement
        x += dx;
        y += dy;

        // Wall collisions
        if (panelWidth > 0 && panelHeight > 0) {
            if (x - radius < 0 || x + radius > panelWidth) {
                dx *= -1;
                x = Math.max(radius, Math.min(x, panelWidth - radius));
            }
            if (y - radius < 0 || y + radius > panelHeight) {
                dy *= -1;
                y = Math.max(radius, Math.min(y, panelHeight - radius));
            }
        }

        // Slight movement direction changes
        if (random.nextDouble() < 0.05) {
            dx += (random.nextDouble() - 0.5) * 0.5;
            dy += (random.nextDouble() - 0.5) * 0.5;
            // Clamp speed
            dx = Math.max(-2, Math.min(2, dx));
            dy = Math.max(-2, Math.min(2, dy));
        }
    }

    public boolean canDivide() {
        return radius >= maxRadius && age < maxAge * 0.8;
    }

    public boolean isDead() {
        return age >= maxAge;
    }

    /**
     * Asexual Reproduction logic with evolutionary transitions.
     */
    public Organism reproduce() {
        // Shrink parent back to initial size after splitting
        this.radius /= 1.6;

        double offX = x + (random.nextDouble() - 0.5) * 20;
        double offY = y + (random.nextDouble() - 0.5) * 20;

        // Evolutionary chance during asexual division
        double evoRoll = random.nextDouble();

        if (type == OrganismType.EUKARYOTE) {
            if (evoRoll < 0.25) {
                // Eukaryote simplifies / degrades to Prokaryotic strain
                return new Organism(offX, offY, OrganismType.PROKARYOTE);
            } else if (evoRoll > 0.80) {
                // Eukaryote forms a Multicellular colony
                return new Organism(offX, offY, OrganismType.MULTICELLULAR);
            } else {
                // Mitosis -> Identical Eukaryote
                return new Organism(offX, offY, OrganismType.EUKARYOTE);
            }
        } else if (type == OrganismType.PROKARYOTE) {
            if (evoRoll > 0.88) {
                // Prokaryote endosymbiosis / mutation into Eukaryote
                return new Organism(offX, offY, OrganismType.EUKARYOTE);
            } else {
                // Binary Fission -> Identical Prokaryote
                return new Organism(offX, offY, OrganismType.PROKARYOTE);
            }
        } else {
            // Multicellular fragmentation / Budding
            return new Organism(offX, offY, OrganismType.MULTICELLULAR);
        }
    }

    public void draw(Graphics2D g2) {
        int drawX = (int) (x - radius);
        int drawY = (int) (y - radius);
        int diameter = (int) (radius * 2);

        switch (type) {
            case PROKARYOTE -> {
                // Small green capsule / oval without membrane-bound nucleus
                g2.setColor(new Color(34, 197, 94, 200));
                g2.fillOval(drawX, drawY, diameter, diameter);
                g2.setColor(new Color(134, 239, 172));
                g2.drawOval(drawX, drawY, diameter, diameter);

                // Nucleoid (DNA strand)
                g2.setColor(Color.WHITE);
                g2.drawArc((int) x - 2, (int) y - 2, 4, 4, 0, 270);
            }
            case EUKARYOTE -> {
                // Larger cyan cell with distinct cell wall and inner nucleus
                g2.setColor(new Color(6, 182, 212, 180));
                g2.fillOval(drawX, drawY, diameter, diameter);
                g2.setColor(new Color(165, 243, 252));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(drawX, drawY, diameter, diameter);

                // Prominent Nucleus
                int nucDim = (int) (radius * 0.8);
                g2.setColor(new Color(236, 72, 153)); // Pink nucleus
                g2.fillOval((int) (x - nucDim / 2.0), (int) (y - nucDim / 2.0), nucDim, nucDim);
            }
            case MULTICELLULAR -> {
                // Cluster of bound cells moving together
                g2.setColor(new Color(168, 85, 247, 180)); // Purple cluster
                for (int i = 0; i < cellClusterCount; i++) {
                    double angle = (2 * Math.PI / cellClusterCount) * i;
                    int subX = (int) (x + Math.cos(angle) * (radius * 0.5) - radius * 0.4);
                    int subY = (int) (y + Math.sin(angle) * (radius * 0.5) - radius * 0.4);
                    int subDim = (int) (radius * 0.8);

                    g2.fillOval(subX, subY, subDim, subDim);
                    g2.setColor(new Color(233, 213, 255));
                    g2.drawOval(subX, subY, subDim, subDim);
                    g2.setColor(new Color(168, 85, 247, 180));
                }
            }
        }
    }
}