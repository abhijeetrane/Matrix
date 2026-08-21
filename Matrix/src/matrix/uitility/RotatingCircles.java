package matrix.uitility;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class RotatingCircles extends JPanel {

    private double angle = 0; // Rotation angle in radians
    private final int circleRadius = 110;
    private final int orbitRadius = 45; // Radius of orbital movement

    public RotatingCircles() {
        // Set up animation timer (~60 FPS)
        Timer timer = new Timer(16, e -> {
            angle += 0.03; // Increment rotation angle
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        // Enable anti-aliasing for smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // --- Calculate Center Positions ---
        // Circle 1: Anti-clockwise rotation around (centerX - 40, centerY)
        double c1x = (centerX - 40) + orbitRadius * Math.cos(-angle);
        double c1y = centerY + orbitRadius * Math.sin(-angle);

        // Circle 2: Clockwise rotation around (centerX + 40, centerY)
        double c2x = (centerX + 40) + orbitRadius * Math.cos(angle);
        double c2y = centerY + orbitRadius * Math.sin(angle);

        // Create Circle shapes
        Ellipse2D.Double circle1 = new Ellipse2D.Double(
                c1x - circleRadius, c1y - circleRadius, circleRadius * 2, circleRadius * 2);
        Ellipse2D.Double circle2 = new Ellipse2D.Double(
                c2x - circleRadius, c2y - circleRadius, circleRadius * 2, circleRadius * 2);

        // --- Construct Area Geometry for Overlap & Non-Overlap ---
        Area area1 = new Area(circle1);
        Area area2 = new Area(circle2);

        // Green region (Circle 1 ONLY)
        Area greenArea = new Area(area1);
        greenArea.subtract(area2);

        // Red region (Circle 2 ONLY)
        Area redArea = new Area(area2);
        redArea.subtract(area1);

        // Blue region (INTERSECTION)
        Area overlapArea = new Area(area1);
        overlapArea.intersect(area2);

        // --- Render Shaded Areas ---
        // 1. Draw Green area (Anti-clockwise circle)
        g2d.setColor(new Color(46, 184, 92)); // Vibrant Green
        g2d.fill(greenArea);

        // 2. Draw Red area (Clockwise circle)
        g2d.setColor(new Color(220, 53, 69)); // Vibrant Red
        g2d.fill(redArea);

        // 3. Draw Blue area (Overlap)
        g2d.setColor(new Color(13, 110, 253)); // Vibrant Blue
        g2d.fill(overlapArea);

        // --- Render Outlines & Rotation Indicator Lines ---
        g2d.setStroke(new BasicStroke(2f));

        // Circle 1 Outlines & Anti-clockwise indicator line
        g2d.setColor(Color.WHITE);
        g2d.draw(circle1);
        int line1X = (int) (c1x + circleRadius * Math.cos(-angle));
        int line1Y = (int) (c1y + circleRadius * Math.sin(-angle));
        g2d.drawLine((int) c1x, (int) c1y, line1X, line1Y);

        // Circle 2 Outlines & Clockwise indicator line
        g2d.draw(circle2);
        int line2X = (int) (c2x + circleRadius * Math.cos(angle));
        int line2Y = (int) (c2y + circleRadius * Math.sin(angle));
        g2d.drawLine((int) c2x, (int) c2y, line2X, line2Y);

        // Draw center dots
        g2d.fillOval((int) c1x - 4, (int) c1y - 4, 8, 8);
        g2d.fillOval((int) c2x - 4, (int) c2y - 4, 8, 8);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rotating Overlapping Circles");
            RotatingCircles panel = new RotatingCircles();
            panel.setBackground(new Color(30, 30, 30)); // Dark background

            frame.add(panel);
            frame.setSize(600, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}