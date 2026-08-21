package matrix.justice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GoddessOfJusticeSimulationTopThree extends JFrame {

    public GoddessOfJusticeSimulationTopThree() {
        setTitle("Goddess of Justice - India Pakistan Balance");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new JusticePanel());
    }

    static class JusticePanel extends JPanel implements ActionListener {
        private Timer timer;
        private float angle = 0; // for oscillation
        private float direction = 0.03f;

        JusticePanel() {
            timer = new Timer(30, this);
            timer.start();
            setBackground(new Color(245, 240, 230));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            angle += direction;
            if (angle > 0.5f || angle < -0.5f) {
                direction = -direction;
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int W = getWidth();
            int H = getHeight();

            // --- Top Title ---
            g2.setFont(new Font("Serif", Font.BOLD, 32));
            g2.setColor(new Color(80, 0, 0));
            String title = "India + Pakistan";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(title, (W - fm.stringWidth(title)) / 2, 50);

            g2.setFont(new Font("Serif", Font.ITALIC, 16));
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Goddess of Justice", (W - 150) / 2, 75);

            // --- Goddess Center ---
            int centerX = W / 2;
            int centerY = H / 2 + 20;

            // Body / Robe
            g2.setColor(new Color(210, 210, 210));
            int[] robeX = {centerX - 60, centerX + 60, centerX + 40, centerX - 40};
            int[] robeY = {centerY - 80, centerY - 80, centerY + 180, centerY + 180};
            g2.fillPolygon(robeX, robeY, 4);

            // Neck
            g2.setColor(new Color(255, 220, 177));
            g2.fillRect(centerX - 15, centerY - 110, 30, 35);

            // Face
            g2.fillOval(centerX - 35, centerY - 150, 70, 70);

            // Black Cloth Blindfold
            g2.setColor(Color.BLACK);
            g2.fillRect(centerX - 36, centerY - 130, 72, 18);
            // knot
            g2.fillOval(centerX + 30, centerY - 125, 12, 12);

            // Hair
            g2.setColor(new Color(60, 30, 10));
            g2.fillArc(centerX - 38, centerY - 155, 76, 40, 0, 180);

            // Hands holding scale
            g2.setColor(new Color(255, 220, 177));
            g2.fillOval(centerX - 110, centerY - 20, 25, 25);
            g2.fillOval(centerX + 85, centerY - 20, 25, 25);

            // Scale Beam
            int beamY = centerY - 10;
            int beamLength = 400;
            
            // beam tilts based on angle
            double sin = Math.sin(angle) * 80;
            
            int leftX = centerX - beamLength / 2;
            int rightX = centerX + beamLength / 2;
            int leftY = (int)(beamY + sin);
            int rightY = (int)(beamY - sin);

            g2.setStroke(new BasicStroke(6));
            g2.setColor(new Color(184, 134, 11)); // golden
            g2.drawLine(leftX, leftY, rightX, rightY);

            // Center support
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(centerX, beamY, centerX, centerY - 80);

            // Chains and Plates
            drawPlate(g2, leftX, leftY, true);
            drawPlate(g2, rightX, rightY, false);
        }

        private void drawPlate(Graphics2D g2, int x, int y, boolean isLeft) {
            int plateWidth = 180;
            int plateHeight = 20;
            int chainHeight = 70;

            // chains
            g2.setStroke(new BasicStroke(2));
            g2.setColor(new Color(100, 100, 100));
            g2.drawLine(x - 50, y, x - 50, y + chainHeight);
            g2.drawLine(x + 50, y, x + 50, y + chainHeight);
            g2.drawLine(x, y, x, y + chainHeight);

            int plateX = x - plateWidth / 2;
            int plateY = y + chainHeight;

            // plate
            g2.setColor(new Color(184, 134, 11));
            g2.fillOval(plateX, plateY, plateWidth, plateHeight);
            g2.setColor(Color.BLACK);
            g2.drawOval(plateX, plateY, plateWidth, plateHeight);

            // labels inside plate
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.setColor(new Color(40, 0, 0));
            
            if (isLeft) {
                // Side 1: Uzbekistan, Russia, China
                g2.drawString("Uzbekistan", plateX + 45, plateY + 35);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.drawString("(Muslim Invaders of India and Pakistan)", plateX - 5, plateY + 47);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.drawString("Russia", plateX + 65, plateY + 65);
                g2.drawString("China", plateX + 65, plateY + 80);
            } else {
                // Side 2: Britain, Bangladesh, America
                g2.drawString("Britain", plateX + 65, plateY + 35);
                g2.drawString("Bangladesh", plateX + 50, plateY + 55);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.drawString("(Goddess Kali)", plateX + 50, plateY + 67);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.drawString("America", plateX + 60, plateY + 85);
            }

            // weights icons on plate
            g2.setColor(new Color(120, 120, 120));
            g2.fillRect(plateX + 30, plateY - 15, 30, 15);
            g2.fillRect(plateX + 100, plateY - 15, 40, 15);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GoddessOfJusticeSimulationTopThree().setVisible(true);
        });
    }
}