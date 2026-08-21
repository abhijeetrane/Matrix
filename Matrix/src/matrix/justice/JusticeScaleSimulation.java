package matrix.justice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JusticeScaleSimulation extends JPanel implements ActionListener {
    private double angle = 0; // for animation
    private Timer timer;
    private final int WIDTH = 1000;
    private final int HEIGHT = 700;

    public JusticeScaleSimulation() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(245, 235, 220));
        timer = new Timer(30, this); // ~33 FPS
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        angle += 0.04; // speed of oscillation
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // --- 1. Background ---
        GradientPaint bg = new GradientPaint(0, 0, new Color(250, 240, 225), 0, h, new Color(210, 190, 160));
        g2.setPaint(bg);
        g2.fillRect(0, 0, w, h);

        // --- 2. Top Text: INDIA ---
        g2.setFont(new Font("Serif", Font.BOLD, 52));
        g2.setColor(new Color(138, 0, 0));
        String indiaText = "INDIA";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(indiaText, w/2 - fm.stringWidth(indiaText)/2, 70);

        g2.setFont(new Font("Serif", Font.PLAIN, 18));
        g2.setColor(new Color(60, 60, 60));
        String sub = "Goddess of Justice - Balancing The World Powers";
        g2.drawString(sub, w/2 - g2.getFontMetrics().stringWidth(sub)/2, 95);

        // --- 3. Scale Calculations ---
        int pivotX = w / 2;
        int pivotY = 280;
        int beamHalf = 300;
        double maxTilt = Math.toRadians(12); // 12 degrees tilt
        double currentTilt = Math.sin(angle) * maxTilt;

        int leftBeamX = (int) (pivotX - beamHalf * Math.cos(currentTilt));
        int leftBeamY = (int) (pivotY + beamHalf * Math.sin(currentTilt));

        int rightBeamX = (int) (pivotX + beamHalf * Math.cos(currentTilt));
        int rightBeamY = (int) (pivotY - beamHalf * Math.sin(currentTilt));

        // --- 4. Draw Goddess of Justice (center, behind the scale pole) ---
        drawGoddess(g2, pivotX, 420);

        // --- 5. Draw Scale Structure ---
        g2.setStroke(new BasicStroke(6));
        g2.setColor(new Color(80, 60, 30));
        // Central Pole
        g2.drawLine(pivotX, pivotY, pivotX, 550);
        // Beam
        g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(180, 140, 60));
        g2.drawLine(leftBeamX, leftBeamY, rightBeamX, rightBeamY);
        // Pivot circle
        g2.setColor(new Color(120, 90, 40));
        g2.fillOval(pivotX - 10, pivotY - 10, 20, 20);

        // --- 6. Draw Plates with Strings ---
        drawPlate(g2, leftBeamX, leftBeamY, "RUSSIA", new String[]{"China", "Iran", "N. Korea", "Belarus"}, new Color(200, 60, 60));
        drawPlate(g2, rightBeamX, rightBeamY, "AMERICA", new String[]{"NATO", "UK", "EU", "Japan", "Australia"}, new Color(60, 90, 200));
    }

    private void drawGoddess(Graphics2D g2, int cx, int cy) {
        // cy is base position
        g2.setColor(new Color(230, 215, 180)); // robe color
        // Robe / Body
        int[] xPoints = {cx - 70, cx + 70, cx + 50, cx - 50};
        int[] yPoints = {cy + 30, cy + 30, cy - 130, cy - 130};
        g2.fillPolygon(xPoints, yPoints, 4);

        // Sash
        g2.setColor(new Color(180, 140, 60));
        g2.fillRect(cx - 10, cy - 130, 20, 110);

        // Neck
        g2.setColor(new Color(235, 205, 165));
        g2.fillRect(cx - 12, cy - 150, 24, 25);

        // Head
        g2.fillOval(cx - 35, cy - 210, 70, 75);

        // Hair
        g2.setColor(new Color(50, 35, 20));
        g2.fillArc(cx - 38, cy - 215, 76, 50, 0, 180);

        // Black Cloth over eyes
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(cx - 32, cy - 185, 64, 16, 8, 8);
        // Cloth knot at back
        g2.fillRect(cx + 28, cy - 182, 18, 6);

        // Eyes hint (covered, so just shadow line)
        // Arms holding sword and scale
        g2.setColor(new Color(235, 205, 165));
        g2.setStroke(new BasicStroke(14, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // Left arm to sword
        g2.drawLine(cx - 40, cy - 100, cx - 110, cy - 20);
        // Right arm to scale pole
        g2.drawLine(cx + 40, cy - 100, cx + 20, cy - 120);

        // Sword
        g2.setColor(new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(5));
        int swordX = cx - 115;
        g2.drawLine(swordX, cy - 50, swordX, cy + 90);
        g2.setColor(new Color(120, 90, 40));
        g2.drawLine(swordX - 12, cy - 20, swordX + 12, cy - 20); // hilt
    }

    private void drawPlate(Graphics2D g2, int beamX, int beamY, String mainPower, String[] allies, Color powerColor) {
        int plateY = beamY + 90;
        int plateWidth = 200;
        int plateHeight = 20;

        // Strings
        g2.setColor(new Color(90, 70, 50));
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(beamX, beamY, beamX - plateWidth/2 + 10, plateY);
        g2.drawLine(beamX, beamY, beamX + plateWidth/2 - 10, plateY);
        g2.drawLine(beamX, beamY, beamX, plateY);

        // Plate
        g2.setColor(new Color(180, 140, 60));
        g2.fillOval(beamX - plateWidth/2, plateY, plateWidth, plateHeight);
        g2.setColor(new Color(120, 90, 40));
        g2.drawOval(beamX - plateWidth/2, plateY, plateWidth, plateHeight);

        // Content on plate - box
        int boxWidth = 160;
        int boxHeight = 90;
        int boxX = beamX - boxWidth/2;
        int boxY = plateY - boxHeight + 5;

        g2.setColor(new Color(255, 255, 255, 230));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 12, 12);
        g2.setColor(powerColor);
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 12, 12);

        // Main Power Text
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(powerColor.darker());
        g2.drawString(mainPower, beamX - fm.stringWidth(mainPower)/2, boxY + 18);

        // Allies list
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.setColor(Color.DARK_GRAY);
        int startY = boxY + 32;
        for (int i = 0; i < allies.length; i++) {
            String ally = "+ " + allies[i];
            g2.drawString(ally, boxX + 15, startY + i * 13);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Goddess of Justice - India Balancing Powers");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JusticeScaleSimulation());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}