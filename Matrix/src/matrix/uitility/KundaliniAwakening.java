package matrix.uitility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;

public class KundaliniAwakening extends JFrame {

    public KundaliniAwakening() {
        setTitle("Kundalini Awakening - Chakra Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new ChakraPanel());
        pack();
        setLocationRelativeTo(null);
    }

    static class Chakra {
        String sanskrit;
        String english;
        Color color;
        String element;
        double yPos; // relative position 0.0 to 1.0
        boolean awakened = false;

        Chakra(String sanskrit, String english, Color color, String element, double yPos) {
            this.sanskrit = sanskrit;
            this.english = english;
            this.color = color;
            this.element = element;
            this.yPos = yPos;
        }
    }

    static class ChakraPanel extends JPanel {
        private final Chakra[] chakras = {
            new Chakra("Sahasrara", "Crown", new Color(255, 255, 255), "Consciousness", 0.12),
            new Chakra("Ajna", "Third Eye", new Color(75, 0, 130), "Light", 0.20),
            new Chakra("Vishuddha", "Throat", new Color(0, 191, 255), "Ether", 0.29),
            new Chakra("Anahata", "Heart", new Color(0, 200, 83), "Air", 0.41),
            new Chakra("Manipura", "Solar Plexus", new Color(255, 214, 0), "Fire", 0.51),
            new Chakra("Svadhisthana", "Sacral", new Color(255, 109, 0), "Water", 0.62),
            new Chakra("Muladhara", "Root", new Color(213, 0, 0), "Earth", 0.75)
        };

        private float energyY = 0.95f;
        private int currentChakraIndex = 6; // start from bottom
        private float energySpeed = 0.004f;
        private Timer timer;
        private float pulse = 0;

        ChakraPanel() {
            setPreferredSize(new Dimension(900, 750));
            setBackground(new Color(12, 12, 20));

            timer = new Timer(16, (ActionEvent e) -> {
                pulse += 0.08f;
                if (currentChakraIndex >= 0) {
                    energyY -= energySpeed;
                    double targetY = chakras[currentChakraIndex].yPos;
                    if (energyY <= targetY) {
                        chakras[currentChakraIndex].awakened = true;
                        currentChakraIndex--;
                        // pause briefly at each chakra
                        try { Thread.sleep(300); } catch (Exception ignored) {}
                    }
                } else {
                    // All awakened - cosmic glow
                    energyY = 0.12f;
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int centerX = getWidth() / 2 - 50;
            int bodyTop = 50;
            int bodyHeight = 650;

            // Draw title
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Serif", Font.BOLD, 28));
            g2.drawString("Kundalini Awakening", 260, 35);

            // Draw human silhouette - subtle
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 40));
            drawHumanOutline(g2, centerX, bodyTop, bodyHeight);

            // Draw Sushumna Nadi - central channel
            g2.setColor(new Color(255, 235, 150, 60));
            g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{10, 8}, 0));
            g2.drawLine(centerX, bodyTop + 40, centerX, bodyTop + bodyHeight - 30);

            // Draw Kundalini energy trail
            if (currentChakraIndex >= -1) {
                GradientPaint trail = new GradientPaint(centerX, getHeight(), new Color(255, 215, 0, 180),
                        centerX, (int)(bodyTop + energyY * bodyHeight), new Color(255, 100, 0, 0));
                g2.setPaint(trail);
                g2.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(centerX, bodyTop + bodyHeight - 30, centerX, (int)(bodyTop + energyY * bodyHeight));
            }

            // Draw Kundalini Shakti - moving orb
            double orbY = bodyTop + energyY * bodyHeight;
            float orbPulse = (float)(8 + Math.sin(pulse) * 3);
            g2.setColor(new Color(255, 240, 150));
            g2.fill(new Ellipse2D.Double(centerX - orbPulse, orbY - orbPulse, orbPulse * 2, orbPulse * 2));
            g2.setColor(new Color(255, 180, 0, 120));
            g2.fill(new Ellipse2D.Double(centerX - orbPulse*1.8, orbY - orbPulse*1.8, orbPulse * 3.6, orbPulse * 3.6));

            // Draw Chakras
            for (int i = 0; i < chakras.length; i++) {
                Chakra c = chakras[i];
                int y = (int)(bodyTop + c.yPos * bodyHeight);
                int size = c.awakened? 36 : 24;
                if (i == currentChakraIndex + 1 && c.awakened) {
                    size = 36 + (int)(Math.sin(pulse) * 4);
                }

                // Glow if awakened
                if (c.awakened) {
                    g2.setColor(new Color(c.color.getRed(), c.color.getGreen(), c.color.getBlue(), 70));
                    g2.fillOval(centerX - size - 10, y - size - 10, (size+10)*2, (size+10)*2);
                }

                // Chakra circle
                g2.setColor(c.awakened? c.color : new Color(c.color.getRed(), c.color.getGreen(), c.color.getBlue(), 100));
                g2.fillOval(centerX - size, y - size, size*2, size*2);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(c.awakened? 2.5f : 1f));
                g2.drawOval(centerX - size, y - size, size*2, size*2);

                // Lotus petals hint
                if (c.awakened) {
                    g2.setColor(new Color(255,255,255,60));
                    g2.setStroke(new BasicStroke(1f));
                    for(int p=0; p<8; p++) {
                        double ang = Math.toRadians(p * 45 + pulse*5);
                        int px = (int)(centerX + Math.cos(ang) * (size+8));
                        int py = (int)(y + Math.sin(ang) * (size+8));
                        g2.drawLine(centerX, y, px, py);
                    }
                }

                // Label
                boolean isLeft = i % 2 == 0;
                int labelX = isLeft? centerX + 70 : centerX - 260;
                g2.setColor(c.awakened? Color.WHITE : new Color(255,255,255,120));
                g2.setFont(new Font("SansSerif", Font.BOLD, c.awakened? 14 : 12));
                g2.drawString(c.sanskrit + " - " + c.english, labelX, y + 5);

                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2.setColor(new Color(200,200,200, c.awakened? 200 : 80));
                g2.drawString(c.element, labelX, y + 18);

                // Connecting line
                g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4,4}, 0));
                g2.setColor(new Color(255,255,255, 50));
                g2.drawLine(centerX, y, labelX + (isLeft? -5 : 160), y);
            }

            // Right side info panel
            int infoX = 620;
            g2.setColor(new Color(30, 30, 45));
            g2.fillRoundRect(infoX, 80, 250, 520, 20, 20);
            g2.setColor(new Color(255,255,255,30));
            g2.drawRoundRect(infoX, 80, 250, 520, 20, 20);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("Awakening Status", infoX + 20, 110);

            int ty = 140;
            for (int i = chakras.length - 1; i >=0; i--) {
                Chakra c = chakras[i];
                g2.setColor(c.awakened? c.color : new Color(80,80,80));
                g2.fillOval(infoX + 20, ty - 8, 12, 12);
                g2.setColor(c.awakened? Color.WHITE : Color.GRAY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
                g2.drawString(c.sanskrit, infoX + 45, ty+2);
                if (c.awakened) {
                    g2.setColor(new Color(0,255,150));
                    g2.drawString("✓ Awakened", infoX + 150, ty+2);
                } else if (i == currentChakraIndex) {
                    g2.setColor(new Color(255,215,0));
                    g2.drawString("◉ Rising...", infoX + 150, ty+2);
                }
                ty += 32;
            }

            // Progress bar
            int progress = (int)(((6 - currentChakraIndex) / 7.0) * 100);
            if (progress < 0) progress = 100;
            g2.setColor(new Color(50,50,60));
            g2.fillRoundRect(infoX+20, 480, 210, 12, 6, 6);
            g2.setColor(new Color(255,215,0));
            g2.fillRoundRect(infoX+20, 480, (int)(210 * progress / 100.0), 12, 6, 6);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString("Kundalini Progress: " + progress + "%", infoX+20, 510);

            if (progress == 100) {
                g2.setFont(new Font("Serif", Font.ITALIC, 14));
                g2.setColor(new Color(255, 240, 150));
                g2.drawString("Samadhi - Union Achieved", infoX+20, 540);
            }

            // Reset button
            g2.setColor(new Color(60,60,80));
            g2.fillRoundRect(infoX+20, 555, 210, 30, 10, 10);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString("Click to Restart Journey", infoX+45, 574);
        }

        private void drawHumanOutline(Graphics2D g2, int cx, int top, int h) {
            // Head
            g2.drawOval(cx - 30, top, 60, 70);
            // Neck
            g2.drawLine(cx - 12, top + 70, cx - 12, top + 90);
            g2.drawLine(cx + 12, top + 70, cx + 12, top + 90);
            // Shoulders and arms
            g2.drawLine(cx - 12, top + 90, cx - 75, top + 130);
            g2.drawLine(cx + 12, top + 90, cx + 75, top + 130);
            g2.drawLine(cx - 75, top + 130, cx - 70, top + 280);
            g2.drawLine(cx + 75, top + 130, cx + 70, top + 280);
            // Torso
            g2.drawLine(cx - 12, top + 90, cx - 40, top + 350);
            g2.drawLine(cx + 12, top + 90, cx + 40, top + 350);
            // Legs
            g2.drawLine(cx - 40, top + 350, cx - 50, top + h);
            g2.drawLine(cx + 40, top + 350, cx + 50, top + h);
            g2.drawLine(cx - 10, top + 350, cx - 20, top + h);
            g2.drawLine(cx + 10, top + 350, cx + 20, top + h);
        }

        {
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int infoX = 620;
                    if (e.getX() > infoX+20 && e.getX() < infoX+230 && e.getY() > 555 && e.getY() < 585) {
                        energyY = 0.95f;
                        currentChakraIndex = 6;
                        for (Chakra c : chakras) c.awakened = false;
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KundaliniAwakening().setVisible(true));
    }
}