package matrix.evolution;

import javax.swing.*;
import java.awt.*;

public class PlantEvolutionApp extends JFrame {

    public PlantEvolutionApp() {
        setTitle("Botany: Evolution of Plants");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 49, 34));
        JLabel titleLabel = new JLabel("Evolutionary Journey of Kingdom Plantae");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Evolutionary Stages Data
        EvolutionStage[] stages = {
            new EvolutionStage(
                "1. Single-Celled Eukaryotes (~1.6 Billion Years Ago)",
                "Unicellular Green Algae (e.g., Chlorophyta ancestors)",
                "• Primary endosymbiosis: Heterotrophic eukaryote engulfed a photosynthetic cyanobacterium.\n" +
                "• Origin of chloroplasts containing chlorophyll a and b.\n" +
                "• Aquatic existence; no specialized tissues or protection against desiccation.",
                new Color(130, 201, 30)
            ),
            new EvolutionStage(
                "2. Bryophytes / Non-Vascular Plants (~470 Million Years Ago)",
                "Liverworts, Mosses, and Hornworts",
                "• First transition of photosynthetic organisms onto land.\n" +
                "• Developed a waxy cuticle to reduce water loss and protective jacket for gametes.\n" +
                "• Lacking vascular tissue (xylem/phloem); restricted to damp environments and low heights.",
                new Color(76, 175, 80)
            ),
            new EvolutionStage(
                "3. Seedless Vascular Plants (~420 Million Years Ago)",
                "Ferns, Horsetails, and Lycophytes",
                "• Evolution of true vascular tissues: Xylem (water transport + structural lignin) and Phloem (nutrient transport).\n" +
                "• True roots, stems, and leaves evolved, allowing plants to grow tall.\n" +
                "• Flagellated sperm still required liquid water for fertilization.",
                new Color(46, 125, 50)
            ),
            new EvolutionStage(
                "4. Gymnosperms / Seed Plants (~360 Million Years Ago)",
                "Conifers, Cycads, Ginkgos",
                "• Evolution of the **seed**: protects embryo, provides nutrients, allows dormancy.\n" +
                "• Evolution of **pollen**: wind-borne male gametophytes eliminate the need for water during fertilization.\n" +
                "• Secondary growth (wood) enabled massive arboreal structures.",
                new Color(27, 94, 32)
            ),
            new EvolutionStage(
                "5. Angiosperms / Flowering Plants (~140 Million Years Ago)",
                "Monocots and Dicots (Flowering Plants)",
                "• Evolution of **flowers** to attract animal pollinators for efficient fertilization.\n" +
                "• Evolution of **fruit** enclosing seeds to enhance seed dispersal.\n" +
                "• Double fertilization mechanism leading to nutrient-rich endosperm.",
                new Color(156, 39, 176)
            )
        };

        // UI Components
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));

        for (EvolutionStage stage : stages) {
            tabbedPane.addTab(stage.shortTitle, createStagePanel(stage));
        }

        // Main Layout
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createStagePanel(EvolutionStage stage) {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Text Info Panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(stage.title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(stage.themeColor.darker());

        JLabel exampleLabel = new JLabel("Examples: " + stage.examples);
        exampleLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        exampleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        JTextArea descArea = new JTextArea(stage.description);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(panel.getBackground());

        textPanel.add(titleLabel);
        textPanel.add(exampleLabel);
        textPanel.add(descArea);

        // Canvas Panel for Visual Diagram
        DiagramCanvas canvas = new DiagramCanvas(stage);
        canvas.setPreferredSize(new Dimension(350, 400));
        canvas.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(canvas, BorderLayout.EAST);

        return panel;
    }

    // Class to represent each evolutionary milestone
    private static class EvolutionStage {
        String shortTitle;
        String title;
        String examples;
        String description;
        Color themeColor;

        EvolutionStage(String title, String examples, String description, Color themeColor) {
            this.title = title;
            this.shortTitle = title.split(":")[0].replaceAll("\\(.*?\\)", "").trim();
            this.examples = examples;
            this.description = description;
            this.themeColor = themeColor;
        }
    }

    // Custom Canvas to render schematic visuals for each stage
    private static class DiagramCanvas extends JPanel {
        private final EvolutionStage stage;

        DiagramCanvas(EvolutionStage stage) {
            this.stage = stage;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            switch (stage.shortTitle) {
                case "1. Single-Celled Eukaryotes":
                    // Draw cell wall & membrane
                    g2.setColor(stage.themeColor);
                    g2.fillOval(w / 2 - 80, h / 2 - 80, 160, 160);
                    // Draw nucleus
                    g2.setColor(new Color(220, 50, 50));
                    g2.fillOval(w / 2 - 25, h / 2 - 25, 50, 50);
                    // Draw chloroplasts
                    g2.setColor(new Color(30, 140, 30));
                    g2.fillOval(w / 2 - 60, h / 2 - 40, 25, 15);
                    g2.fillOval(w / 2 + 35, h / 2 + 20, 25, 15);
                    g2.fillOval(w / 2 - 40, h / 2 + 40, 25, 15);
                    // Label
                    g2.setColor(Color.BLACK);
                    g2.drawString("Chloroplasts", w / 2 - 70, h / 2 - 50);
                    g2.drawString("Nucleus", w / 2 - 22, h / 2 + 40);
                    break;

                case "2. Bryophytes / Non-Vascular Plants":
                    // Ground
                    g2.setColor(new Color(110, 70, 40));
                    g2.fillRect(0, h - 50, w, 50);
                    // Moss Tufts
                    g2.setColor(stage.themeColor);
                    for (int i = 50; i < w - 50; i += 30) {
                        g2.fillArc(i, h - 90, 40, 50, 0, 180);
                    }
                    // Rhizoids (primitive roots)
                    g2.setColor(Color.BLACK);
                    for (int i = 60; i < w - 50; i += 30) {
                        g2.drawLine(i, h - 50, i - 5, h - 30);
                        g2.drawLine(i + 10, h - 50, i + 15, h - 30);
                    }
                    break;

                case "3. Seedless Vascular Plants":
                    // Ground
                    g2.setColor(new Color(110, 70, 40));
                    g2.fillRect(0, h - 50, w, 50);
                    // Vascular Stem
                    g2.setColor(new Color(100, 60, 20));
                    g2.setStroke(new BasicStroke(6));
                    g2.drawLine(w / 2, h - 50, w / 2, h - 220);
                    // Fern Fronds
                    g2.setColor(stage.themeColor);
                    g2.setStroke(new BasicStroke(2));
                    for (int y = h - 200; y < h - 70; y += 30) {
                        g2.drawLine(w / 2, y, w / 2 - 60, y - 30);
                        g2.drawLine(w / 2, y, w / 2 + 60, y - 30);
                    }
                    break;

                case "4. Gymnosperms / Seed Plants":
                    // Tree Trunk
                    g2.setColor(new Color(80, 50, 20));
                    g2.fillRect(w / 2 - 20, h - 220, 40, 170);
                    // Pine Tree Needles (Triangle Foliage)
                    g2.setColor(stage.themeColor);
                    int[] xPoints = {w / 2, w / 2 - 90, w / 2 + 90};
                    int[] yPoints1 = {h - 320, h - 200, h - 200};
                    int[] yPoints2 = {h - 260, h - 140, h - 140};
                    g2.fillPolygon(xPoints, yPoints1, 3);
                    g2.fillPolygon(xPoints, yPoints2, 3);
                    // Pine Cone (Seed Representation)
                    g2.setColor(new Color(139, 69, 19));
                    g2.fillOval(w / 2 + 30, h - 170, 20, 30);
                    break;

                case "5. Angiosperms / Flowering Plants":
                    // Stem
                    g2.setColor(new Color(46, 125, 50));
                    g2.setStroke(new BasicStroke(5));
                    g2.drawLine(w / 2, h - 50, w / 2, h - 180);
                    // Petals
                    g2.setColor(new Color(230, 81, 0));
                    for (int i = 0; i < 360; i += 45) {
                        double rad = Math.toRadians(i);
                        int px = (int) (w / 2 + 40 * Math.cos(rad));
                        int py = (int) (h - 180 + 40 * Math.sin(rad));
                        g2.fillOval(px - 15, py - 15, 30, 30);
                    }
                    // Flower Center (Stamen/Carpel region)
                    g2.setColor(Color.YELLOW);
                    g2.fillOval(w / 2 - 20, h - 200, 40, 40);
                    // Fruit/Seed Container
                    g2.setColor(new Color(205, 220, 57));
                    g2.fillOval(w / 2 - 80, h - 120, 35, 35);
                    g2.setColor(Color.BLACK);
                    g2.drawString("Fruit", w / 2 - 75, h - 70);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlantEvolutionApp app = new PlantEvolutionApp();
            app.setVisible(true);
        });
    }
}