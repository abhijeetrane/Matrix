package matrix.justice;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class LadyJusticeSimulation extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Enable antialiasing for smooth vector rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // 1. Background (Dark Gradient)
        GradientPaint bgGradient = new GradientPaint(0, 0, new Color(20, 24, 33), 0, height, new Color(10, 12, 18));
        g2.setPaint(bgGradient);
        g2.fillRect(0, 0, width, height);

        // 2. Title: "INDIA" at top center
        g2.setFont(new Font("Serif", Font.BOLD, 36));
        FontMetrics fm = g2.getFontMetrics();
        String title = "INDIA";
        int titleX = (width - fm.stringWidth(title)) / 2;
        g2.setColor(new Color(255, 215, 0)); // Gold title
        g2.drawString(title, titleX, 50);

        // 3. Side Labels: Allies
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        
        // Left Side: Russia & Allies
        g2.setColor(new Color(220, 50, 50));
        g2.drawString("RUSSIA & ALLIES", 40, 120);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.setColor(new Color(200, 200, 200));
        g2.drawString("• China", 40, 150);
        g2.drawString("• Iran", 40, 175);
        g2.drawString("• North Korea", 40, 200);

        // Right Side: America & Allies
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.setColor(new Color(70, 130, 180));
        int rightX = width - 220;
        g2.drawString("AMERICA & ALLIES", rightX, 120);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.setColor(new Color(200, 200, 200));
        g2.drawString("• NATO", rightX, 150);
        g2.drawString("• European Union", rightX, 175);
        g2.drawString("• Japan / S. Korea", rightX, 200);

        // Center offsets
        int cx = width / 2;
        int cy = height / 2 + 30;

        // 4. Draw Lady Justice Statue
        drawLadyJustice(g2, cx, cy);

        // 5. Draw Scales of Justice
        drawScales(g2, cx, cy);
    }

    private void drawLadyJustice(Graphics2D g2, int cx, int cy) {
        // Robe / Body
        g2.setColor(new Color(220, 215, 200)); // Off-white/marble
        Path2D robe = new Path2D.Double();
        robe.moveTo(cx - 25, cy - 20);
        robe.lineTo(cx + 25, cy - 20);
        robe.lineTo(cx + 45, cy + 180);
        robe.lineTo(cx - 45, cy + 180);
        robe.closePath();
        g2.fill(robe);

        // Robe Fold Details
        g2.setColor(new Color(180, 175, 160));
        g2.setStroke(new BasicStroke(2));
        g2.draw(new Line2D.Double(cx - 10, cy, cx - 15, cy + 180));
        g2.draw(new Line2D.Double(cx + 5, cy, cx + 8, cy + 180));

        // Neck
        g2.setColor(new Color(235, 200, 175));
        g2.fillRect(cx - 8, cy - 40, 16, 20);

        // Head
        g2.fillOval(cx - 20, cy - 80, 40, 45);

        // Hair
        g2.setColor(new Color(90, 50, 20));
        g2.fillArc(cx - 22, cy - 85, 44, 30, 0, 180);

        // Black Blindfold
        g2.setColor(Color.BLACK);
        g2.fillRect(cx - 22, cy - 65, 44, 12);
        // Blindfold Strap Ties
        Path2D strap = new Path2D.Double();
        strap.moveTo(cx + 20, cy - 60);
        strap.lineTo(cx + 35, cy - 50);
        strap.lineTo(cx + 30, cy - 40);
        strap.closePath();
        g2.fill(strap);

        // Outstretched Arms (holding the scale beam)
        g2.setColor(new Color(235, 200, 175));
        g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // Left arm extended
        g2.draw(new Line2D.Double(cx - 20, cy - 10, cx - 130, cy - 30));
        // Right arm extended
        g2.draw(new Line2D.Double(cx + 20, cy - 10, cx + 130, cy - 30));
    }

    private void drawScales(Graphics2D g2, int cx, int cy) {
        g2.setColor(new Color(212, 175, 55)); // Metallic Gold
        g2.setStroke(new BasicStroke(4));

        // Scale Beam
        int leftX = cx - 140;
        int rightX = cx + 140;
        int beamY = cy - 30;
        g2.draw(new Line2D.Double(leftX, beamY, rightX, beamY));

        // Center Fulcrum Knob
        g2.fillOval(cx - 8, beamY - 8, 16, 16);

        // --- Left Scale Pan (Russia Side) ---
        drawPan(g2, leftX, beamY, "Russia", new Color(220, 50, 50));

        // --- Right Scale Pan (America Side) ---
        drawPan(g2, rightX, beamY, "America", new Color(70, 130, 180));
    }

    private void drawPan(Graphics2D g2, int x, int y, String label, Color c) {
        int panWidth = 70;
        int panDrop = 70;

        // Support Chains
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(new Color(180, 180, 180));
        g2.draw(new Line2D.Double(x, y, x - (panWidth / 2), y + panDrop));
        g2.draw(new Line2D.Double(x, y, x + (panWidth / 2), y + panDrop));

        // Pan Plate
        g2.setColor(new Color(212, 175, 55));
        g2.fillArc(x - (panWidth / 2), y + panDrop - 10, panWidth, 20, 180, 180);

        // Weight/Orb on the Pan representing the Block
        g2.setColor(c);
        g2.fillOval(x - 12, y + panDrop - 22, 24, 20);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(label, x - (fm.stringWidth(label) / 2), y + panDrop - 8);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Goddess of Justice Simulation");
            LadyJusticeSimulation panel = new LadyJusticeSimulation();
            frame.add(panel);
            frame.setSize(850, 550);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}