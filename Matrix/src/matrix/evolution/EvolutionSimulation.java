package matrix.evolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EvolutionSimulation extends JFrame {

    enum EvolutionaryStage {
        UNDIFFERENTIATED("1. Undifferentiated Multicellular Life", 
            "Organisms are hermaphroditic/undifferentiated. Chromosomes are homomorphic (X-like proto-sex chromosomes)."),
        MUTATION_TO_Y("2. X -> Y Mutation (Male Suppression/Promotion)", 
            "A proto-X chromosome mutates into a Y chromosome acquiring GSF (female suppression) and SPF (stamen promotion)."),
        DIOECIOUS_PLANTS("3. Dioecious Plant Emergence (XX Female & XY Male)", 
            "Distinct male (XY, stamen/pollen) and female (XX, carpel/ovule) plants develop in the plant kingdom."),
        BOTANICAL_POLLINATION("4. Botanical Reproduction (Pollen to Ovule)", 
            "Male pollen (carrying X or Y gametes) fertilizes female ovules (carrying X gametes) to produce seeds.");

        final String title;
        final String description;

        EvolutionaryStage(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

    private EvolutionaryStage currentStage = EvolutionaryStage.UNDIFFERENTIATED;
    private final SimulationPanel simPanel;
    private final JLabel titleLabel;
    private final JTextArea infoTextArea;

    public EvolutionSimulation() {
        setTitle("Botanical Biology: X -> Y Chromosome Evolution Simulator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 44, 52));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        titleLabel = new JLabel(currentStage.title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        infoTextArea = new JTextArea(currentStage.description);
        infoTextArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        infoTextArea.setForeground(new Color(200, 210, 220));
        infoTextArea.setBackground(new Color(40, 44, 52));
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setLineWrap(true);
        infoTextArea.setEditable(false);
        infoTextArea.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        headerPanel.add(infoTextArea, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // Simulation Canvas
        simPanel = new SimulationPanel();
        add(simPanel, BorderLayout.CENTER);

        // Control Footer Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(230, 235, 240));

        JButton prevBtn = new JButton("<< Previous Stage");
        JButton nextBtn = new JButton("Next Stage >>");

        prevBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        nextBtn.setFont(new Font("SansSerif", Font.BOLD, 13));

        prevBtn.addActionListener(e -> changeStage(-1));
        nextBtn.addActionListener(e -> changeStage(1));

        controlPanel.add(prevBtn);
        controlPanel.add(nextBtn);
        add(controlPanel, BorderLayout.SOUTH);

        // Timer for animation ticks
        Timer timer = new Timer(30, e -> simPanel.updateSimulation());
        timer.start();
    }

    private void changeStage(int delta) {
        EvolutionaryStage[] stages = EvolutionaryStage.values();
        int newOrdinal = currentStage.ordinal() + delta;
        if (newOrdinal >= 0 && newOrdinal < stages.length) {
            currentStage = stages[newOrdinal];
            titleLabel.setText(currentStage.title);
            infoTextArea.setText(currentStage.description);
            simPanel.resetForStage(currentStage);
        }
    }

    // Canvas class handling drawing and animation
    class SimulationPanel extends JPanel {
        private final List<Cell> cells = new ArrayList<>();
        private final List<PollenParticle> pollenList = new ArrayList<>();
        private final Random random = new Random();
        private int animationStep = 0;

        public SimulationPanel() {
            setBackground(new Color(245, 247, 250));
            resetForStage(EvolutionaryStage.UNDIFFERENTIATED);
        }

        public void resetForStage(EvolutionaryStage stage) {
            cells.clear();
            pollenList.clear();
            animationStep = 0;

            int width = 900;
            int height = 450;

            switch (stage) {
                case UNDIFFERENTIATED:
                    // Spawn hermaphroditic/ancestral cells
                    for (int i = 0; i < 18; i++) {
                        int x = 100 + random.nextInt(width - 200);
                        int y = 50 + random.nextInt(height - 150);
                        cells.add(new Cell(x, y, CellType.HERMAPHRODITE, "XX (Proto)"));
                    }
                    break;

                case MUTATION_TO_Y:
                    // Spawn mostly XX and a mutating cell turning into XY
                    for (int i = 0; i < 12; i++) {
                        int x = 80 + (i % 6) * 130;
                        int y = 80 + (i / 6) * 160;
                        CellType type = (i == 3 || i == 8) ? CellType.MUTATING : CellType.FEMALE;
                        String gen = (type == CellType.MUTATING) ? "X -> Y Mutating" : "XX Female";
                        cells.add(new Cell(x, y, type, gen));
                    }
                    break;

                case DIOECIOUS_PLANTS:
                    // Separate Male (XY) and Female (XX) Plant structures
                    // Female Plant on Left
                    for (int i = 0; i < 8; i++) {
                        cells.add(new Cell(180 + random.nextInt(100), 120 + random.nextInt(180), CellType.FEMALE, "XX Female"));
                    }
                    // Male Plant on Right
                    for (int i = 0; i < 8; i++) {
                        cells.add(new Cell(580 + random.nextInt(100), 120 + random.nextInt(180), CellType.MALE, "XY Male"));
                    }
                    break;

                case BOTANICAL_POLLINATION:
                    // Male flower releasing pollen to Female flower pistil
                    for (int i = 0; i < 30; i++) {
                        pollenList.add(new PollenParticle(620, 180, 220, 240));
                    }
                    break;
            }
        }

        public void updateSimulation() {
            animationStep++;
            for (Cell c : cells) {
                c.update(animationStep);
            }
            for (PollenParticle p : pollenList) {
                p.update();
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            switch (currentStage) {
                case UNDIFFERENTIATED:
                    drawStage1(g2);
                    break;
                case MUTATION_TO_Y:
                    drawStage2(g2);
                    break;
                case DIOECIOUS_PLANTS:
                    drawStage3(g2);
                    break;
                case BOTANICAL_POLLINATION:
                    drawStage4(g2);
                    break;
            }
        }

        private void drawStage1(Graphics2D g2) {
            for (Cell c : cells) {
                c.draw(g2);
            }
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif", Font.ITALIC, 14));
            g2.drawString("Ancestral multicellular organisms carry identical proto-sex chromosomes.", 50, getHeight() - 30);
        }

        private void drawStage2(Graphics2D g2) {
            for (Cell c : cells) {
                c.draw(g2);
                if (c.type == CellType.MUTATING) {
                    g2.setColor(new Color(200, 0, 0));
                    g2.drawString("GSF + SPF Mutation!", c.x - 30, c.y - 25);
                }
            }
        }

        private void drawStage3(Graphics2D g2) {
            // Draw Female Plant Container
            g2.setColor(new Color(230, 245, 230));
            g2.fillRoundRect(120, 60, 260, 320, 20, 20);
            g2.setColor(new Color(40, 120, 40));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(120, 60, 260, 320, 20, 20);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.drawString("Female Plant (Dioecious)", 150, 90);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("Organ: Carpel / Ovule (XX)", 150, 110);

            // Draw Male Plant Container
            g2.setColor(new Color(230, 235, 250));
            g2.fillRoundRect(520, 60, 260, 320, 20, 20);
            g2.setColor(new Color(40, 60, 150));
            g2.drawRoundRect(520, 60, 260, 320, 20, 20);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.drawString("Male Plant (Dioecious)", 550, 90);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("Organ: Stamen / Anther (XY)", 550, 110);

            for (Cell c : cells) {
                c.draw(g2);
            }
        }

        private void drawStage4(Graphics2D g2) {
            // Female Flower Structure
            g2.setColor(new Color(200, 100, 150));
            g2.fillOval(180, 180, 80, 120); // Pistil/Carpel
            g2.setColor(Color.BLACK);
            g2.drawOval(180, 180, 80, 120);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Female Pistil (XX)", 165, 330);

            // Male Flower Structure
            g2.setColor(new Color(220, 180, 50));
            g2.fillRect(600, 160, 40, 140); // Anther/Stamen
            g2.setColor(Color.BLACK);
            g2.drawRect(600, 160, 40, 140);
            g2.drawString("Male Anther (XY)", 575, 330);

            // Pollen Particles
            for (PollenParticle p : pollenList) {
                p.draw(g2);
            }

            // Explanatory Banner
            g2.setColor(new Color(50, 50, 50));
            g2.setFont(new Font("SansSerif", Font.ITALIC, 13));
            g2.drawString("Wind/Insect Pollination carries male pollen (X or Y gametes) to female ovules (X) to create seeds.", 150, 400);
        }
    }

    enum CellType { HERMAPHRODITE, FEMALE, MALE, MUTATING }

    // Internal class representing individual cells/chromosomal bodies
    static class Cell {
        int x, y;
        CellType type;
        String genotype;

        public Cell(int x, int y, CellType type, String genotype) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.genotype = genotype;
        }

        public void update(int step) {
            // Subtle floating motion
            x += (int) (Math.sin(step * 0.05 + x) * 1.2);
            y += (int) (Math.cos(step * 0.05 + y) * 1.2);
        }

        public void draw(Graphics2D g2) {
            int radius = 38;

            switch (type) {
                case HERMAPHRODITE:
                    g2.setColor(new Color(180, 140, 200));
                    break;
                case FEMALE:
                    g2.setColor(new Color(240, 120, 160));
                    break;
                case MALE:
                    g2.setColor(new Color(100, 150, 240));
                    break;
                case MUTATING:
                    g2.setColor(new Color(230, 80, 50));
                    break;
            }

            g2.fillOval(x - radius / 2, y - radius / 2, radius, radius);
            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(x - radius / 2, y - radius / 2, radius, radius);

            // Draw Chromosome label inside
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.drawString(genotype, x - 22, y + 4);
        }
    }

    // Class simulating pollen movement from Stamen to Carpel
    static class PollenParticle {
        float x, y;
        final float targetX, targetY;
        final float speed;

        public PollenParticle(float startX, float startY, float targetX, float targetY) {
            this.x = startX;
            this.y = startY + (float) (Math.random() * 60 - 30);
            this.targetX = targetX;
            this.targetY = targetY;
            this.speed = 1.5f + (float) Math.random() * 2.0f;
        }

        public void update() {
            x -= speed;
            y += (float) (Math.sin(x * 0.05) * 1.5);
            if (x < targetX) {
                x = 620; // reset to male anther
            }
        }

        public void draw(Graphics2D g2) {
            g2.setColor(new Color(240, 200, 0));
            g2.fillOval((int) x, (int) y, 7, 7);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EvolutionSimulation sim = new EvolutionSimulation();
            sim.setLocationRelativeTo(null);
            sim.setVisible(true);
        });
    }
}