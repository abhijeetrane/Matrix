package matrix.uitility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

public class KundaliniSimulation extends JFrame {

    public KundaliniSimulation() {
        setTitle("Kundalini Awakening Simulation");
        setSize(700, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        ChakraPanel chakraPanel = new ChakraPanel();
        add(chakraPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Start Awakening");
        JButton resetButton = new JButton("Reset");

        startButton.addActionListener(e -> chakraPanel.startAwakening());
        resetButton.addActionListener(e -> chakraPanel.resetSimulation());

        controlPanel.add(startButton);
        controlPanel.add(resetButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KundaliniSimulation frame = new KundaliniSimulation();
            frame.setVisible(true);
        });
    }
}

class ChakraPanel extends JPanel {

    static class Chakra {
        String name;
        String englishName;
        Color color;
        int yCoord;
        boolean activated = false;

        Chakra(String name, String englishName, Color color, int yCoord) {
            this.name = name;
            this.englishName = englishName;
            this.color = color;
            this.yCoord = yCoord;
        }
    }

    private final Chakra[] chakras = new Chakra[7];
    private double currentEnergyY;
    private final int startY = 560; // Muladhara location
    private final int endY = 110;   // Sahasrara location
    private Timer timer;

    public ChakraPanel() {
        setBackground(new Color(20, 20, 30));

        // Initialize 7 Chakras from bottom (Muladhara) to top (Sahasrara)
        chakras[0] = new Chakra("Muladhara", "Root", new Color(255, 50, 50), 560);
        chakras[1] = new Chakra("Svadhisthana", "Sacral", new Color(255, 140, 0), 485);
        chakras[2] = new Chakra("Manipura", "Solar Plexus", new Color(255, 215, 0), 410);
        chakras[3] = new Chakra("Anahata", "Heart", new Color(50, 205, 50), 335);
        chakras[4] = new Chakra("Vishuddha", "Throat", new Color(30, 144, 255), 260);
        chakras[5] = new Chakra("Ajna", "Third Eye", new Color(75, 0, 130), 185);
        chakras[6] = new Chakra("Sahasrara", "Crown", new Color(148, 0, 211), 110);

        currentEnergyY = startY;

        // Animation Timer
        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentEnergyY > endY) {
                    currentEnergyY -= 2; // Speed of rising Kundalini

                    // Activate chakras as energy reaches them
                    for (Chakra chakra : chakras) {
                        if (currentEnergyY <= chakra.yCoord) {
                            chakra.activated = true;
                        }
                    }
                    repaint();
                } else {
                    timer.stop();
                }
            }
        });
    }

    public void startAwakening() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public void resetSimulation() {
        timer.stop();
        currentEnergyY = startY;
        for (Chakra chakra : chakras) {
            chakra.activated = false;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;

        // 1. Draw Standing Human Silhouette
        drawHumanBody(g2, centerX);

        // 2. Draw Energy Beam (Sushumna Nadi & Kundalini Fire)
        if (currentEnergyY < startY) {
            g2.setStroke(new BasicStroke(6f));
            g2.setColor(new Color(255, 255, 200, 200));
            g2.drawLine(centerX, startY, centerX, (int) currentEnergyY);

            // Glowing tip of rising energy
            g2.setColor(Color.WHITE);
            g2.fill(new Ellipse2D.Double(centerX - 8, currentEnergyY - 8, 16, 16));
        }

        // 3. Draw Chakras and Labels
        for (Chakra chakra : chakras) {
            int radius = 24;
            int x = centerX - radius / 2;
            int y = chakra.yCoord - radius / 2;

            if (chakra.activated) {
                // Aura effect when active
                g2.setColor(new Color(chakra.color.getRed(), chakra.color.getGreen(), chakra.color.getBlue(), 100));
                g2.fillOval(x - 10, y - 10, radius + 20, radius + 20);
                g2.setColor(chakra.color);
            } else {
                // Dim state when unawakened
                g2.setColor(chakra.color.darker().darker());
            }

            g2.fillOval(x, y, radius, radius);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(x, y, radius, radius);

            // Text Labels
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            String label = chakra.name + " (" + chakra.englishName + ")";
            
            // Alternating labels left and right for UI clarity
            if (chakra.yCoord % 2 == 0) {
                g2.setColor(chakra.activated ? Color.WHITE : Color.GRAY);
                g2.drawString(label, centerX + 40, chakra.yCoord + 5);
                g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0));
                g2.drawLine(centerX + 15, chakra.yCoord, centerX + 35, chakra.yCoord);
            } else {
                g2.setColor(chakra.activated ? Color.WHITE : Color.GRAY);
                int stringWidth = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, centerX - 40 - stringWidth, chakra.yCoord + 5);
                g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0));
                g2.drawLine(centerX - 15, chakra.yCoord, centerX - 35, chakra.yCoord);
            }
        }
    }

    private void drawHumanBody(Graphics2D g2, int cx) {
        g2.setColor(new Color(50, 50, 70));

        // Head
        g2.fillOval(cx - 30, 80, 60, 70);

        // Neck
        g2.fillRect(cx - 12, 145, 24, 25);

        // Torso and Legs
        Path2D body = new Path2D.Double();
        body.moveTo(cx - 50, 170); // Left Shoulder
        body.lineTo(cx + 50, 170); // Right Shoulder
        body.lineTo(cx + 35, 330); // Waist
        body.lineTo(cx + 40, 580); // Right Foot
        body.lineTo(cx + 10, 580); // Right Inner Leg
        body.lineTo(cx, 450);      // Crotch
        body.lineTo(cx - 10, 580); // Left Inner Leg
        body.lineTo(cx - 40, 580); // Left Foot
        body.lineTo(cx - 35, 330); // Waist
        body.closePath();

        g2.fill(body);
    }
}