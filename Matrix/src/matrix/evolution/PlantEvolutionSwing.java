package matrix.evolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlantEvolutionSwing extends JFrame {

    private EvolutionPanel evolutionPanel;
    private JLabel titleLabel, infoLabel, timeLabel;
    private JSlider timelineSlider;
    private Timer autoTimer;
    private int currentStage = 0;

    private static final String[] STAGE_TITLES = {
        "1. Single-Celled Eukaryote (Protist Ancestor)",
        "2. Colonial Green Algae",
        "3. Filamentous Charophyte Algae",
        "4. Bryophytes - First Land Plants",
        "5. Pteridophytes - Vascular Plants",
        "6. Gymnosperms - Seed Plants",
        "7. Angiosperms - Flowering Plants"
    };

    private static final String[] STAGE_INFO = {
        "<html><b>~1.6 Billion Years Ago</b><br>Photosynthetic eukaryote with nucleus, mitochondria and chloroplast. Origin of Archaeplastida. Endosymbiosis event.</html>",
        "<html><b>~1.0 Billion Years Ago</b><br>Cells stay together after division. Volvox-like colonies. Division of labor begins. Still aquatic.</html>",
        "<html><b>~700 Million Years Ago</b><br>Charophytes - closest relatives to land plants. Filamentous structure, cell wall with cellulose, phragmoplast formation.</html>",
        "<html><b>~470 Mya - Ordovician</b><br>Colonization of land. Non-vascular, requires water for reproduction. Dominant gametophyte. e.g., Mosses, Liverworts. Cuticle and spores evolve.</html>",
        "<html><b>~420 Mya - Silurian</b><br>Evolution of xylem and phloem, true roots, stems, leaves. Dominant sporophyte. Lignin allows tall growth. e.g., Ferns.</html>",
        "<html><b>~360 Mya - Carboniferous</b><br>Evolution of seed, pollen, no need for water for fertilization. Naked seeds in cones. e.g., Conifers, Cycads. Secondary growth.</html>",
        "<html><b>~140 Mya - Cretaceous</b><br>Most advanced. Enclosed seed in flower, fruit, double fertilization, co-evolution with pollinators. Dominant today. e.g., Mango, Rose.</html>"
    };

    private static final String[] STAGE_TIME = {
        "1.6 BYA", "1.0 BYA", "700 MYA", "470 MYA", "420 MYA", "360 MYA", "140 MYA - Present"
    };

    public PlantEvolutionSwing() {
        setTitle("Plant Evolution - From Single Cell to Flowering Plant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Top Title
        titleLabel = new JLabel(STAGE_TITLES[0], SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Center Drawing Panel
        evolutionPanel = new EvolutionPanel();
        evolutionPanel.setBackground(new Color(235, 248, 235));
        add(evolutionPanel, BorderLayout.CENTER);

        // Bottom Controls
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        infoLabel = new JLabel(STAGE_INFO[0]);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        timeLabel = new JLabel(STAGE_TIME[0], SwingConstants.RIGHT);
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        timeLabel.setForeground(new Color(0, 100, 0));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        infoPanel.add(timeLabel, BorderLayout.EAST);

        // Slider and Buttons
        JPanel controlPanel = new JPanel(new BorderLayout());
        timelineSlider = new JSlider(0, 6, 0);
        timelineSlider.setMajorTickSpacing(1);
        timelineSlider.setPaintTicks(true);
        timelineSlider.setPaintLabels(true);
        timelineSlider.addChangeListener(e -> {
            currentStage = timelineSlider.getValue();
            updateStage();
        });

        JPanel buttonPanel = new JPanel();
        JButton prevBtn = new JButton("<< Previous");
        JButton nextBtn = new JButton("Next >>");
        JToggleButton autoBtn = new JToggleButton("Auto-Play");

        prevBtn.addActionListener(e -> {
            if (currentStage > 0) {
                currentStage--;
                timelineSlider.setValue(currentStage);
            }
        });
        nextBtn.addActionListener(e -> {
            if (currentStage < 6) {
                currentStage++;
                timelineSlider.setValue(currentStage);
            }
        });

        autoBtn.addActionListener(e -> {
            if (autoBtn.isSelected()) {
                autoTimer.start();
                autoBtn.setText("Stop");
            } else {
                autoTimer.stop();
                autoBtn.setText("Auto-Play");
            }
        });

        buttonPanel.add(prevBtn);
        buttonPanel.add(autoBtn);
        buttonPanel.add(nextBtn);

        controlPanel.add(timelineSlider, BorderLayout.CENTER);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);

        bottomPanel.add(infoPanel, BorderLayout.NORTH);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Auto timer - evolves every 2.5 seconds
        autoTimer = new Timer(2500, e -> {
            currentStage = (currentStage + 1) % 7;
            timelineSlider.setValue(currentStage);
            if (currentStage == 6) {
                autoTimer.stop();
                autoBtn.setSelected(false);
                autoBtn.setText("Auto-Play");
            }
        });

        updateStage();
    }

    private void updateStage() {
        titleLabel.setText(STAGE_TITLES[currentStage]);
        infoLabel.setText(STAGE_INFO[currentStage]);
        timeLabel.setText(STAGE_TIME[currentStage]);
        evolutionPanel.setStage(currentStage);
    }

    // Custom Drawing Panel
    class EvolutionPanel extends JPanel {
        private int stage = 0;
        public void setStage(int s) {
            this.stage = s;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            switch (stage) {
                case 0: drawSingleCell(g2, w, h); break;
                case 1: drawColonial(g2, w, h); break;
                case 2: drawFilamentous(g2, w, h); break;
                case 3: drawBryophyte(g2, w, h); break;
                case 4: drawPteridophyte(g2, w, h); break;
                case 5: drawGymnosperm(g2, w, h); break;
                case 6: drawAngiosperm(g2, w, h); break;
            }
        }

        private void drawSingleCell(Graphics2D g, int w, int h) {
            int cx = w/2, cy = h/2;
            g.setColor(new Color(120, 200, 120));
            g.fillOval(cx-60, cy-60, 120, 120);
            g.setColor(new Color(0,100,0)); g.setStroke(new BasicStroke(3));
            g.drawOval(cx-60, cy-60, 120, 120);
            // Nucleus
            g.setColor(new Color(70, 130, 180)); g.fillOval(cx-20, cy-20, 40, 40);
            g.setColor(Color.WHITE); g.drawString("Nucleus", cx-18, cy+5);
            // Chloroplast
            g.setColor(new Color(50, 180, 50)); g.fillOval(cx-45, cy+10, 25, 15);
            g.fillOval(cx+20, cy-30, 25, 15);
            g.setColor(Color.BLACK); g.drawString("Chloroplast", cx-80, cy-70);
        }

        private void drawColonial(Graphics2D g, int w, int h) {
            int cx = w/2, cy = h/2;
            g.setColor(new Color(100, 180, 220, 80)); g.fillOval(cx-90, cy-90, 180, 180);
            for(int i=0; i<8; i++) {
                double ang = i * Math.PI/4;
                int x = cx + (int)(60*Math.cos(ang));
                int y = cy + (int)(60*Math.sin(ang));
                g.setColor(new Color(120,200,120)); g.fillOval(x-20, y-20, 40, 40);
                g.setColor(new Color(0,100,0)); g.drawOval(x-20, y-20, 40, 40);
            }
        }

        private void drawFilamentous(Graphics2D g, int w, int h) {
            int y = h/2;
            for(int i=0; i<6; i++) {
                int x = w/2 - 150 + i*60;
                g.setColor(new Color(130, 210, 130)); g.fillRect(x, y-20, 60, 40);
                g.setColor(new Color(0,100,0)); g.drawRect(x, y-20, 60, 40);
                g.setColor(new Color(70,130,180)); g.fillOval(x+15, y-8, 15, 15);
            }
        }

        private void drawBryophyte(Graphics2D g, int w, int h) {
            g.setColor(new Color(139, 90, 43)); g.fillRect(0, h-60, w, 60);
            int baseY = h-60;
            g.setColor(new Color(60, 150, 60));
            for(int i=0; i<15; i++) {
                int x = w/2 - 150 + i*20;
                g.fillOval(x, baseY-40 - (int)(Math.random()*20), 10, 30);
            }
            // Capsule
            g.setColor(new Color(80,160,80)); g.fillRect(w/2-5, baseY-100, 10, 60);
            g.setColor(new Color(160,120,80)); g.fillOval(w/2-12, baseY-120, 24, 25);
        }

        private void drawPteridophyte(Graphics2D g, int w, int h) {
            g.setColor(new Color(139,90,43)); g.fillRect(0, h-60, w, 60);
            g.setColor(new Color(34,139,34)); g.setStroke(new BasicStroke(8));
            g.drawLine(w/2, h-60, w/2, h/2-80);
            // Fronds
            g.setStroke(new BasicStroke(3));
            for(int i=0; i<6; i++) {
                int y = h/2 - 20 - i*30;
                g.drawLine(w/2, y, w/2-60+i*5, y-20);
                g.drawLine(w/2, y, w/2+60-i*5, y-20);
            }
            g.setColor(new Color(139,90,43)); g.fillRect(w/2-30, h-60, 60, 20);
        }

        private void drawGymnosperm(Graphics2D g, int w, int h) {
            g.setColor(new Color(139,90,43)); g.fillRect(0, h-60, w, 60);
            // Trunk
            g.setColor(new Color(101,67,33)); g.fillRect(w/2-20, h/2, 40, h/2-60);
            // Conical leaves
            g.setColor(new Color(0,100,0));
            int[] xPoints = {w/2-120, w/2, w/2+120};
            int[] yPoints = {h/2+20, h/2-150, h/2+20};
            g.fillPolygon(xPoints, yPoints, 3);
            int[] x2 = {w/2-90, w/2, w/2+90};
            int[] y2 = {h/2-30, h/2-180, h/2-30};
            g.fillPolygon(x2, y2, 3);
            // Cone
            g.setColor(new Color(160,120,60)); g.fillOval(w/2+40, h-100, 30, 40);
        }

        private void drawAngiosperm(Graphics2D g, int w, int h) {
            g.setColor(new Color(139,90,43)); g.fillRect(0, h-60, w, 60);
            g.setColor(new Color(101,67,33)); g.fillRect(w/2-15, h/2+20, 30, h/2-80);
            // Leaves
            g.setColor(new Color(34,139,34)); g.fillOval(w/2-70, h/2, 50, 25); g.fillOval(w/2+20, h/2-20, 50, 25);
            // Flower
            int fx = w/2, fy = h/2-40;
            g.setColor(Color.YELLOW); g.fillOval(fx-15, fy-15, 30, 30);
            g.setColor(Color.PINK);
            for(int i=0; i<5; i++) {
                double ang = i*72*Math.PI/180;
                int px = fx + (int)(35*Math.cos(ang));
                int py = fy + (int)(35*Math.sin(ang));
                g.fillOval(px-18, py-18, 36, 36);
            }
            g.setColor(Color.YELLOW); g.fillOval(fx-15, fy-15, 30, 30);
            // Fruit indication
            g.setColor(new Color(255,100,100)); g.fillOval(w/2+60, h/2+40, 20, 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PlantEvolutionSwing().setVisible(true);
        });
    }
}