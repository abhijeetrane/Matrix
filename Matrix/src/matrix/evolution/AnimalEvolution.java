package matrix.evolution;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalEvolution extends JFrame {

    static class EvolutionStage {
        String name;
        String period;
        String innovation;
        String example;
        String description;
        Color color;
        int complexity;

        EvolutionStage(String name, String period, String innovation, String example, String description, Color color, int complexity) {
            this.name = name;
            this.period = period;
            this.innovation = innovation;
            this.example = example;
            this.description = description;
            this.color = color;
            this.complexity = complexity;
        }
    }

    private final List<EvolutionStage> stages = new ArrayList<>();
    private int currentStageIndex = 0;
    private JSlider timelineSlider;
    private EvolutionPanel evolutionPanel;
    private JLabel nameLabel, periodLabel, innovationLabel, exampleLabel;
    private JTextArea descArea;
    private Timer autoTimer;

    public AnimalEvolution() {
        setTitle("Zoology: Evolution of Animal Kingdom from Eukaryotic Cell");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initStages();
        initUI();
        updateStage(0);
    }

    private void initStages() {
        stages.add(new EvolutionStage(
                "1. Single-Celled Eukaryote (Protozoa)",
                "~1.8 Billion Years Ago - Proterozoic",
                "True Nucleus, Mitochondria, Mitosis",
                "Amoeba, Paramecium, Euglena",
                "The first eukaryotic cell. Unlike prokaryotes, it has membrane-bound nucleus and organelles. All animals originated from a flagellated protist ancestor similar to Choanoflagellates.",
                new Color(100, 180, 255), 1));

        stages.add(new EvolutionStage(
                "2. Colonial Protists",
                "~1.2 Billion Years Ago",
                "Cellular Cooperation & Division of Labour",
                "Volvox, Choanoflagellate colony",
                "Cells stay together after division. Some cells specialize for reproduction, others for movement. This is the first step towards multicellularity.",
                new Color(80, 200, 220), 2));

        stages.add(new EvolutionStage(
                "3. Porifera - Sponges (Parazoa)",
                "~760 MYA - Pre-Cambrian",
                "Multicellularity, No True Tissues",
                "Sycon, Spongilla",
                "First true animals. Simplest body plan - cellular level organization, choanocytes for feeding, ostia and osculum. No nerves or muscles.",
                new Color(255, 220, 100), 3));

        stages.add(new EvolutionStage(
                "4. Cnidaria - Tissue Grade",
                "~580 MYA - Ediacaran",
                "True Tissues, Radial Symmetry, Cnidoblasts",
                "Hydra, Jellyfish, Corals",
                "Diploblastic with two germ layers (ectoderm, endoderm). First nervous system (nerve net), first muscle cells. Alternation of Polyp and Medusa forms.",
                new Color(255, 130, 130), 4));

        stages.add(new EvolutionStage(
                "5. Platyhelminthes - Flatworms",
                "~550 MYA - Cambrian",
                "Bilateral Symmetry, Triploblastic, Acoelomate",
                "Planaria, Liver Fluke, Tapeworm",
                "First bilaterians. Cephalization begins (brain concentration). Mesoderm appears. Incomplete gut, excretory system with flame cells.",
                new Color(200, 150, 255), 5));

        stages.add(new EvolutionStage(
                "6. Annelida - Segmented Worms",
                "~540 MYA - Cambrian Explosion",
                "True Coelom, Metameric Segmentation",
                "Earthworm, Leech, Nereis",
                "First true coelomates. Closed circulatory system, complete gut, metanephridia. Segmentation allows specialized body regions - a major evolutionary leap.",
                new Color(180, 100, 80), 6));

        stages.add(new EvolutionStage(
                "7. Arthropoda - Jointed Legs",
                "~530 MYA - Cambrian",
                "Chitinous Exoskeleton, Jointed Appendages",
                "Insects, Crabs, Spiders, Prawn",
                "Most successful phylum. Open circulatory system, compound eyes, molting (ecdysis). Highly developed nervous system and sensory organs.",
                new Color(90, 90, 90), 7));

        stages.add(new EvolutionStage(
                "8. Mollusca - Soft Bodied",
                "~540 MYA",
                "Mantle, Shell, Muscular Foot",
                "Pila, Octopus, Unio",
                "Second largest phylum. Body divided into head, visceral mass and foot. Radula present, reduced coelom. High intelligence in Cephalopods.",
                new Color(255, 180, 200), 7));

        stages.add(new EvolutionStage(
                "9. Echinodermata - Spiny Skinned",
                "~520 MYA",
                "Water Vascular System, Deuterostome, Radial in Adult",
                "Starfish, Sea Urchin",
                "Exclusive marine, link between invertebrates and chordates. Deuterostome development like vertebrates. Amazing power of regeneration.",
                new Color(255, 150, 50), 8));

        stages.add(new EvolutionStage(
                "10. Protochordata & Pisces",
                "~500 MYA - Ordovician",
                "Notochord, Dorsal Nerve Cord, Gill Slits",
                "Branchiostoma, Scoliodon (Shark), Labeo",
                "First chordates. Notochord provides skeletal support. Agnatha (jawless) to Gnathostomata (jawed fishes). Origin of vertebral column.",
                new Color(100, 200, 100), 9));

        stages.add(new EvolutionStage(
                "11. Amphibia - Water to Land",
                "~365 MYA - Devonian",
                "Lungs, Tetrapod Limbs, Transition to Land",
                "Frog, Salamander, Ichthyophis",
                "First tetrapods. Can live both in water and land. Moist skin for cutaneous respiration, three-chambered heart. Larvae still aquatic.",
                new Color(80, 180, 80), 10));

        stages.add(new EvolutionStage(
                "12. Reptilia - True Land Vertebrates",
                "~320 MYA - Carboniferous",
                "Amniotic Egg, Dry Scaly Skin, Internal Fertilization",
                "Lizard, Snake, Crocodile, Turtle",
                "First fully terrestrial vertebrates due to cleidoic (amniotic) egg. No need to return to water for breeding. Cold-blooded, efficient lungs.",
                new Color(60, 130, 60), 11));

        stages.add(new EvolutionStage(
                "13. Aves & Mammalia - Warm Blooded",
                "~160 MYA & 200 MYA - Jurassic",
                "Endothermy, Feathers/Hair, 4-Chambered Heart",
                "Pigeon, Crow, Human, Whale, Bat",
                "Highest evolution. Aves from reptiles for flight - feathers, pneumatic bones. Mammalia - mammary glands, hair, highly developed brain (neocortex). Dominance of land, air and sea.",
                new Color(255, 100, 100), 12));
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Top Title
        JLabel title = new JLabel("Evolution of Animal Kingdom: From Eukaryote to Mammals", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10,0,5,0));
        add(title, BorderLayout.NORTH);

        // Center - Drawing Panel
        evolutionPanel = new EvolutionPanel();
        add(evolutionPanel, BorderLayout.CENTER);

        // Bottom - Controls
        JPanel bottomPanel = new JPanel(new BorderLayout());
        timelineSlider = new JSlider(0, stages.size() - 1, 0);
        timelineSlider.setMajorTickSpacing(1);
        timelineSlider.setPaintTicks(true);
        timelineSlider.setPaintLabels(true);
        timelineSlider.setSnapToTicks(true);
        timelineSlider.addChangeListener(this::onSliderChanged);

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setBorder(BorderFactory.createTitledBorder("Geological Timeline Slider - Drag to Evolve"));
        sliderPanel.add(new JLabel("Protozoa"), BorderLayout.WEST);
        sliderPanel.add(timelineSlider, BorderLayout.CENTER);
        sliderPanel.add(new JLabel("Mammalia"), BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        JButton autoBtn = new JButton("▶ Auto Evolve");
        JButton resetBtn = new JButton("Reset");
        buttonPanel.add(autoBtn);
        buttonPanel.add(resetBtn);

        autoBtn.addActionListener(e -> startAutoEvolve());
        resetBtn.addActionListener(e -> {
            if (autoTimer != null) autoTimer.stop();
            updateStage(0);
        });

        bottomPanel.add(sliderPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Right - Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(350, 0));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Zoological Details"),
                BorderFactory.createEmptyBorder(10,10,10,10)));
        infoPanel.setBackground(new Color(250, 250, 240));

        nameLabel = new JLabel();
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        periodLabel = new JLabel();
        periodLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        innovationLabel = new JLabel();
        exampleLabel = new JLabel();
        descArea = new JTextArea(5, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(new Color(250, 250, 240));
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 13));

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(periodLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(innovationLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(exampleLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(new JSeparator());
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(new JLabel("Description:"));
        infoPanel.add(new JScrollPane(descArea));

        add(infoPanel, BorderLayout.EAST);
    }

    private void onSliderChanged(ChangeEvent e) {
        if (!timelineSlider.getValueIsAdjusting() || true) {
            updateStage(timelineSlider.getValue());
        }
    }

    private void updateStage(int index) {
        currentStageIndex = index;
        EvolutionStage s = stages.get(index);
        nameLabel.setText("<html><b>" + s.name + "</b></html>");
        periodLabel.setText("Period: " + s.period);
        innovationLabel.setText("<html><b>Key Innovation:</b> " + s.innovation + "</html>");
        exampleLabel.setText("<html><b>Examples:</b> " + s.example + "</html>");
        descArea.setText(s.description);
        timelineSlider.setValue(index);
        evolutionPanel.setStage(s, index);
    }

    private void startAutoEvolve() {
        if (autoTimer != null && autoTimer.isRunning()) {
            autoTimer.stop();
            return;
        }
        autoTimer = new Timer(1500, e -> {
            int next = currentStageIndex + 1;
            if (next >= stages.size()) {
                ((Timer) e.getSource()).stop();
            } else {
                updateStage(next);
            }
        });
        autoTimer.start();
    }

    class EvolutionPanel extends JPanel {
        private EvolutionStage stage;
        private int stageIndex;
        private double animScale = 0;

        EvolutionPanel() {
            setBackground(Color.WHITE);
            Timer t = new Timer(16, e -> {
                animScale += 0.05;
                repaint();
            });
            t.start();
        }

        void setStage(EvolutionStage stage, int index) {
            this.stage = stage;
            this.stageIndex = index;
            this.animScale = 0;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (stage == null) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Draw phylogenetic tree line
            g2.setColor(new Color(220, 220, 220));
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(50, h/2, w-50, h/2);

            // Draw progress
            double progress = (double) stageIndex / (stages.size() - 1);
            g2.setColor(new Color(100, 150, 255, 150));
            g2.setStroke(new BasicStroke(6));
            g2.drawLine(50, h/2, (int)(50 + (w-100)*progress), h/2);

            // Draw current organism
            int cx = w/2;
            int cy = h/2;
            drawOrganism(g2, stage, cx, cy);

            // Draw evolution arrow labels
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString("SIMPLE", 60, h/2 - 40);
            g2.drawString("COMPLEX →", w - 110, h/2 - 40);
        }

        private void drawOrganism(Graphics2D g2, EvolutionStage s, int cx, int cy) {
            double pulse = 1 + 0.1 * Math.sin(animScale);
            int size = 80 + s.complexity * 12;
            int drawSize = (int)(size * pulse);

            g2.setColor(s.color);
            // Shadow
            g2.setColor(new Color(0,0,0,30));
            g2.fillOval(cx - drawSize/2 + 5, cy - drawSize/2 + 5, drawSize, drawSize);
            g2.setColor(s.color);

            switch (stageIndex) {
                case 0: // single cell
                    g2.fillOval(cx - drawSize/2, cy - drawSize/2, drawSize, drawSize);
                    g2.setColor(Color.BLACK);
                    g2.fillOval(cx - 15, cy - 15, 30, 30); // nucleus
                    g2.setColor(Color.WHITE);
                    g2.drawString("Nucleus", cx - 20, cy + 5);
                    break;
                case 1: // colony
                    for (int i=0;i<8;i++){
                        double ang = i * Math.PI/4 + animScale*0.2;
                        int x = (int)(cx + Math.cos(ang)*40);
                        int y = (int)(cy + Math.sin(ang)*40);
                        g2.fillOval(x-20, y-20, 40, 40);
                    }
                    break;
                case 2: // sponge
                    g2.fillRoundRect(cx-drawSize/2, cy-drawSize/2, drawSize, drawSize, 20, 20);
                    g2.setColor(Color.BLACK);
                    for(int i=0;i<6;i++) g2.fillOval(cx-30 + i*12, cy, 5, 15);
                    break;
                case 3: // jellyfish
                    g2.fillArc(cx-drawSize/2, cy-drawSize/2, drawSize, drawSize/2, 0, 180);
                    for(int i=0;i<5;i++){
                        int x = cx - drawSize/2 + 15 + i*20;
                        g2.drawLine(x, cy, x, cy + 40 + (int)(10*Math.sin(animScale+i)));
                    }
                    break;
                case 4: // flatworm
                    g2.fillOval(cx-drawSize/2, cy-20, drawSize, 40);
                    g2.fillOval(cx+drawSize/2 -20, cy-25, 30, 20); // head
                    break;
                case 5: // annelid
                    for(int i=0;i<5;i++){
                        g2.setColor(i%2==0 ? s.color : s.color.darker());
                        g2.fillOval(cx-drawSize/2 + i*20, cy-15, 35, 30);
                    }
                    break;
                case 6: // arthropod - crab like
                    g2.fillOval(cx-30, cy-20, 60, 40);
                    g2.fillOval(cx-45, cy-10, 15, 15);
                    g2.fillOval(cx+30, cy-10, 15, 15);
                    g2.drawLine(cx-20, cy+10, cx-40, cy+30);
                    g2.drawLine(cx+20, cy+10, cx+40, cy+30);
                    break;
                case 7: // mollusc - octopus
                    g2.fillOval(cx-25, cy-30, 50, 50);
                    for(int i=0;i<4;i++){
                        int x = cx-30 + i*20;
                        g2.drawArc(x, cy+10, 10, 40, 0, -180);
                    }
                    break;
                case 8: // starfish
                    int[] xs = {cx, cx-35, cx-20, cx+20, cx+35};
                    int[] ys = {cy-40, cy-10, cy+35, cy+35, cy-10};
                    // simple star
                    Polygon p = new Polygon();
                    for(int i=0;i<5;i++){
                        double ang = Math.toRadians(90 + i*72);
                        p.addPoint((int)(cx + Math.cos(ang)*40), (int)(cy + Math.sin(ang)*40));
                    }
                    g2.fillPolygon(p);
                    break;
                case 9: // fish
                    g2.fillOval(cx-40, cy-15, 70, 30);
                    int[] fx = {cx-40, cx-60, cx-40};
                    int[] fy = {cy-15, cy, cy+15};
                    g2.fillPolygon(fx, fy, 3);
                    g2.fillOval(cx+10, cy-10, 8, 8);
                    break;
                case 10: // frog
                    g2.fillOval(cx-30, cy-10, 60, 30);
                    g2.fillOval(cx-35, cy-20, 20, 20);
                    g2.fillOval(cx+15, cy-20, 20, 20);
                    g2.fillOval(cx-10, cy+15, 20, 15);
                    break;
                case 11: // reptile
                    g2.fillRoundRect(cx-40, cy-10, 80, 20, 20, 20);
                    g2.fillOval(cx+30, cy-15, 25, 25);
                    g2.fillRect(cx-50, cy, 30, 5);
                    break;
                case 12: // mammal/human + bird silhouette
                    g2.setFont(new Font("SansSerif", Font.BOLD, 60));
                    g2.drawString("🐦 + 🧬 + 👤", cx-90, cy+20);
                    break;
            }

            // Label
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString(s.name, cx - g2.getFontMetrics().stringWidth(s.name)/2, cy + drawSize/2 + 30);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnimalEvolution().setVisible(true));
    }
}