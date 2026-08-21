package matrix.uitility;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class ThreeCirclesOverlap extends JPanel {

    private static final int CIRCLE_RADIUS = 90;
    private static final int ORBIT_RADIUS = 65;
    
    private double redAngle = 0;   // clockwise
    private double greenAngle = 0; // anti-clockwise

    public ThreeCirclesOverlap() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(600, 600));

        // 60 FPS animation
        Timer timer = new Timer(16, e -> {
            redAngle += Math.toRadians(1.5);   // clockwise -> angle increases
            greenAngle -= Math.toRadians(1.5); // anti-clockwise -> angle decreases
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Blue circle - static, at center
        double blueX = centerX - CIRCLE_RADIUS;
        double blueY = centerY - CIRCLE_RADIUS;
        Ellipse2D.Double blueCircle = new Ellipse2D.Double(blueX, blueY, CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);

        // Red circle - rotates clockwise around center
        double redCenterX = centerX + ORBIT_RADIUS * Math.cos(redAngle);
        double redCenterY = centerY + ORBIT_RADIUS * Math.sin(redAngle);
        Ellipse2D.Double redCircle = new Ellipse2D.Double(
                redCenterX - CIRCLE_RADIUS,
                redCenterY - CIRCLE_RADIUS,
                CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);

        // Green circle - rotates anti-clockwise around center
        double greenCenterX = centerX + ORBIT_RADIUS * Math.cos(greenAngle);
        double greenCenterY = centerY + ORBIT_RADIUS * Math.sin(greenAngle);
        Ellipse2D.Double greenCircle = new Ellipse2D.Double(
                greenCenterX - CIRCLE_RADIUS,
                greenCenterY - CIRCLE_RADIUS,
                CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);

        // 1. Draw the 3 base circles
        g2.setColor(Color.RED); // Red -> Black
        g2.fill(redCircle);

        g2.setColor(Color.GREEN); // Green ->White
        g2.fill(greenCircle);

        g2.setColor(Color.BLUE); // Blue -> Gray
        g2.fill(blueCircle);

        // 2. Calculate overlapping area of all 3 circles
        Area overlap = new Area(redCircle);
        overlap.intersect(new Area(greenCircle));
        overlap.intersect(new Area(blueCircle));

        // 3. Paint the common intersection as Grey
        if (!overlap.isEmpty()) {
            g2.setColor(new Color(165, 42, 42)); // Grey -> Brown
            g2.fill(overlap);
        }

        // Optional: Draw borders for clarity
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        g2.draw(redCircle);
        g2.draw(greenCircle);
        g2.draw(blueCircle);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("3 Overlapping Circles - Rotating");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ThreeCirclesOverlap());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}