package matrix.evolution;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class EvolutionZoology extends JFrame {

    private List<EvoStage> stages;
    private int currentIndex = 0;
    
    private EvolutionPanel evolutionPanel;
    private JLabel titleLabel, scientificLabel, periodLabel;
    private JTextArea infoArea;
    private JProgressBar progressBar;
    private JButton prevBtn, nextBtn;

    public EvolutionZoology() {
        setTitle("Zoology: Evolution from Reptiles to Homo sapiens");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));
        
        initStages();
        initUI();
        updateStage();
    }

    private void initStages() {
        stages = new ArrayList<>();
        stages.add(new EvoStage("1. Basal Reptiles\n(Cotylosauria)", "Hylonomus", "~320 MYA\nCarboniferous", 
                "Cold-blooded, scaly skin, lay amniotic eggs on land. First true reptiles. Skull anapsid - no temporal fenestra.",
                new Color(0x5D4037), StageType.REPTILE));
        
        stages.add(new EvoStage("2. Therapsids\n(Mammal-like Reptiles)", "Dimetrodon / Lystrosaurus", "~270-250 MYA\nPermian-Triassic", 
                "Synapsid skull with one temporal opening. Differentiation of teeth (incisors, canines, molars). More upright posture. Beginning of endothermy.",
                new Color(0x8D6E63), StageType.THERAPSID));
        
        stages.add(new EvoStage("3. Early Mammals", "Morganucodon", "~210 MYA\nLate Triassic", 
                "Small, nocturnal, warm-blooded. Hair present. Mammary glands evolve. Diphyodont dentition and heterodont teeth. Middle ear with 3 ossicles.",
                new Color(0xA1887F), StageType.EARLY_MAMMAL));
        
        stages.add(new EvoStage("4. Early Primates", "Purgatorius", "~66 MYA\nPaleocene", 
                "Arboreal, binocular vision evolving, grasping hands with opposable thumb, large brain-to-body ratio. Nails instead of claws.",
                new Color(0x795548), StageType.PRIMATE));
        
        stages.add(new EvoStage("5. Anthropoids\n(Monkeys)", "Aegyptopithecus", "~35 MYA\nOligocene", 
                "Larger brain, flat face, stereoscopic color vision. Diurnal. Tail present but reduced. Complex social groups.",
                new Color(0x6D4C41), StageType.MONKEY));
        
        stages.add(new EvoStage("6. Hominoids\n(Apes)", "Proconsul", "~23 MYA\nMiocene", 
                "Tailless, large body, Y-5 molar pattern, suspensory shoulder joint (brachiation). Much larger cranial capacity.",
                new Color(0x4E342E), StageType.APE));
        
        stages.add(new EvoStage("7. Early Hominids", "Australopithecus afarensis", "~3.2 MYA\nPliocene", 
                "Bipedalism fully established. Foramen magnum anterior. Reduced canines. Brain ~400-500cc. Still arboreal adaptations.",
                new Color(0x3E2723), StageType.HOMINID));
        
        stages.add(new EvoStage("8. Genus Homo\n(Habilis & Erectus)", "Homo erectus", "~1.8 MYA - 300 KYA\nPleistocene", 
                "Tool making (Oldowan, Acheulean). Fire control. Brain 600-900cc. Reduced prognathism. Modern body proportions.",
                new Color(0x263238), StageType.HOMO));
        
        stages.add(new EvoStage("9. Homo sapiens\n(Modern Man)", "Homo sapiens sapiens", "~300 KYA - Present\nHolocene", 
                "CELL LEVEL: Eukaryotic Animal Cell. 46 chromosomes (23 pairs). High cerebral cortex development. Chin present. Brain ~1350cc. Complex language, culture. Dominant cell type: Highly differentiated somatic cells with specialized organelles.",
                new Color(0x0D47A1), StageType.SAPIENS));
    }

    private void initUI() {
        // Top Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0xE3F2FD));
        header.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        JLabel branch = new JLabel("BIOLOGY > ZOOLOGY > CHORDATA > VERTEBRATA > MAMMALIA > PRIMATES > EVOLUTION", SwingConstants.CENTER);
        branch.setFont(new Font("Monospaced", Font.BOLD, 12));
        header.add(branch, BorderLayout.NORTH);
        
        progressBar = new JProgressBar(0, stages.size()-1);
        progressBar.setStringPainted(true);
        header.add(progressBar, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // Center Evolution Visual
        evolutionPanel = new EvolutionPanel();
        add(evolutionPanel, BorderLayout.CENTER);

        // Right Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(380, 0));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20,15,20,15));
        infoPanel.setBackground(Color.WHITE);

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        scientificLabel = new JLabel();
        scientificLabel.setFont(new Font("Serif", Font.ITALIC, 16));
        scientificLabel.setForeground(new Color(0x555555));
        periodLabel = new JLabel();
        periodLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        periodLabel.setForeground(new Color(0x1565C0));

        infoArea = new JTextArea(6,20);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        infoArea.setBackground(new Color(0xFFF9C4));
        infoArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(scientificLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(periodLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(new JSeparator());
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(new JLabel("Zoological Characteristics:"));
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(new JScrollPane(infoArea));

        // Navigation
        JPanel navPanel = new JPanel(new GridLayout(1,2,10,0));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
        prevBtn = new JButton("<< Previous");
        nextBtn = new JButton("Next >>");
        prevBtn.addActionListener(e -> { if(currentIndex>0){currentIndex--; updateStage();}});
        nextBtn.addActionListener(e -> { if(currentIndex<stages.size()-1){currentIndex++; updateStage();}});
        navPanel.add(prevBtn);
        navPanel.add(nextBtn);
        infoPanel.add(navPanel);

        add(infoPanel, BorderLayout.EAST);
    }

    private void updateStage() {
        EvoStage s = stages.get(currentIndex);
        titleLabel.setText("<html>" + s.name.replace("\n","<br>") + "</html>");
        scientificLabel.setText(s.scientificName);
        periodLabel.setText(s.period);
        infoArea.setText(s.description);
        progressBar.setValue(currentIndex);
        progressBar.setString("Evolution Progress: Stage " + (currentIndex+1) + " / " + stages.size());
        evolutionPanel.setStage(s, currentIndex);
        prevBtn.setEnabled(currentIndex != 0);
        nextBtn.setEnabled(currentIndex != stages.size()-1);
    }

    // Custom drawing panel
    class EvolutionPanel extends JPanel {
        private EvoStage stage;
        private int index;
        EvolutionPanel(){ setBackground(new Color(0xFAFAFA)); }
        void setStage(EvoStage s, int i){ this.stage = s; this.index = i; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(stage==null) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw timeline
            int w = getWidth(), h = getHeight();
            g2.setColor(new Color(0xE0E0E0));
            g2.fillRoundRect(40, h-60, w-80, 10, 10,10);
            int markerX = 40 + (int)((w-80) * ((double)index/(stages.size()-1)));
            g2.setColor(stage.color);
            g2.fillOval(markerX-12, h-67, 24,24);

            // Draw organism silhouette based on type
            int cx = w/2, cy = h/2 - 20;
            g2.setColor(stage.color);
            
            // Simple schematic drawing
            switch(stage.type){
                case REPTILE -> drawReptile(g2, cx, cy);
                case THERAPSID -> drawTherapsid(g2, cx, cy);
                case EARLY_MAMMAL -> drawEarlyMammal(g2, cx, cy);
                case PRIMATE, MONKEY, APE -> drawPrimate(g2, cx, cy, index);
                case HOMINID, HOMO -> drawHominid(g2, cx, cy, index);
                case SAPIENS -> drawHumanCell(g2, cx, cy);
            }
            
            // Label
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Evolutionary Adaptation Level: " + (index*12 + 15) + "% toward modern human", cx-180, 40);
        }

        private void drawReptile(Graphics2D g2, int x, int y){
            g2.fillOval(x-60, y-10, 100, 30); // body
            g2.fillOval(x+40, y-5, 30, 20); // head
            g2.fillRect(x-60, y+15, 80, 8); // tail
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(x-20, y+20, x-30, y+40);
            g2.drawLine(x+10, y+20, x+20, y+40);
        }
        private void drawTherapsid(Graphics2D g2, int x, int y){
            g2.fillOval(x-50, y-15, 90, 35);
            g2.fillOval(x+30, y-10, 35, 28);
            g2.setColor(Color.ORANGE.darker());
            g2.fillArc(x+45, y-5, 20, 10, 0, 180);
        }
        private void drawEarlyMammal(Graphics2D g2, int x, int y){
            g2.fillOval(x-40, y-15, 70, 30);
            g2.fillOval(x+20, y-12, 25, 22);
            g2.fillOval(x-45, y+0, 20, 20); // tail curl
        }
        private void drawPrimate(Graphics2D g2, int x, int y, int idx){
            // stick figure getting upright
            int upright = (idx-3)*15;
            g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x, y+30-upright, x, y-20); // spine
            g2.drawOval(x-15, y-45, 30, 30); // head
            g2.drawLine(x, y-10, x-25, y-20); // arm
            g2.drawLine(x, y-10, x+25, y-20);
            g2.drawLine(x, y+30-upright, x-15, y+50);
            g2.drawLine(x, y+30-upright, x+15, y+50);
        }
        private void drawHominid(Graphics2D g2, int x, int y, int idx){
            g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x, y+40, x, y-10);
            g2.drawOval(x-18, y-40, 36, 36);
            g2.drawLine(x, y, x-30, y+10);
            g2.drawLine(x, y, x+30, y+10);
            g2.drawLine(x, y+40, x-18, y+70);
            g2.drawLine(x, y+40, x+18, y+70);
            if(idx>5) { g2.setColor(Color.BLACK); g2.drawLine(x+5, y-30, x+12, y-30); } // tool
        }
        private void drawHumanCell(Graphics2D g2, int x, int y){
            // Draw Animal Cell for Homo sapiens
            g2.setColor(new Color(0x90CAF9));
            g2.fillOval(x-110, y-90, 220, 220); // cytoplasm
            g2.setColor(new Color(0x1565C0));
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x-110, y-90, 220, 220); // cell membrane

            g2.setColor(new Color(0x1A237E));
            g2.fillOval(x-45, y-35, 90, 90); // nucleus
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.drawString("NUCLEUS", x-25, y+5);
            g2.drawString("46 Chromosomes", x-40, y+15);

            g2.setColor(new Color(0xFF8A65));
            g2.fillOval(x+40, y-40, 30, 18); // mitochondria
            g2.fillOval(x-70, y+10, 30, 18);
            g2.setColor(Color.BLACK);
            g2.drawString("Mitochondria", x+35, y-50);
        }
    }

    enum StageType { REPTILE, THERAPSID, EARLY_MAMMAL, PRIMATE, MONKEY, APE, HOMINID, HOMO, SAPIENS }

    static class EvoStage {
        String name, scientificName, period, description;
        Color color; StageType type;
        EvoStage(String n, String sn, String p, String d, Color c, StageType t){
            name=n; scientificName=sn; period=p; description=d; color=c; type=t;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EvolutionZoology().setVisible(true));
    }
}